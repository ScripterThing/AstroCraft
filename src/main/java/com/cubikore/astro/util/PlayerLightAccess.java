package com.cubikore.astro.util;

import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;

public interface PlayerLightAccess {
    boolean disconnected = false;

    LightRenderHandle<PointLightData> getLightHandle();

    void setLightHandle(LightRenderHandle<PointLightData> handle);

    void removeLight();
}
