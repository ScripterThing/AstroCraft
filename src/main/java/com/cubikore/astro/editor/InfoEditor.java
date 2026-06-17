package com.cubikore.astro.editor;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.weather.planet.ClientWeather;
import foundry.veil.api.client.editor.SingleWindowInspector;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class InfoEditor extends SingleWindowInspector {
    @Override
    protected void renderComponents() {
        ImGui.text("World Offset:");
        ImGui.indent();
        ImGui.text("x: " + ClientStorage.renderedWorldOffset.x);
        ImGui.text("y: " + ClientStorage.renderedWorldOffset.y);
        ImGui.text("z: " + ClientStorage.renderedWorldOffset.z);
        ImGui.text("yaw: " + ClientStorage.renderedWorldOffset.w);
        ImGui.unindent();

        ClientWeather weather = AstroCraftClient.weatherManager.get(MinecraftClient.getInstance().world.getRegistryKey());

        ImGui.text("Atmosphere Conditions");
        ImGui.indent();
        ImGui.text("hasFog: " + weather.currentAtmosphereConditions.hasFog);
        ImGui.text("fogDistance: " + weather.currentAtmosphereConditions.fogDistance);
        ImGui.text("lightDarkFactor: " + weather.currentAtmosphereConditions.lightDarkFactor);
        ImGui.unindent();

        ImGui.dragFloat3("World Render Offset", ClientStorage.terrainOffset);

        ImGui.text("Lighting");
        ImGui.indent();
        ImGui.dragFloat2("Sun Orientation", ClientStorage.sunOrientation);
        ImGui.colorPicker3("Sun Color", ClientStorage.sunColor);
        ImGui.text("Brightness: " + ClientStorage.sunBrightness[0]);
        ImGui.unindent();
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Info");
    }
}
