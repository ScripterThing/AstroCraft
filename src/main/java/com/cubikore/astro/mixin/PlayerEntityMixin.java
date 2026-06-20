package com.cubikore.astro.mixin;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.light.SpotLight;
import com.cubikore.astro.util.PlayerComponentAccess;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerComponentAccess {
    @Unique
    private LightRenderHandle<PointLightData> pointLightHandle;

    @Unique
    private SpotLight spotLight;

    @Unique
    private boolean flashlightOn = false;

    @Override
    public LightRenderHandle<PointLightData> getPointLightHandle() {
        return pointLightHandle;
    }

    @Override
    public void setPointLightHandle(LightRenderHandle<PointLightData> handle) {
        pointLightHandle = handle;
    }

    @Override
    public SpotLight getPlayerSpotLight() {
        return spotLight;
    }

    @Override
    public void setPlayerSpotLight(SpotLight spotLight) {
        this.spotLight = spotLight;
    }

    @Override
    public boolean isFlashlightOn() {
        return flashlightOn;
    }

    @Override
    public void setFlashlightOn(boolean bl) {
        flashlightOn = bl;
    }

    @Override
    public void removeLights() {
        if(pointLightHandle != null && pointLightHandle.isValid() && spotLight != null) {
            pointLightHandle.free();
            pointLightHandle = null;
            spotLight.remove();
            spotLight = null;
            AstroCraftClient.renderer.markLightsDirty();
        }
    }
}
