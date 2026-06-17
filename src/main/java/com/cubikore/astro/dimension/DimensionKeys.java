package com.cubikore.astro.dimension;

import com.cubikore.astro.AstroCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

public class DimensionKeys {
    public static RegistryKey<World> SPACE_DIM = dimensionId("space_dim");
    public static RegistryKey<World> VENUS_DIM = dimensionId("venus_dim");

    private static RegistryKey<World> dimensionId(String name) {
        return RegistryKey.of(RegistryKeys.WORLD, Identifier.of(AstroCraft.MOD_ID, name));
    }
}
