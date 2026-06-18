package com.cubikore.astro;

import com.cubikore.astro.block.AstroCraftBlocks;
import com.cubikore.astro.block.entity.AstroCraftBlockEntities;
import com.cubikore.astro.block.entity.renderer.BrightLightBlockEntityRenderer;
import com.cubikore.astro.block.fluid.AstroCraftFluids;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.client.cutscene.CutsceneManager;
import com.cubikore.astro.client.cutscene.FTLJumpCutscene;
import com.cubikore.astro.client.light.PositionPointLightData;
import com.cubikore.astro.client.renderer.ShadowRenderer;
import com.cubikore.astro.math.AstMath;
import com.cubikore.astro.util.CommonStorage;
import com.cubikore.astro.dimension.DimensionKeys;
import com.cubikore.astro.editor.InfoEditor;
import com.cubikore.astro.editor.PlanetEditor;
import com.cubikore.astro.networking.AstroCraftNetworking;
import com.cubikore.astro.networking.payload.ShipMovingPayload;
import com.cubikore.astro.particle.AstParticleManager;
import com.cubikore.astro.sound.VenusHighWindSoundInstance;
import com.cubikore.astro.sound.VenusLowWindSoundInstance;
import com.cubikore.astro.universe.Planet;
import com.cubikore.astro.universe.Universe;
import com.cubikore.astro.util.PlayerLightAccess;
import com.cubikore.astro.weather.ClientWeatherManager;
import com.cubikore.astro.weather.PlanetWeather;
import com.cubikore.astro.weather.planet.ClientWeather;
import com.cubikore.astro.weather.planet.VenusClientWeather;
import foundry.veil.api.client.editor.EditorManager;
import foundry.veil.api.client.registry.LightTypeRegistry;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import foundry.veil.api.client.render.post.PostProcessingManager;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import foundry.veil.api.client.render.shader.program.ShaderProgram;
import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.platform.VeilEventPlatform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.Random;
import org.joml.*;
import org.lwjgl.glfw.GLFW;

import java.lang.Math;
import java.util.Arrays;

public class AstroCraftClient implements ClientModInitializer {
    public static Identifier PLANETS_ID = Identifier.of(AstroCraft.MOD_ID, "planets");
    public static Identifier ATMOSPHERE_SHADER = Identifier.of(AstroCraft.MOD_ID, "atmosphere");
    public static Identifier STARS_SHADER = Identifier.of(AstroCraft.MOD_ID, "stars");
    public static Identifier PLANET_SKY_ID = Identifier.of(AstroCraft.MOD_ID, "planetsky");
    public static Identifier PLANET_FOG_ID = Identifier.of(AstroCraft.MOD_ID, "planetfog");
    public static Identifier BLOOM_ID = Identifier.of(AstroCraft.MOD_ID, "bloom");
    public static Identifier SHADOWS_ID = Identifier.of(AstroCraft.MOD_ID, "shadows");

    private static final long startTime = System.currentTimeMillis();
    private static long lastTime = System.currentTimeMillis();

    public static Vector4f prevWorldOffset = new Vector4f(0);
    public static Vector4f targetOffsetPos = new Vector4f(0);

    private static final Vector4f lastDirection = new Vector4f(0);

    public static float interpTime = 0;

    public static AstParticleManager particleManager;
    public static VenusLowWindSoundInstance venusLowWindSoundInstance;
    public static VenusHighWindSoundInstance venusHighWindSoundInstance;

    public static ClientWeatherManager weatherManager = new ClientWeatherManager();
    public static CutsceneManager cutsceneManager = new CutsceneManager();

    public static final Identifier PARTICLE_LAYER_P = Identifier.of(AstroCraft.MOD_ID, "astparticle");
    public static final Identifier UNLIT_LAYER = Identifier.of(AstroCraft.MOD_ID, "unlit");

    private Camera camera;

    private java.util.Random random = new java.util.Random();

    @Override
    public void onInitializeClient() {
        AstroCraftNetworking.registerS2CPayloads();

        registerClientEvents();

        initVeilEvents();

        ClientStorage.windNoiseSampler = new SimplexNoiseSampler(Random.create(System.nanoTime()));

        weatherManager.add(Universe.SPACE_ID, new ClientWeather("space"));
        weatherManager.add(Universe.EARTH_ID, new ClientWeather("earth"));
        weatherManager.add(Universe.VENUS_ID, new VenusClientWeather());

        cutsceneManager.addScene(new FTLJumpCutscene());
    }

