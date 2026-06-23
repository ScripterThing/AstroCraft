package com.cubikore.astro.weather.planet;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.game.client.AstroCraftClientGameManager;
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

public class VenusClientWeather extends ClientWeather {
    public VenusClientWeather() {
        super("venus");
        this.renderClouds = false;
        this.clearAtmosphereConditions.fogDistance = 150f;
        this.clearAtmosphereConditions.lightDarkFactor = 1f;
        this.clearAtmosphereConditions.hasFog = true;
        this.clearAtmosphereConditions.updateSunEachFrame = true;
        this.targetAtmosphereConditions.updateSunEachFrame = true;
        this.currentAtmosphereConditions.updateSunEachFrame = true;

        this.supportedTypes.add("acid_rain");

        this.weatherTransitionSpeed = 0.15f;

        this.setSunBrightness(4f);
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

        AstroCraftClientGameManager gameManager = AstroCraftClient.clientGameManager;

        if (type.equals("acid_rain")) {
            this.targetAtmosphereConditions.fogDistance = 20f;
            this.targetAtmosphereConditions.lightDarkFactor = 0f;

            this.targetAtmosphereConditions.sunBrightness = 1f;

            this.targetAtmosphereConditions.sunSize = 900f;
            this.targetAtmosphereConditions.sunWhiteSize = 1000f;

            AstroCraftClient.clientGameManager.particleManager.stopEmitterType(VenusAtmosphereParticleEmitter.class);
            AstroCraftClient.clientGameManager.particleManager.addEmitter(new VenusAcidRainParticleEmitter(new Vector2f(0.025f), 1000));
        }
        else if (type.equals("clear")) {
            gameManager.particleManager.stopEmitterType(VenusAcidRainParticleEmitter.class);

            gameManager.particleManager.addEmitter(new VenusAtmosphereParticleEmitter(new Vector2f(0.0625f, 0.025f),0.04f, 255, 203, 33, 150));
            gameManager.particleManager.addEmitter(new VenusAtmosphereParticleEmitter(new Vector2f(0.04375f, 0.0175F),  0.08f, 46, 46, 46, 225));

            if(gameManager.venusLowWindSoundInstance == null || gameManager.venusHighWindSoundInstance == null) {
                gameManager.venusLowWindSoundInstance = new VenusLowWindSoundInstance(AstroCraftSounds.WIND_LOW, SoundCategory.AMBIENT, 1, 1, client.player, 1);
                gameManager.venusHighWindSoundInstance = new VenusHighWindSoundInstance(AstroCraftSounds.WIND_HIGH, SoundCategory.AMBIENT, 1, 1, client.player, 1);
            }

            client.getSoundManager().play(gameManager.venusLowWindSoundInstance);
            client.getSoundManager().play(gameManager.venusHighWindSoundInstance);
        }
    }

    @Override
    public void cleanUp() {
        super.cleanUp();

        AstroCraftClientGameManager gameManager = AstroCraftClient.clientGameManager;

        gameManager.particleManager.removeEmitterType(VenusAtmosphereParticleEmitter.class);
        gameManager.particleManager.removeEmitterType(VenusAcidRainSplashParticleEmitter.class);
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
