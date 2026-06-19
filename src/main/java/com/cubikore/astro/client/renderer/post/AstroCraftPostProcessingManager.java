package com.cubikore.astro.client.renderer.post;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.client.renderer.AstroCraftRenderer;
import com.cubikore.astro.client.renderer.ShadowRenderer;
import com.cubikore.astro.dimension.DimensionKeys;
import com.cubikore.astro.editor.InfoEditor;
import com.cubikore.astro.editor.PlanetEditor;
import com.cubikore.astro.game.client.AstroCraftClientGameManager;
import com.cubikore.astro.universe.Planet;
import com.cubikore.astro.universe.Universe;
import com.cubikore.astro.weather.planet.ClientWeather;
import foundry.veil.api.client.editor.EditorManager;
import foundry.veil.api.client.registry.LightTypeRegistry;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import foundry.veil.api.client.render.post.PostProcessingManager;
import foundry.veil.api.client.render.shader.program.ShaderProgram;
import foundry.veil.platform.VeilEventPlatform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class AstroCraftPostProcessingManager {
    public static Identifier PLANETS_ID = Identifier.of(AstroCraft.MOD_ID, "planets");
    public static Identifier ATMOSPHERE_SHADER = Identifier.of(AstroCraft.MOD_ID, "atmosphere");
    public static Identifier STARS_SHADER = Identifier.of(AstroCraft.MOD_ID, "stars");
    public static Identifier PLANET_SKY_ID = Identifier.of(AstroCraft.MOD_ID, "planetsky");
    public static Identifier PLANET_FOG_ID = Identifier.of(AstroCraft.MOD_ID, "planetfog");
    public static Identifier BLOOM_ID = Identifier.of(AstroCraft.MOD_ID, "bloom");
    public static Identifier SHADOWS_ID = Identifier.of(AstroCraft.MOD_ID, "shadows");

    private AstroCraftRenderer renderer;

    public AstroCraftPostProcessingManager(AstroCraftRenderer renderer) {
        this.renderer = renderer;
    }

    public void init() {
        initEvents();
    }

    public void initEvents() {
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

        VeilEventPlatform.INSTANCE.preVeilPostProcessing(((pipelineName, postPipeline, context) -> {
            if(client.world != null && client.player != null) {
                AstroCraftClientGameManager gameManager = AstroCraftClient.clientGameManager;

                ClientWeather weather = gameManager.weatherManager.get(client.world.getRegistryKey());

                boolean inSpace = DimensionKeys.inSpace(client.player);

                float gameTime = gameManager.getGameTime();

                Camera camera = renderer.getCamera();

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
                            if(i >= 32)
                                break;

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
                            if(i >= 32)
                                break;

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
}
