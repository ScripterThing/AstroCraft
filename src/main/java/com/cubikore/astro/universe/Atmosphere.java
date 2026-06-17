package com.cubikore.astro.universe;

import org.joml.Vector3f;

public class Atmosphere {
    public float[] radius;
    public float[] fallOff;
    public float[] scatteringStrength;

    public float[] wavelengths;

    public Atmosphere(float radius, float fallOff) {
        this.radius = new float[]{radius};
        this.fallOff = new float[]{fallOff};
        this.wavelengths = new float[]{700, 530, 440};
        this.scatteringStrength = new float[]{1.0f};
    }

    public Atmosphere(float radius, float fallOff, Vector3f wavelengths) {
        this.radius = new float[]{radius};
        this.fallOff = new float[]{fallOff};
        this.wavelengths = new float[]{wavelengths.x, wavelengths.y, wavelengths.z};
        this.scatteringStrength = new float[]{1.0f};
    }
}
