package com.cubikore.astro.universe;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.dimension.DimensionKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Universe {
    public static Identifier SPACE_ID = planetId("space");
    public static Identifier MERCURY_ID = planetId("mercury");
    public static Identifier VENUS_ID = planetId("venus");
    public static Identifier EARTH_ID = planetId("earth");
    public static Identifier MARS_ID = planetId("mars");
    public static Identifier JUPITER_ID = planetId("jupiter");
    public static Identifier SATURN_ID = planetId("saturn");
    public static Identifier URANUS_ID = planetId("uranus");
    public static Identifier NEPTUNE_ID = planetId("neptune");

    private Map<RegistryKey<World>, Identifier> planetMap = new HashMap<>();

    public List<Planet> planets = new ArrayList<>();

    private static Identifier planetId(String name) {
        return Identifier.of(AstroCraft.MOD_ID, name);
    }

    public void init() {
        planetMap.put(DimensionKeys.SPACE_DIM, SPACE_ID);

        planetMap.put(DimensionKeys.VENUS_DIM, VENUS_ID);

        planetMap.put(World.OVERWORLD, EARTH_ID);
        planetMap.put(World.NETHER, EARTH_ID);
        planetMap.put(World.END, EARTH_ID);
    }

    public void add(Planet planet) {
        planets.add(planet);
    }

    public Planet getPlanet(String name) {
        for(Planet planet : planets) {
            if(planet.name.equals(name))
                return planet;
        }

        return null;
    }

    public Planet getClosestPlanet(Vector3f pos) {
        Planet closestPlanet = null;
        float closest = Float.POSITIVE_INFINITY;
        for(Planet planet : planets) {
            float dist = pos.distance(new Vector3f(planet.position[0], planet.position[1], planet.position[2]));
            if(dist < closest) {
                closestPlanet = planet;
                closest = dist;
            }
        }

        return closestPlanet;
    }

    public static Identifier getPlanetFromName(String name) {
        return switch (name) {
            case "space" -> SPACE_ID;
            case "mercury" -> MERCURY_ID;
            case "venus" -> VENUS_ID;
            case "earth" -> EARTH_ID;
            case "mars" -> MARS_ID;
            case "jupiter" -> JUPITER_ID;
            case "saturn" -> SATURN_ID;
            case "uranus" -> NEPTUNE_ID;
            case "neptune" -> URANUS_ID;
            default -> null;
        };
    }

    public Identifier getPlanetIdFromWorld(RegistryKey<World> dimensionKey) {
        return planetMap.getOrDefault(dimensionKey, null);
    }
}
