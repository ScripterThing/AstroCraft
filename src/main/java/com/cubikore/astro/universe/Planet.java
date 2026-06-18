package com.cubikore.astro.universe;

import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class Planet {
    public float[] position, color;
    public float[] radius;

    public Identifier planetId;

    public Atmosphere atmosphere;

    public Planet(Vector3f position, float radius, Vector3f color, Atmosphere atmosphere, Identifier planetId) {
        this.position = new float[]{position.x, position.y, position.z};
        this.color = new float[]{color.x, color.y, color.z};
        this.radius = new float[]{radius};
        this.atmosphere = atmosphere;
        this.planetId = planetId;
    }
}
