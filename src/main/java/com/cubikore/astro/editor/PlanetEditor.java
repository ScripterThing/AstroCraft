package com.cubikore.astro.editor;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.universe.Atmosphere;
import com.cubikore.astro.universe.Planet;
import foundry.veil.api.client.editor.SingleWindowInspector;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class PlanetEditor extends SingleWindowInspector {
    private List<Planet> pendingRemove = new ArrayList<>();

    @Override
    protected void renderComponents() {
        AstroCraft.universe.planets.removeAll(pendingRemove);
        pendingRemove.clear();

        if(ImGui.button("Add Planet")) {
            if(AstroCraft.universe.planets.size() < 32) {
                MinecraftClient client = MinecraftClient.getInstance();
                Vec3d pos = client.player.getPos();
                AstroCraft.universe.add(new Planet(new Vector3f((float)pos.x, (float)pos.y, (float)pos.z), 1.0f, new Vector3f(1.0f), new Atmosphere(2.0f, 3.19f), "planet"));
            }
        }

        int i = 0;
        for(Planet planet : AstroCraft.universe.planets) {
            ImGui.pushID(i);

            ImGui.text(planet.name);
            ImGui.indent();

            ImGui.dragFloat3("Position", planet.position, 0.1f);
            ImGui.dragFloat("Radius", planet.radius, 0.1f);

            ImGui.colorPicker3("Color", planet.color);

            ImGui.pushID("atm" + i);
            ImGui.labelText("", "Atmosphere");
            ImGui.indent();

            ImGui.dragFloat("Radius", planet.atmosphere.radius, 0.1f);
            ImGui.dragFloat("Density Fall Off", planet.atmosphere.fallOff, 0.1f);

            ImGui.dragFloat3("Wavelengths", planet.atmosphere.wavelengths);
            ImGui.dragFloat("Scattering Strength", planet.atmosphere.scatteringStrength, 0.1f);

            ImGui.unindent();
            ImGui.popID();

            if(ImGui.button("Remove")) {
                pendingRemove.add(planet);
            }

            ImGui.unindent();
            ImGui.separator();

            ImGui.popID();
            i++;
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Planet Editor");
    }
}
