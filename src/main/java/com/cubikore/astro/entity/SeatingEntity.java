package com.cubikore.astro.entity;

import com.cubikore.astro.server.ServerStorage;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SeatingEntity extends DisplayEntity.TextDisplayEntity {
    public List<String> tags = new ArrayList<>();

    public SeatingEntity(World world) {
        super(EntityType.TEXT_DISPLAY, world);
    }

    @Override
    public void tick() {
        if(this.getFirstPassenger() == null) {
            ServerStorage.seatingEntities.remove(this.getBlockPos());

            this.discard();
        }

        super.tick();
    }
}
