package com.cubikore.astro.game.client;

import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.client.cutscene.CutsceneManager;
import com.cubikore.astro.client.cutscene.FTLJumpCutscene;
import com.cubikore.astro.client.light.PositionPointLightData;
import com.cubikore.astro.dimension.DimensionKeys;
import com.cubikore.astro.events.client.AstroCraftClientWeatherEvents;
import com.cubikore.astro.math.AstMath;
import com.cubikore.astro.networking.payload.ShipMovingPayload;
import com.cubikore.astro.particle.AstParticleManager;
import com.cubikore.astro.sound.VenusHighWindSoundInstance;
import com.cubikore.astro.sound.VenusLowWindSoundInstance;
import com.cubikore.astro.universe.Planet;
import com.cubikore.astro.universe.Universe;
import com.cubikore.astro.util.CommonStorage;
import com.cubikore.astro.util.PlayerLightAccess;
import com.cubikore.astro.weather.ClientWeatherManager;
import com.cubikore.astro.weather.PlanetWeather;
import com.cubikore.astro.weather.planet.ClientWeather;
import com.cubikore.astro.weather.planet.VenusClientWeather;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class AstroCraftClientGameManager {
    private static final long startTime = System.currentTimeMillis();
    private static long lastTime = System.currentTimeMillis();

    public Vector4f prevWorldOffset = new Vector4f(0);
    public Vector4f targetOffsetPos = new Vector4f(0);
    private final Vector4f lastDirection = new Vector4f(0);

    public ClientWeatherManager weatherManager = new ClientWeatherManager();
    public CutsceneManager cutsceneManager = new CutsceneManager();

    public AstParticleManager particleManager;
    public VenusLowWindSoundInstance venusLowWindSoundInstance;
    public VenusHighWindSoundInstance venusHighWindSoundInstance;

    public void init() {
        weatherManager.add(Universe.SPACE_ID, new ClientWeather("space"));
        weatherManager.add(Universe.EARTH_ID, new ClientWeather("earth"));
        weatherManager.add(Universe.VENUS_ID, new VenusClientWeather());

        cutsceneManager.addScene(new FTLJumpCutscene());

        initEvents();

        ClientStorage.windNoiseSampler = new SimplexNoiseSampler(Random.create(System.nanoTime()));

        AstroCraftClientWeatherEvents.WEATHER_REGISTERING.invoker().onRegisteringClientWeather(weatherManager);
    }

    private void initEvents() {
        WorldRenderEvents.END.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if(client.player != null) {
                ClientWeather weather = weatherManager.get(context.gameRenderer().getClient().world.getRegistryKey());

                if(weather != null)
                    weather.tickFrame();

                cutsceneManager.frame(client);

                interpolateShip();
                handleWind();
                handleSunLight(client);
                handleBlockLights(client);
            }
        });

        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register(((client, world) -> {
            boolean inSpace = DimensionKeys.isSpace(world);

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
    }

    private void handleWind() {
        float gameTime = getGameTime();

        double strength = ClientStorage.windNoiseSampler.sample((gameTime + 32) * 0.03f, (gameTime - 67) * 0.03f) * 0.5 + 0.5;

        ClientStorage.windStrength = (float) strength;
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

            lastDirection.set(direction);
        }
        else {
            if(!ClientStorage.stoppedHandeled) {
                ClientStorage.stoppedHandeled = true;
                ClientPlayNetworking.send(new ShipMovingPayload(0, 0, 0, 0, true));
            }
        }
    }

    public void removeAllLights() {
        for(LightRenderHandle<PositionPointLightData> lightData : ClientStorage.BLOCK_LIGHTS.values()) {
            lightData.free();
        }

        ClientStorage.BLOCK_LIGHTS.clear();
    }

    public void weatherChanged(MinecraftClient client, PlayerEntity player, boolean planetChanged, Identifier fromPlanet) {
        if(planetChanged) {
            ClientWeather weather = weatherManager.get(fromPlanet);

            weather.cleanUp();
        }

        PlanetWeather currentWeather = ClientStorage.currentWeather;
        Identifier planetId = Universe.getPlanetIdFromWorld(player.getWorld().getRegistryKey());

        weatherManager.changed(planetId, currentWeather.getType(), planetChanged);

        ClientStorage.weatherChangedTime = System.currentTimeMillis();
    }

    private void  interpolateShip() {
        float dt = getDeltaTime(); // seconds
        float k = 2.0f;            // stiffness (higher = snappier)

        float alpha = 1f - (float)Math.exp(-k * dt);

        // exponential: smooths towards target over time
        ClientStorage.renderedWorldOffset.set(
                AstMath.lerp(prevWorldOffset, targetOffsetPos, alpha)
        );

        prevWorldOffset.set(ClientStorage.renderedWorldOffset);
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

    public boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), key) == GLFW.GLFW_PRESS;
    }

    public float getGameTime() {
        return (float)(System.currentTimeMillis() - startTime) / 1000;
    }

    public float getDeltaTime() {
        float d = (float)(System.currentTimeMillis() - lastTime) / 1000;
        lastTime = System.currentTimeMillis();
        return d;
    }
}
