package com.cubikore.astro.sound;

import net.minecraft.block.Blocks;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import java.util.Random;

public class VenusWindSoundInstance extends EntityTrackingSoundInstance {
    private Random random = new Random();

    private Entity trackedEntity;

    public VenusWindSoundInstance(SoundEvent sound, SoundCategory category, float volume, float pitch, Entity entity, long seed) {
        super(sound, category, volume, pitch, entity, seed);

        trackedEntity = entity;

        this.repeat = true;
        this.repeatDelay = Math.round(random.nextFloat() * 40f);
    }

    @Override
    public void tick() {
        super.tick();

        float d = (float) Math.min(Math.max((trackedEntity.getPos().y - 64f) / 6f, 0), 1);

        this.volume = d;
    }

    @Override
    public boolean isDone() {
        return trackedEntity.isRemoved();
    }
}
