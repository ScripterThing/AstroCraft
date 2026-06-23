package com.cubikore.astro.math;

import org.joml.Math;
import org.joml.Vector2f;

public class VectorMath {
    public static Vector2f orbit(float angleDegrees, float radius) {
        float radians = Math.toRadians(angleDegrees);

        float x = Math.cos(radians) * radius;
        float y = Math.sin(radians) * radius;

        return new Vector2f(x, y);
    }

    public static float[] fma(float a, float ax, float ay, float az, float bx, float by, float bz) {
        float x = Math.fma(a, bx, ax);
        float y = Math.fma(a, by, ay);
        float z = Math.fma(a, bz, az);
        return new float[]{x, y, z};
    }

    public static float[] mul(float ax, float ay, float az, float scalar) {
        float x = ax * scalar;
        float y = ay * scalar;
        float z = az * scalar;
        return new float[]{x, y, z};
    }

    public static float[] mul(float ax, float ay, float az, float bx, float by, float bz) {
        float x = ax * bx;
        float y = ay * by;
        float z = az * bz;
        return new float[]{x, y, z};
    }

    public static float[] add(float ax, float ay, float az, float scalar) {
        float x = ax + scalar;
        float y = ay + scalar;
        float z = az + scalar;
        return new float[]{x, y, z};
    }

    public static float[] add(float ax, float ay, float az, float bx, float by, float bz) {
        float x = ax + bx;
        float y = ay + by;
        float z = az + bz;
        return new float[]{x, y, z};
    }
    public static float[] sub(float ax, float ay, float az, float scalar) {
        float x = ax - scalar;
        float y = ay - scalar;
        float z = az - scalar;
        return new float[]{x, y, z};
    }

    public static float[] sub(float ax, float ay, float az, float bx, float by, float bz) {
        float x = ax - bx;
        float y = ay - by;
        float z = az - bz;
        return new float[]{x, y, z};
    }

    public static float distance(float ax, float ay, float bx, float by) {
        float xs = bx - ax;
        float ys = by - ay;
        return Math.sqrt(xs * xs + ys * ys);
    }

    public static float distance(float ax, float ay, float az, float bx, float by, float bz) {
        float dx = ax - bx;
        float dy = ay - by;
        float dz = az - bz;
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, dz * dz)));
    }

    public static float[] normalize(float x, float y, float z) {
        float scalar = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, z * z)));
        float nx = x * scalar;
        float ny = y * scalar;
        float nz = z * scalar;
        return new float[]{nx, ny, nz};
    }
}
