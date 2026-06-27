package com.cubikore.astro.client.editor;

import com.cubikore.astro.universe.Planet;
import com.cubikore.astro.universe.Universe;
import foundry.veil.api.client.editor.SingleWindowInspector;
import imgui.ImGui;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PlanetEditor extends SingleWindowInspector {
    private List<Planet> pendingRemove = new ArrayList<>();

    @Override
    protected void renderComponents() {
        Universe.planets.removeAll(pendingRemove);
        pendingRemove.clear();

//        if(ImGui.button("Add Planet")) {
//            if(Universe.planets.size() < 32) {
//                MinecraftClient client = MinecraftClient.getInstance();
//                Vec3d pos = client.player.getPos();
//                Universe.addPlanet(new Planet(new Vector3f((float)pos.x, (float)pos.y, (float)pos.z), 1.0f, new Vector3f(1.0f), new Atmosphere(2.0f, 3.19f), "planet"));
//            }
//        }

        int i = 0;
        for(Planet planet : Universe.planets) {
            ImGui.pushID(i);

            ImGui.text(planet.planetId.toString());
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
