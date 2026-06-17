package com.cubikore.astro.weather.planet;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.particle.emitters.VenusAcidRainParticleEmitter;
import com.cubikore.astro.particle.emitters.VenusAcidRainSplashParticleEmitter;
import com.cubikore.astro.particle.emitters.VenusAtmosphereParticleEmitter;
import com.cubikore.astro.sound.AstroCraftSounds;
import com.cubikore.astro.sound.VenusHighWindSoundInstance;
import com.cubikore.astro.sound.VenusLowWindSoundInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2f;

public class VenusWeather extends ClientWeather {
    public VenusWeather() {
        super("venus");
        this.renderClouds = false;
        this.clearAtmosphereConditions.fogDistance = 500f;
        this.clearAtmosphereConditions.lightDarkFactor = 1f;
        this.clearAtmosphereConditions.hasFog = true;
        this.clearAtmosphereConditions.updateSunEachFrame = true;
        this.targetAtmosphereConditions.updateSunEachFrame = true;
        this.currentAtmosphereConditions.updateSunEachFrame = true;

        this.supportedTypes.add("acid_rain");

        this.weatherTransitionSpeed = 0.15f;

        this.setSunBrightness(2f);
        this.setSunOrientation(new float[]{45f, -45f});
        this.setSunColor(new float[]{0.988f, 0.921f, 0.662f});
        this.setFogColor(new float[]{0.85f, 0.63f, 0.23f});
        this.setFogDarkColor(new float[]{0.283f, 0.21f, 0.076f});
        this.setSunSize(100f, 300f);

        this.init();
    }

    @Override
    public void changed(String type, boolean none) {
        super.changed(type, none);

        MinecraftClient client = MinecraftClient.getInstance();

        if (type.equals("acid_rain")) {
            this.targetAtmosphereConditions.fogDistance = 10f;
            this.targetAtmosphereConditions.lightDarkFactor = 0f;

            this.targetAtmosphereConditions.sunBrightness = 0.3f;

            this.targetAtmosphereConditions.sunSize = 900f;
            this.targetAtmosphereConditions.sunWhiteSize = 1000f;

            AstroCraftClient.particleManager.stopEmitterType(VenusAtmosphereParticleEmitter.class);
            AstroCraftClient.particleManager.addEmitter(new VenusAcidRainParticleEmitter(new Vector2f(0.025f), 500));
        }
        else if (type.equals("clear")) {
            this.targetAtmosphereConditions.sunBrightness = 2f;
            AstroCraftClient.particleManager.stopEmitterType(VenusAcidRainParticleEmitter.class);

            AstroCraftClient.particleManager.addEmitter(new VenusAtmosphereParticleEmitter(new Vector2f(0.0625f, 0.025f),0.04f, 255, 203, 33, 150));
            AstroCraftClient.particleManager.addEmitter(new VenusAtmosphereParticleEmitter(new Vector2f(0.04375f, 0.0175F),  0.08f, 46, 46, 46, 225));

            if(AstroCraftClient.venusLowWindSoundInstance == null || AstroCraftClient.venusHighWindSoundInstance == null) {
                AstroCraftClient.venusLowWindSoundInstance = new VenusLowWindSoundInstance(AstroCraftSounds.WIND_LOW, SoundCategory.AMBIENT, 1, 1, client.player, 1);
                AstroCraftClient.venusHighWindSoundInstance = new VenusHighWindSoundInstance(AstroCraftSounds.WIND_HIGH, SoundCategory.AMBIENT, 1, 1, client.player, 1);
            }

            client.getSoundManager().play(AstroCraftClient.venusLowWindSoundInstance);
            client.getSoundManager().play(AstroCraftClient.venusHighWindSoundInstance);
        }
    }

    @Override
    public void cleanUp() {
        super.cleanUp();

        AstroCraftClient.particleManager.removeEmitterType(VenusAtmosphereParticleEmitter.class);
        AstroCraftClient.particleManager.removeEmitterType(VenusAcidRainSplashParticleEmitter.class);
    }

    @Override
    public void tickFrame() {
        super.tickFrame();

        if(type.equals("clear")) {
            MinecraftClient client = MinecraftClient.getInstance();
            Vec3d playerPos = client.player.getPos();
            this.targetAtmosphereConditions.lightDarkFactor = (float) Math.min(Math.max((playerPos.y - 64.0) / 6.0, 0), 1);
        }
    }
}
