package com.cubikore.astro.weather;

public class PlanetWeather {
    private String type;
    private int duration;
    private int ticksPassed;
    private float intensity;

    public PlanetWeather(String type, int duration, float intensity) {
        this.type = type;
        this.duration = duration;
        this.ticksPassed = 0;
        this.intensity = intensity;
    }

    public void tick() {
        ticksPassed++;
    }

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public int getTicksPassed() {
        return ticksPassed;
    }

    public float getIntensity() {
        return intensity;
    }

    public float getTimePassed() {
        return (float)ticksPassed / 20.0f;
    }

    public boolean isClear() {
        return this.type.equals("clear");
    }

    public static PlanetWeather getClear() {
        return new PlanetWeather("clear", Integer.MAX_VALUE, 1);
    }
}
