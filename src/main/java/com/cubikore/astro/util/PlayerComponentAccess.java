package com.cubikore.astro.util;

import com.cubikore.astro.client.light.SpotLight;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;

public interface PlayerComponentAccess {
    boolean disconnected = false;

    LightRenderHandle<PointLightData> getPointLightHandle();
    void setPointLightHandle(LightRenderHandle<PointLightData> handle);

    SpotLight getPlayerSpotLight();
    void setPlayerSpotLight(SpotLight spotLight);

    boolean isFlashlightOn();
    void setFlashlightOn(boolean bl);

    void removeLights();
}
