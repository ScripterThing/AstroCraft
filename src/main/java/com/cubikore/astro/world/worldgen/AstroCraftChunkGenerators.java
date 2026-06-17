package com.cubikore.astro.world.worldgen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AstroCraftChunkGenerators {
    public static void registerChunkGenerators() {
        Registry.register(Registries.CHUNK_GENERATOR, Identifier.of("astrocraft", "venus"), VenusChunkGenerator.CODEC);
    }
}
