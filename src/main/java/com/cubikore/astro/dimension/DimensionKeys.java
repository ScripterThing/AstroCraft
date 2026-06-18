package com.cubikore.astro.dimension;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.universe.Universe;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DimensionKeys {
    private static HashMap<Identifier, RegistryKey<World>> DIMENSION_MAP = new HashMap<>();

    public static void addDimension(Identifier planetId) {
        DIMENSION_MAP.putIfAbsent(planetId, dimensionId(planetId));
    }

    public static Identifier getPlanetId(RegistryKey<World> dimId) {
        for(Identifier id : DIMENSION_MAP.keySet()) {
            if(getDimension(id).equals(dimId))
                return id;
        }

        return null;
    }

    public static RegistryKey<World> getDimension(Identifier planetId) {
        return DIMENSION_MAP.getOrDefault(planetId, null);
    }

    private static RegistryKey<World> dimensionId(Identifier planerId) {
        Identifier dimId = Identifier.of(planerId.getNamespace(), planerId.getPath() + "_dim");
        return RegistryKey.of(RegistryKeys.WORLD, dimId);
    }

    public static boolean inSpace(Entity entity) {
        return onPlanet(entity, Universe.SPACE_ID);
    }

    public static boolean onPlanet(Entity entity, Identifier planetId) {
        return entity != null && entity.getWorld() != null && isPlanetDimension(entity.getWorld(), planetId);
    }

    public static boolean isSpace(World world) {
        return isPlanetDimension(world, Universe.SPACE_ID);
    }

    public static boolean isPlanetDimension(World world, Identifier planetId) {
        RegistryKey<World> dim = getDimension(planetId);
        return world != null && dim != null && dim.equals(world.getRegistryKey());
    }
}
