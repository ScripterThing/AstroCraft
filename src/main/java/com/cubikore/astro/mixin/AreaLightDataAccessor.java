package com.cubikore.astro.mixin;

import foundry.veil.api.client.render.light.data.AreaLightData;
import org.joml.Matrix4d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AreaLightData.class)
public interface AreaLightDataAccessor {
    @Accessor("matrix")
    Matrix4d getMatrix();
}
