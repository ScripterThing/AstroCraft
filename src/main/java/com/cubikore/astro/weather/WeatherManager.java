package com.cubikore.astro.weather;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.networking.payload.WeatherChangedPayload;
import com.cubikore.astro.universe.Universe;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class WeatherManager {
    private Map<Identifier, PlanetWeather> planetWeatherMap = new HashMap<>();

    private boolean dirty = false;

    public void tick(MinecraftServer server) {
        planetWeatherMap.forEach((planetId, planetWeather) -> {
            if(dirty) {
                dirty = false;

                for(ServerPlayerEntity player : PlayerLookup.all(server)) {
                    Identifier from = Universe.getPlanetIdFromWorld(player.getWorld().getRegistryKey());
                    sendWeatherState(player, false, from);
                }
            }

            if(planetWeather.getTicksPassed() >= planetWeather.getDuration() && !planetWeather.isClear()) {
                planetWeatherMap.replace(planetId, PlanetWeather.getClear());

                for(ServerPlayerEntity player : PlayerLookup.all(server)) {
                    Identifier from = Universe.getPlanetIdFromWorld(player.getWorld().getRegistryKey());
                    sendWeatherState(player, false, from);
                    System.out.println("Donesss " + player.getName().getString());
                }
            }

            planetWeather.tick();
        });
    }

    public void sendWeatherState(ServerPlayerEntity player, boolean planetChanged, Identifier fromPlanetName) {
        Identifier planetId = Universe.getPlanetIdFromWorld(player.getWorld().getRegistryKey());
        PlanetWeather weather = AstroCraft.serverGameManager.weatherManager.getWeather(planetId);

        String json = AstroCraft.gson.toJson(weather);
        ServerPlayNetworking.send(player, new WeatherChangedPayload(json, planetChanged, fromPlanetName));
    }

    public void add(Identifier planetId, PlanetWeather weather) {
        planetWeatherMap.put(planetId, weather);
    }

    public void setWeather(Identifier planetId, PlanetWeather weather) {
        planetWeatherMap.replace(planetId, weather);
        markDirty();
    }

    public PlanetWeather getWeather(Identifier planetId) {
        return planetWeatherMap.getOrDefault(planetId, PlanetWeather.getClear());
    }

    private void markDirty() {
        dirty = true;
    }
}
