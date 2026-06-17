package com.cubikore.astro.sound;

import com.cubikore.astro.client.ClientStorage;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class VenusLowWindSoundInstance extends EntityTrackingSoundInstance {
    private Entity trackedEntity;

    public VenusLowWindSoundInstance(SoundEvent sound, SoundCategory category, float volume, float pitch, Entity entity, long seed) {
        super(sound, category, volume, pitch, entity, seed);

        trackedEntity = entity;

        this.repeat = true;
        this.repeatDelay = 0;
    }

    @Override
    public void tick() {
        super.tick();

        float d = (float) Math.min(Math.max((trackedEntity.getPos().y - 64f) / 6f, 0), 1);

        this.volume = 1.0f - ClientStorage.windStrength;
        this.volume *= d;
    }

    @Override
    public boolean isDone() {
        return trackedEntity.isRemoved();
    }
}
