package com.cubikore.astro.client;

import com.cubikore.astro.client.light.PositionPointLightData;
import com.cubikore.astro.weather.PlanetWeather;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import org.joml.Vector4f;

import java.util.HashMap;

public class ClientStorage {
    public static SimplexNoiseSampler windNoiseSampler;

    public static Vector4f renderedWorldOffset = new Vector4f(0);

    public static float windStrength = 0f;

    public static PlanetWeather currentWeather;

    public static long weatherChangedTime;

    public static boolean stoppedHandeled = false;
    public static boolean doingFTLJump = false;
    public static Vector4f FTLJumpDestination = new Vector4f(0);

    public static HashMap<BlockPos, LightRenderHandle<PositionPointLightData>> BLOCK_LIGHTS = new HashMap<>();

    public static float[] terrainOffset = new float[]{0f, 0f, 0f};
    public static float[] prevTerrainOffset = new float[]{0f, 0f, 0f};

    public static float[] sunOrientation = new float[]{45f, 45f};
    public static float[] sunColor = new float[]{0.988f, 0.921f, 0.662f};
    public static float[] sunBrightness = new float[]{1f};

    public static LightRenderHandle<PointLightData> pointLightDataLightRenderHandle;
}
