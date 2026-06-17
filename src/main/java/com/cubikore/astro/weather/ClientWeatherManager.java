package com.cubikore.astro.weather;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.weather.planet.ClientWeather;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientWeatherManager {
    private Map<Identifier, ClientWeather> clientWeatherMap = new HashMap<>();

    public List<String> supportedPlanets = new ArrayList<>();
    public List<String> clientWeatherTypes = new ArrayList<>();

    public void add(Identifier planetId, ClientWeather weather) {
        for(String type : weather.supportedTypes) {
            if(!clientWeatherTypes.contains(type))
                clientWeatherTypes.add(type);
        }

        supportedPlanets.add(planetId.getPath());

        clientWeatherMap.put(planetId, weather);
    }

    public void changed(Identifier planetId, String type, boolean planetChanged) {
        ClientWeather cw = clientWeatherMap.get(planetId);
        if(cw != null){
            boolean gda = noneWeather(planetId);
            if(planetChanged) {
                cw.cleanUp();
            }
            cw.changed(type, gda);
        }
    }

    private boolean noneWeather(Identifier planetId) {
        return planetId.getPath().equals("space")
                || planetId.getPath().equals("earth");
    }

    public ClientWeather get(Identifier planerId) {
        return clientWeatherMap.getOrDefault(planerId, null);
    }

    public ClientWeather get(RegistryKey<World> dimKey) {
        ClientWeather result = get(AstroCraft.universe.getPlanetIdFromWorld(dimKey));

        if(result != null)
            return result;

        return new ClientWeather("null");
    }
}
