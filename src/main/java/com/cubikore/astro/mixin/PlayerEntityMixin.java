package com.cubikore.astro.mixin;

import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.util.PlayerLightAccess;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerLightAccess {
    @Unique
    private LightRenderHandle<PointLightData> pointLightHandle;

    @Override
    public LightRenderHandle<PointLightData> getLightHandle() {
        return pointLightHandle;
    }

    @Override
    public void setLightHandle(LightRenderHandle<PointLightData> handle) {
        pointLightHandle = handle;
    }

    @Override
    public void removeLight() {
        if(pointLightHandle != null && pointLightHandle.isValid()) {
            pointLightHandle.free();
            pointLightHandle = null;
            ClientStorage.shaderLightsDirty = true;
        }
    }
}
