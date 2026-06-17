package com.cubikore.astro.ship;

import com.cubikore.astro.math.AstMath;

public class ShipParameters {
    public static float shipSpeed = 0f;

    public static float getSpeed(float d) {

        // Exponential slowdown when below 1500m
        if (d <= 1500f) {
            // t goes from 0 (surface) to 1 (1500m)
            float t = d / 1500f;

            // Exponential curve: very slow near the ground, approaches 1f at 1500m
            // Adjust exponent (-3f) for stronger/weaker slowdown
            return (float)Math.max(Math.exp(-(1f - t)), 0.1);
        }

        if (d <= 3000f) return remap(d, 1500f, 3000f, 1f, 5f);
        if (d <= 10000f) return remap(d, 3000f, 10000f, 5f, 10f);
        if (d <= 20000f) return remap(d, 10000f, 20000f, 10f, 20f);
        if (d <= 30000f) return remap(d, 20000f, 30000f, 20f, 30f);
        if (d <= 40000f) return remap(d, 30000f, 40000f, 30f, 40f);
        if (d <= 70000f) return remap(d, 40000f, 70000f, 40f, 100f);
        return 100f;
    }

    private static float remap(float value, float inMin, float inMax, float outMin, float outMax) {
        float t = (value - inMin) / (inMax - inMin);
        t = Math.clamp(t, 0f, 1f);
        return AstMath.lerp(outMin, outMax, t);
    }
}
