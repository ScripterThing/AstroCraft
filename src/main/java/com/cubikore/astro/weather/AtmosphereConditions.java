package com.cubikore.astro.weather;

public class AtmosphereConditions {
    public boolean hasFog = false;
    public float fogDistance = 0;
    public float lightDarkFactor = 1f;

    public float[] fogColor = new float[]{1f, 1f, 1f};
    public float[] fogDarkColor = new float[]{0.1f, 0.1f, 0.1f};

    public float[] sunOrientation = new float[]{20f, 45f};
    public float[] sunColor = new float[]{1f, 1f, 1f};
    public float sunBrightness = 1f;

    public float sunSize = 100f;
    public float sunWhiteSize = 300f;

    public boolean updateSunEachFrame = false;

    public void lerpTowards(AtmosphereConditions target, float t) {
        fogDistance += (target.fogDistance - fogDistance) * t;
        lightDarkFactor += (target.lightDarkFactor - lightDarkFactor) * t;
        sunBrightness += (target.sunBrightness - sunBrightness) * t;
        sunOrientation[0] += (target.sunOrientation[0] - sunOrientation[0]) * t;
        sunOrientation[1] += (target.sunOrientation[1] - sunOrientation[1]) * t;

        sunColor[0] += (target.sunColor[0] - sunColor[0]) * t;
        sunColor[1] += (target.sunColor[1] - sunColor[1]) * t;
        sunColor[2] += (target.sunColor[2] - sunColor[2]) * t;

        fogColor[0] += (target.fogColor[0] - fogColor[0]) * t;
        fogColor[1] += (target.fogColor[1] - fogColor[1]) * t;
        fogColor[2] += (target.fogColor[2] - fogColor[2]) * t;

        fogDarkColor[0] += (target.fogDarkColor[0] - fogDarkColor[0]) * t;
        fogDarkColor[1] += (target.fogDarkColor[1] - fogDarkColor[1]) * t;
        fogDarkColor[2] += (target.fogDarkColor[2] - fogDarkColor[2]) * t;

        sunSize += (target.sunSize - sunSize) * t;
        sunWhiteSize += (target.sunWhiteSize - sunWhiteSize) * t;
    }
}
