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
    public static Identifier VENUS_ID = planetId("venus");

    public static Identifier SUN_ID = planetId("sun");

    public static Identifier MERCURY_ID = planetId("mercury");
    public static Identifier EARTH_ID = planetId("earth");
    public static Identifier MARS_ID = planetId("mars");
    public static Identifier JUPITER_ID = planetId("jupiter");
    public static Identifier SATURN_ID = planetId("saturn");
    public static Identifier URANUS_ID = planetId("uranus");
    public static Identifier NEPTUNE_ID = planetId("neptune");

    public static List<Planet> planets = new ArrayList<>();

    private static Identifier planetId(String name) {
        return Identifier.of(AstroCraft.MOD_ID, name);
    }

    public static void init() {
        AstroCraft.LOGGER.info("Initializing universe for astrocraft");
        DimensionKeys.addDimension(SPACE_ID);
    }

    public static void addPlanet(Planet planet) {
        planets.add(planet);

        if(!planet.planetId.equals(EARTH_ID)) {
            DimensionKeys.addDimension(planet.planetId);
        }
    }

    public static Planet getPlanet(Identifier id) {
        for(Planet planet : planets) {
            if(planet.planetId.equals(id))
                return planet;
        }

        return null;
    }

    public static Planet getClosestPlanet(Vector3f pos) {
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

    public static Identifier getPlanetIdFromWorld(RegistryKey<World> dimensionKey) {
        return dimensionKey.equals(World.OVERWORLD)
                || dimensionKey.equals(World.NETHER)
                || dimensionKey.equals(World.END)
                ? EARTH_ID : DimensionKeys.getPlanetId(dimensionKey);
    }
}
