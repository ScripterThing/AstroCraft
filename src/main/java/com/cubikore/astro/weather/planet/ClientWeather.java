package com.cubikore.astro.weather.planet;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.weather.AtmosphereConditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientWeather {
    private long lastTime = System.nanoTime();

    public AtmosphereConditions currentAtmosphereConditions = new AtmosphereConditions();
    protected AtmosphereConditions targetAtmosphereConditions = new AtmosphereConditions();
    protected AtmosphereConditions clearAtmosphereConditions = new AtmosphereConditions();

    public String type = "clear";

    public List<String> supportedTypes = new ArrayList<>();

    protected float weatherTransitionSpeed = 0.05f;

    protected boolean renderClouds = true;

    public final String weatherName;

    public ClientWeather(String weatherName) {
        this.weatherName = weatherName;
        supportedTypes.add("clear");
    }

    public void changed(String type, boolean none) {
        this.type = type;

        if(type.equals("clear")) {
            if(!none) {
                this.targetAtmosphereConditions.fogDistance = clearAtmosphereConditions.fogDistance;
                this.targetAtmosphereConditions.lightDarkFactor = clearAtmosphereConditions.lightDarkFactor;

                this.targetAtmosphereConditions.fogColor[0] = clearAtmosphereConditions.fogColor[0];
                this.targetAtmosphereConditions.fogColor[1] = clearAtmosphereConditions.fogColor[1];
                this.targetAtmosphereConditions.fogColor[2] = clearAtmosphereConditions.fogColor[2];

                this.targetAtmosphereConditions.sunColor[0] = clearAtmosphereConditions.sunColor[0];
                this.targetAtmosphereConditions.sunColor[1] = clearAtmosphereConditions.sunColor[1];
                this.targetAtmosphereConditions.sunColor[2] = clearAtmosphereConditions.sunColor[2];

                this.targetAtmosphereConditions.fogDarkColor[0] = clearAtmosphereConditions.fogDarkColor[0];
                this.targetAtmosphereConditions.fogDarkColor[1] = clearAtmosphereConditions.fogDarkColor[1];
                this.targetAtmosphereConditions.fogDarkColor[2] = clearAtmosphereConditions.fogDarkColor[2];

                this.targetAtmosphereConditions.sunSize = clearAtmosphereConditions.sunSize;
                this.targetAtmosphereConditions.sunWhiteSize = clearAtmosphereConditions.sunWhiteSize;

                this.targetAtmosphereConditions.sunBrightness = clearAtmosphereConditions.sunBrightness;
            }
        }

        if (none) {
            this.currentAtmosphereConditions.hasFog = clearAtmosphereConditions.hasFog;
            this.targetAtmosphereConditions.fogDistance = clearAtmosphereConditions.fogDistance;
            this.targetAtmosphereConditions.lightDarkFactor = clearAtmosphereConditions.lightDarkFactor;
            this.currentAtmosphereConditions.fogDistance = clearAtmosphereConditions.fogDistance;
            this.currentAtmosphereConditions.lightDarkFactor = clearAtmosphereConditions.lightDarkFactor;
        }

        this.lastTime = System.nanoTime();
    }

    public void cleanUp() {
        this.currentAtmosphereConditions.hasFog = clearAtmosphereConditions.hasFog;
        this.targetAtmosphereConditions.fogDistance = clearAtmosphereConditions.fogDistance;
        this.targetAtmosphereConditions.lightDarkFactor = clearAtmosphereConditions.lightDarkFactor;
        this.currentAtmosphereConditions.fogDistance = clearAtmosphereConditions.fogDistance;
        this.currentAtmosphereConditions.lightDarkFactor = clearAtmosphereConditions.lightDarkFactor;
    }

    public void init() {
        this.currentAtmosphereConditions.hasFog = this.clearAtmosphereConditions.hasFog;
        this.currentAtmosphereConditions.fogDistance = this.clearAtmosphereConditions.fogDistance;
        this.currentAtmosphereConditions.lightDarkFactor = this.clearAtmosphereConditions.lightDarkFactor;
    }

    public void tickFrame() {
        currentAtmosphereConditions.lerpTowards(targetAtmosphereConditions, this.weatherTransitionSpeed * getDeltaTime());
        lastTime = System.nanoTime();

        if(currentAtmosphereConditions.updateSunEachFrame) {
            ClientStorage.sunBrightness[0] = currentAtmosphereConditions.sunBrightness;

            ClientStorage.sunOrientation[0] = currentAtmosphereConditions.sunOrientation[0];
            ClientStorage.sunOrientation[1] = currentAtmosphereConditions.sunOrientation[1];

            ClientStorage.sunColor[0] = currentAtmosphereConditions.sunColor[0];
            ClientStorage.sunColor[1] = currentAtmosphereConditions.sunColor[1];
            ClientStorage.sunColor[2] = currentAtmosphereConditions.sunColor[2];
        }
    }

    protected void setSunBrightness(float brightness) {
        this.currentAtmosphereConditions.sunBrightness = brightness;
        this.targetAtmosphereConditions.sunBrightness = brightness;
        this.clearAtmosphereConditions.sunBrightness = brightness;
    }

    protected void setSunSize(float size, float whiteSize) {
        this.currentAtmosphereConditions.sunSize = size;
        this.targetAtmosphereConditions.sunSize = size;
        this.clearAtmosphereConditions.sunSize = size;

        this.currentAtmosphereConditions.sunWhiteSize = whiteSize;
        this.targetAtmosphereConditions.sunWhiteSize = whiteSize;
        this.clearAtmosphereConditions.sunWhiteSize = whiteSize;
    }

    protected void setFogColor(float[] color) {
        this.clearAtmosphereConditions.fogColor[0] = color[0];
        this.clearAtmosphereConditions.fogColor[1] = color[1];
        this.clearAtmosphereConditions.fogColor[2] = color[2];

        this.targetAtmosphereConditions.fogColor[0] = color[0];
        this.targetAtmosphereConditions.fogColor[1] = color[1];
        this.targetAtmosphereConditions.fogColor[2] = color[2];

        this.currentAtmosphereConditions.fogColor[0] = color[0];
        this.currentAtmosphereConditions.fogColor[1] = color[1];
        this.currentAtmosphereConditions.fogColor[2] = color[2];
    }

    protected void setFogDarkColor(float[] color) {
        this.clearAtmosphereConditions.fogDarkColor[0] = color[0];
        this.clearAtmosphereConditions.fogDarkColor[1] = color[1];
        this.clearAtmosphereConditions.fogDarkColor[2] = color[2];

        this.targetAtmosphereConditions.fogDarkColor[0] = color[0];
        this.targetAtmosphereConditions.fogDarkColor[1] = color[1];
        this.targetAtmosphereConditions.fogDarkColor[2] = color[2];

        this.currentAtmosphereConditions.fogDarkColor[0] = color[0];
        this.currentAtmosphereConditions.fogDarkColor[1] = color[1];
        this.currentAtmosphereConditions.fogDarkColor[2] = color[2];
    }

    protected void setSunOrientation(float[] orientation) {
        this.currentAtmosphereConditions.sunOrientation[0] = orientation[0];
        this.currentAtmosphereConditions.sunOrientation[1] = orientation[1];

        this.targetAtmosphereConditions.sunOrientation[0] = orientation[0];
        this.targetAtmosphereConditions.sunOrientation[1] = orientation[1];

        this.clearAtmosphereConditions.sunOrientation[0] = orientation[0];
        this.clearAtmosphereConditions.sunOrientation[1] = orientation[1];
    }

    protected void setSunColor(float[] color) {
        this.currentAtmosphereConditions.sunColor[0] = color[0];
        this.currentAtmosphereConditions.sunColor[1] = color[1];
        this.currentAtmosphereConditions.sunColor[2] = color[2];

        this.targetAtmosphereConditions.sunColor[0] = color[0];
        this.targetAtmosphereConditions.sunColor[1] = color[1];
        this.targetAtmosphereConditions.sunColor[2] = color[2];

        this.clearAtmosphereConditions.sunColor[0] = color[0];
        this.clearAtmosphereConditions.sunColor[1] = color[1];
        this.clearAtmosphereConditions.sunColor[2] = color[2];
    }

    public boolean canRenderClouds() {
        return this.renderClouds;
    }

    protected float getDeltaTime() {
        return (System.nanoTime() - lastTime) / 1_000_000_000.0f;
    }
}