    private void registerClientEvents() {
        registerBlockRenderEvents();

        WorldRenderEvents.END.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if(client.player != null) {
                ClientWeather weather = weatherManager.get(context.gameRenderer().getClient().world.getRegistryKey());

                if(weather != null)
                    weather.tickFrame();

                cutsceneManager.frame(client);

                handleSunLight(client);
                handleBlockLights(client);
            }
        });

        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register(((client, world) -> {
            boolean inSpace = DimensionKeys.isSpace(world);
            System.out.println(inSpace);

            if(!inSpace) {
                removeAllLights();
            }

            if(client.player != null) {
                PlayerLightAccess access = (PlayerLightAccess) client.player;
                access.removeLight();
            }
        }));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = client.player;
            if(player != null) {
                if(player.hasVehicle() && player.getVehicle() instanceof DisplayEntity.TextDisplayEntity entity && CommonStorage.captainSeatEntities.containsKey(player.getVehicle().getBlockPos()))
                    handleShipMovements();

                cutsceneManager.tick(client);
            }
        });

        FluidRenderHandlerRegistry.INSTANCE.register(
                AstroCraftFluids.STILL_SULFURIC_ACID, AstroCraftFluids.FLOWING_SULFURIC_ACID,
                new SimpleFluidRenderHandler(Identifier.ofVanilla("block/water_still"), Identifier.ofVanilla("block/water_flow"), 0xffd573)
        );
    }

    public static void removeAllLights() {
        for(LightRenderHandle<PositionPointLightData> lightData : ClientStorage.BLOCK_LIGHTS.values()) {
            lightData.free();
        }

        ClientStorage.BLOCK_LIGHTS.clear();
    }

    private void handleBlockLights(MinecraftClient client) {
        if(!Arrays.equals(ClientStorage.terrainOffset, ClientStorage.prevTerrainOffset)) {
            for(LightRenderHandle<PositionPointLightData> lightHandle : ClientStorage.BLOCK_LIGHTS.values()) {
                PositionPointLightData light = lightHandle.getLightData();

                Vector3d offseted = new Vector3d(0);
                light.getTruePosition().add(ClientStorage.terrainOffset[0], ClientStorage.terrainOffset[1], ClientStorage.terrainOffset[2], offseted);
                light.setPosition(offseted.x, offseted.y, offseted.z);
            }
        }

        ClientStorage.prevTerrainOffset[0] = ClientStorage.terrainOffset[0];
        ClientStorage.prevTerrainOffset[1] = ClientStorage.terrainOffset[1];
        ClientStorage.prevTerrainOffset[2] = ClientStorage.terrainOffset[2];
    }

    private void handleSunLight(MinecraftClient client) {

        ClientPlayerEntity player = client.player;

        Planet sun = Universe.getPlanet(Universe.SUN_ID);

        if(sun != null && player != null) {
            boolean inSpace = DimensionKeys.inSpace(player);

            if(inSpace) {

                Vector3f pos = new Vector3f(ClientStorage.renderedWorldOffset.x, ClientStorage.renderedWorldOffset.y, ClientStorage.renderedWorldOffset.z);
                float renderedYaw = ClientStorage.renderedWorldOffset.w;

                Vector3f sunPos = new Vector3f(sun.position[0], sun.position[1], sun.position[2]);
                Vector3f sunDir = new Vector3f(sunPos).sub(pos).normalize();

                float distanceFromSun = sunPos.distance(pos);

                float sunYaw = (float)Math.toDegrees(Math.atan2(-sunDir.x, sunDir.z)) ;

                float angle = sunYaw - renderedYaw;

                ClientStorage.sunOrientation[0] = 20f;
                ClientStorage.sunOrientation[1] = angle;

                float OI = (distanceFromSun - 8000f) / 500000f;
                OI = Math.clamp(1f - OI, 0.1f, 1.0f);

                ClientStorage.sunBrightness[0] = OI * 5f;
            }
        }
    }

    private void registerBlockRenderEvents() {
        BlockRenderLayerMap.INSTANCE.putBlock(
                AstroCraftBlocks.REINFORCED_GLASS_BLOCK,
                RenderLayer.getCutout()
        );
        BlockRenderLayerMap.INSTANCE.putBlock(
                AstroCraftBlocks.REINFORCED_GLASS_PANE_BLOCK,
                RenderLayer.getCutout()
        );

        BlockEntityRendererFactories.register(
                AstroCraftBlockEntities.BRIGHT_LIGHT_BLOCK_ENTITY,
                BrightLightBlockEntityRenderer::new
        );
    }

    public static void weatherChanged(MinecraftClient client, PlayerEntity player, boolean planetChanged, Identifier fromPlanet) {
        if(planetChanged) {
            ClientWeather weather = AstroCraftClient.weatherManager.get(fromPlanet);

            weather.cleanUp();
        }

        PlanetWeather currentWeather = ClientStorage.currentWeather;
        Identifier planetId = Universe.getPlanetIdFromWorld(player.getWorld().getRegistryKey());

        weatherManager.changed(planetId, currentWeather.getType(), planetChanged);

        ClientStorage.weatherChangedTime = System.currentTimeMillis();
    }

    public static void interpolateShip() {
        float dt = getDeltaTime(); // seconds
        float k = 2.0f;            // stiffness (higher = snappier)

        float alpha = 1f - (float)Math.exp(-k * dt);

        // exponential: smooths towards target over time
        ClientStorage.renderedWorldOffset.set(
                AstMath.lerp(prevWorldOffset, targetOffsetPos, alpha)
        );

        prevWorldOffset.set(ClientStorage.renderedWorldOffset);
    }

    private void initVeilEvents() {
        MinecraftClient client = MinecraftClient.getInstance();

        VeilEventPlatform.INSTANCE.onVeilRendererAvailable(renderer -> {
            if(VeilRenderSystem.hasImGui()) {
                EditorManager editorManager = renderer.getEditorManager();

                editorManager.add(new PlanetEditor());
                editorManager.add(new InfoEditor());

                PostProcessingManager postProcessingManager = renderer.getPostProcessingManager();

                postProcessingManager.add(SHADOWS_ID);
                postProcessingManager.add(PLANETS_ID);
                postProcessingManager.add(PLANET_FOG_ID);
                postProcessingManager.add(PLANET_SKY_ID);
                postProcessingManager.add(BLOOM_ID);
            }
        });

        VeilEventPlatform.INSTANCE.onVeilRenderLevelStage(((stage, levelRenderer, bufferSource, matrixStack, frustumMatrix, projectionMatrix, renderTick, deltaTracker, camera, frustum) -> {
            if(this.camera == null) {
                this.camera = camera;
            }

            if(stage == VeilRenderLevelStageEvent.Stage.AFTER_SKY) {
                if (camera != null) {
                    ShadowRenderer.render(client, camera, (MatrixStack) matrixStack, bufferSource, deltaTracker.getTickDelta(true));
                }
            }

            if(stage == VeilRenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                if(camera != null) {
                    RenderLayer layer = VeilRenderType.get(AstroCraftClient.PARTICLE_LAYER_P);
                    particleManager.renderParticles(camera, matrixStack.toPoseStack(), bufferSource.getBuffer(layer));
                }
            }
        }));

        VeilEventPlatform.INSTANCE.preVeilPostProcessing(((pipelineName, postPipeline, context) -> {
            if(client.world != null && client.player != null) {
                ClientWeather weather = weatherManager.get(client.world.getRegistryKey());

                boolean inSpace = DimensionKeys.inSpace(client.player);

                float gameTime = getGameTime();

                if (SHADOWS_ID.equals(pipelineName)) {
                    ShaderProgram shadowsShader = context.getShader(SHADOWS_ID);

                    if (shadowsShader != null) {
                        //shadowsShader.getUniform("lightViewProjection").setMatrix(ShadowRenderer.getLightViewProjection());
                        shadowsShader.getUniformSafe("inSpace").setInt(inSpace ? 1 : 0);
                        shadowsShader.getUniformSafe("shadowViewMatrix").setMatrix(ShadowRenderer.shadowModelView(camera).peek().getPositionMatrix());
                        shadowsShader.getUniformSafe("shadowSpaceMatrix").setMatrix(ShadowRenderer.getLightSpaceMatrix());
                        shadowsShader.getUniformSafe("shadowOrthographMatrix").setMatrix(ShadowRenderer.createProjMat());
                        shadowsShader.getUniformSafe("lightColor").setVector(ClientStorage.sunColor);
                        shadowsShader.getUniformSafe("brightness").setFloat(ClientStorage.sunBrightness[0]);
                    }
                }

                if (PLANET_FOG_ID.equals(pipelineName)) {
                    ShaderProgram planetFogShader = context.getShader(PLANET_FOG_ID);

                    if (planetFogShader != null && weather != null) {
                        planetFogShader.getUniformSafe("shouldRender").setInt(weather.currentAtmosphereConditions.hasFog ? 1 : 0);

                        planetFogShader.getUniformSafe("lightDarkFactor").setFloat(weather.currentAtmosphereConditions.lightDarkFactor);
                        planetFogShader.getUniformSafe("fogFactor").setFloat(weather.currentAtmosphereConditions.fogDistance);

                        planetFogShader.getUniformSafe("fogColor").setVector(weather.currentAtmosphereConditions.fogColor);
                        planetFogShader.getUniformSafe("fogDarkColor").setVector(weather.currentAtmosphereConditions.fogDarkColor);

                        planetFogShader.getUniformSafe("shadowViewMatrix").setMatrix(ShadowRenderer.shadowModelView(camera).peek().getPositionMatrix());
                        planetFogShader.getUniformSafe("shadowOrthographMatrix").setMatrix(ShadowRenderer.createProjMat());

                        if(ClientStorage.shaderLightsDirty) {
                            for(int i = 0; i < 32; i++) {
                                planetFogShader.getUniformSafe("pointPositions[" + i + "]").setVector(new Vector3f(0));
                                planetFogShader.getUniformSafe("pointBrightnesses[" + i + "]").setFloat(0);
                                planetFogShader.getUniformSafe("pointRadii[" + i + "]").setFloat(0);
                            }

                            planetFogShader.getUniformSafe("pointLightCount").setInt(0);

                            ClientStorage.shaderLightsDirty = false;
                        }

                        int i = 0;
                        LightTypeRegistry.LightType<PointLightData> lightType = LightTypeRegistry.POINT.get();
                        for(LightRenderHandle<PointLightData> handle : VeilRenderSystem.renderer().getLightRenderer().getLights(lightType)) {
                            if(handle.isValid()) {
                                if(i >= 32)
                                    break;

                                PointLightData lightData = handle.getLightData();

                                Vector3f pos = new Vector3f((float) lightData.getPosition().x(), (float) lightData.getPosition().y(), (float) lightData.getPosition().z());

                                planetFogShader.getUniformSafe("pointPositions[" + i + "]").setVector(pos);
                                planetFogShader.getUniformSafe("pointBrightnesses[" + i + "]").setFloat(lightData.getBrightness());
                                planetFogShader.getUniformSafe("pointRadii[" + i + "]").setFloat(lightData.getRadius());

                                i++;
                            }
                        }

                        planetFogShader.getUniformSafe("pointLightCount").setInt(i + 1);
                    }
                }

                if (PLANET_SKY_ID.equals(pipelineName)) {
                    ShaderProgram planetSkyShader = context.getShader(PLANET_SKY_ID);

                    if (planetSkyShader != null && weather != null) {
                        planetSkyShader.getUniformSafe("shouldRender").setInt(inSpace ? 0 : 1);

                        planetSkyShader.getUniformSafe("sunSize").setFloat(weather.currentAtmosphereConditions.sunSize);
                        planetSkyShader.getUniformSafe("sunWhiteSize").setFloat(weather.currentAtmosphereConditions.sunWhiteSize);

                        planetSkyShader.getUniformSafe("shadowViewMatrix").setMatrix(ShadowRenderer.shadowModelView(camera).peek().getPositionMatrix());
                    }
                }

                if (PLANETS_ID.equals(pipelineName)) {
                    ShaderProgram starsShader = context.getShader(STARS_SHADER);
                    ShaderProgram planetShader = context.getShader(PLANETS_ID);
                    ShaderProgram atmosphereShader = context.getShader(ATMOSPHERE_SHADER);

                    if (starsShader != null) {
                        starsShader.getUniformSafe("shouldRender").setInt(inSpace ? 1 : 0);
                        starsShader.getUniformSafe("yawOffset").setFloat(ClientStorage.renderedWorldOffset.w);
                        starsShader.getUniformSafe("gameTime").setFloat(gameTime);
                    }

                    if (planetShader != null) {
                        planetShader.getUniformSafe("sphereCount").setInt(Universe.planets.size());
                        planetShader.getUniformSafe("gameTime").setFloat(gameTime);
                        planetShader.getUniformSafe("worldOffset").setVector(ClientStorage.renderedWorldOffset);
                        planetShader.getUniformSafe("shouldRender").setInt(inSpace ? 1 : 0);

                        for (int i = 0; i < Universe.planets.size(); i++) {
                            Planet planet = Universe.planets.get(i);

                            planetShader.getUniformSafe("positions[" + i + "]").setVector(planet.position);
                            planetShader.getUniformSafe("radii[" + i + "]").setFloat(planet.radius[0]);
                            planetShader.getUniformSafe("colors[" + i + "]").setVector(planet.color);
                            planetShader.getUniformSafe("atmosphereRadii[" + i + "]").setFloat(planet.atmosphere.radius[0]);
                        }
                    }

                    if (atmosphereShader != null) {
                        atmosphereShader.getUniformSafe("sphereCount").setInt(Universe.planets.size());
                        atmosphereShader.getUniformSafe("gameTime").setFloat(gameTime);
                        atmosphereShader.getUniformSafe("worldOffset").setVector(ClientStorage.renderedWorldOffset);
                        atmosphereShader.getUniformSafe("shouldRender").setInt(inSpace ? 1 : 0);

                        for (int i = 0; i < Universe.planets.size(); i++) {
                            Planet planet = Universe.planets.get(i);

                            atmosphereShader.getUniformSafe("positions[" + i + "]").setVector(planet.position);
                            atmosphereShader.getUniformSafe("radii[" + i + "]").setFloat(planet.radius[0]);
                            atmosphereShader.getUniformSafe("atmosphereRadii[" + i + "]").setFloat(planet.atmosphere.radius[0]);
                            atmosphereShader.getUniformSafe("densityFallOffs[" + i + "]").setFloat(planet.atmosphere.fallOff[0]);
                            atmosphereShader.getUniformSafe("wavelengthses[" + i + "]").setVector(planet.atmosphere.wavelengths);
                            atmosphereShader.getUniformSafe("scatteringStrengths[" + i + "]").setFloat(planet.atmosphere.scatteringStrength[0]);
                            atmosphereShader.getUniformSafe("colors[" + i + "]").setVector(planet.color);
                        }
                    }
                }
            }
        }));
    }

    private void handleShipMovements() {
        if(!ClientStorage.doingFTLJump) {

            Vector4f direction = new Vector4f(0);

            if (isKeyPressed(GLFW.GLFW_KEY_W)) {
                direction.add(1, 0, 0, 0);
            }
            if (isKeyPressed(GLFW.GLFW_KEY_S)) {
                direction.add(-1, 0, 0, 0);
            }
            if (isKeyPressed(GLFW.GLFW_KEY_A)) {
                direction.add(0, 0, 0, -1);
            }
            if (isKeyPressed(GLFW.GLFW_KEY_D)) {
                direction.add(0, 0, 0, 1);
            }
            if(isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
                direction.add(0, 0, 0, -1);
            }
            if(isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
                direction.add(0, 0, 0, 1);
            }

            boolean changed = !direction.equals(lastDirection);

            if (changed) {
                ClientPlayNetworking.send(new ShipMovingPayload(direction.x, direction.y, direction.z, direction.w, true));
            }

            ClientStorage.stoppedHandeled = false;

            // Update previous direction
            lastDirection.set(direction);
        }
        else {
            if(!ClientStorage.stoppedHandeled) {
                ClientStorage.stoppedHandeled = true;
                ClientPlayNetworking.send(new ShipMovingPayload(0, 0, 0, 0, true));
            }
        }
    }

    public static float getDeltaTime() {
        float d = (float)(System.currentTimeMillis() - lastTime) / 1000;
        lastTime = System.currentTimeMillis();
        return d;
    }

    public static boolean allTrue(boolean[] arr) {
        for (boolean b : arr) {
            if (!b) return false;
        }
        return true;
    }

    public static boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), key) == GLFW.GLFW_PRESS;
    }

    public static float getGameTime() {
        return (float)(System.currentTimeMillis() - startTime) / 1000;
    }
}
