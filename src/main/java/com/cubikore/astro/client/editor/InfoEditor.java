package com.cubikore.astro.client.editor;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.client.light.SpotLight;
import com.cubikore.astro.weather.planet.ClientWeather;
import foundry.veil.api.client.editor.SingleWindowInspector;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class InfoEditor extends SingleWindowInspector {
    @Override
    protected void renderComponents() {
        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;
        ClientPlayerEntity player = client.player;

        if(ImGui.checkbox("Render Effects", ClientStorage.renderEffects)) {
            ClientStorage.renderEffects = !ClientStorage.renderEffects;
        }

        if(world != null && player != null) {
            ImGui.text("Spot Lights");
            ImGui.indent();

            if(ImGui.button("Add Spotlight")) {
                SpotLight light = new SpotLight(new Vector3f(0), new Vector2f(0), 3f, 10f, 1.0f);

                light.matchToView(client.gameRenderer.getCamera());

                AstroCraftClient.renderer.addLight(light);
            }

            for(int i = 0; i < AstroCraftClient.renderer.spotLights.size(); i++) {
                SpotLight spotLight = AstroCraftClient.renderer.spotLights.get(i);

                ImGui.pushID("spotLight" + i);

                ImGui.text(String.valueOf(i));
                ImGui.indent();

                if(ImGui.button("Match to view")) {
                    spotLight.matchToView(client.gameRenderer.getCamera());
                }

                Vector3f pos = spotLight.getPosition();
                Vector2f rot = spotLight.getRotation();

                ImGui.text("Position X: " + pos.x + " Y: " + pos.y + " Z: " + pos.z);
                ImGui.text("Rotation Pitch: " + rot.x + " Yaw: " + rot.y);

                float[] radiusP = new float[]{spotLight.getRadius()};
                float[] distanceP = new float[]{spotLight.getDistance()};
                float[] brightnessP = new float[]{spotLight.getBrightness()};

                if(ImGui.dragFloat("Radius", radiusP)) {
                    spotLight.setRadius(radiusP[0]);
                }

                if(ImGui.dragFloat("Distance", distanceP)) {
                    spotLight.setDistance(distanceP[0]);
                }

                if(ImGui.dragFloat("Brightness", brightnessP)) {
                    spotLight.setBrightness(brightnessP[0]);
                }

                ImGui.unindent();

                ImGui.popID();
            }
            ImGui.unindent();

            ImGui.text("World Offset:");
            ImGui.indent();
            ImGui.text("x: " + ClientStorage.renderedWorldOffset.x);
            ImGui.text("y: " + ClientStorage.renderedWorldOffset.y);
            ImGui.text("z: " + ClientStorage.renderedWorldOffset.z);
            ImGui.text("yaw: " + ClientStorage.renderedWorldOffset.w);
            ImGui.unindent();

            ClientWeather weather = AstroCraftClient.clientGameManager.weatherManager.get(MinecraftClient.getInstance().world.getRegistryKey());

            if(weather != null) {
                ImGui.text("Atmosphere Conditions");
                ImGui.indent();
                ImGui.text("hasFog: " + weather.currentAtmosphereConditions.hasFog);
                ImGui.text("fogDistance: " + weather.currentAtmosphereConditions.fogDistance);
                ImGui.text("lightDarkFactor: " + weather.currentAtmosphereConditions.lightDarkFactor);
                ImGui.text("fogColor: " + weather.currentAtmosphereConditions.fogColor[0] + ", " + weather.currentAtmosphereConditions.fogColor[1] + ", " + weather.currentAtmosphereConditions.fogColor[2]);
                ImGui.text("fogDarkColor: " + weather.currentAtmosphereConditions.fogDarkColor[0] + ", " + weather.currentAtmosphereConditions.fogDarkColor[1] + ", " + weather.currentAtmosphereConditions.fogDarkColor[2]);
                ImGui.unindent();
            }

            ImGui.dragFloat3("World Render Offset", ClientStorage.terrainOffset);

            ImGui.text("Lighting");
            ImGui.indent();
            ImGui.dragFloat2("Sun Orientation", ClientStorage.sunOrientation);
            ImGui.colorPicker3("Sun Color", ClientStorage.sunColor);
            ImGui.text("Brightness: " + ClientStorage.sunBrightness[0]);
            ImGui.unindent();
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Info");
    }
}
