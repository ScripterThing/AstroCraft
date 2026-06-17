package com.cubikore.astro.sound;

import com.cubikore.astro.AstroCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class AstroCraftSounds {
    public static SoundEvent WIND_SOUND = registerSound("wind");
    public static SoundEvent WIND_LOW = registerSound("wind_low");
    public static SoundEvent WIND_HIGH = registerSound("wind_high");
    public static SoundEvent WATER_DROP = registerSound("water_drop");

    private static SoundEvent registerSound(String name) {
        SoundEvent soundEvent = SoundEvent.of(Identifier.of(AstroCraft.MOD_ID, name));
        Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent);
        return soundEvent;
    }

    public static void registerSounds() {
        AstroCraft.LOGGER.info("Registering sounds for astrocraft");
    }
}
