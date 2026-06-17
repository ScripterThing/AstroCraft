package com.cubikore.astro.math;

import org.joml.Vector4f;

public class AstMath {
    public static float mod(float x, float y) {
        return (float)(x - y * Math.floor(x / y));
    }

    public static float wrapDegrees(float degrees) {
        degrees = degrees % 360.0f;
        if (degrees >= 180.0f) {
            degrees -= 360.0f;
        }
        if (degrees < -180.0f) {
            degrees += 360.0f;
        }
        return degrees;
    }

    public static Vector4f lerp(Vector4f a, Vector4f b, float t) {
        return new Vector4f(
                a.x + (b.x - a.x) * t,
                a.y + (b.y - a.y) * t,
                a.z + (b.z - a.z) * t,
                a.w + (b.w - a.w) * t
        );
    }

    public static float lerpUnclamped(float startValue, float endValue, float fraction) {
        fraction = Math.clamp(fraction, 0.0f, 1.0f);
        return startValue + (endValue - startValue) * fraction;
    }

    public static float lerp(float startValue, float endValue, float fraction) {
        fraction = Math.clamp(fraction, 0.0f, 1.0f);
        return startValue + (endValue - startValue) * fraction;
    }

    public static float lerpExp(float startValue, float endValue, float exponent, float fraction) {
        fraction = Math.clamp(fraction, 0.0f, 1.0f);
        return (float) (startValue + (endValue - startValue) * (Math.pow(fraction, exponent)));
    }
}
