package com.cubikore.astro.universe;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Planet {
    public float[] position, color;
    public float[] radius;

    public Identifier planetId;

    public Atmosphere atmosphere;

    public List<Text> attributes = new ArrayList<>();

    public Planet(Vector3f position, float radius, Vector3f color, Atmosphere atmosphere, Identifier planetId) {
        this.position = new float[]{position.x, position.y, position.z};
        this.color = new float[]{color.x, color.y, color.z};
        this.radius = new float[]{radius};
        this.atmosphere = atmosphere;
        this.planetId = planetId;
    }

    public void addAttribute(Text attribute) {
        this.attributes.add(attribute);
    }

    public void addAttributes(Text... attributes) {
        this.attributes.addAll(List.of(attributes));
    }
}
