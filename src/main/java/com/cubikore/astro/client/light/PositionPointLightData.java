package com.cubikore.astro.client.light;

import foundry.veil.api.client.render.light.data.LightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import org.joml.Vector3d;

public class PositionPointLightData extends PointLightData {
    private Vector3d truePosition;

    public PositionPointLightData(Vector3d truePosition) {
        super();

        this.truePosition = truePosition;
    }

    public void setTruePosition(Vector3d position) {
        this.truePosition.set(position);
    }

    public Vector3d getTruePosition() {
        return this.truePosition;
    }
}
