package com.cubikore.astro.block.entity;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.block.AstroCraftBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AstroCraftBlockEntities {
    public static final BlockEntityType<BrightLightBlockEntity> BRIGHT_LIGHT_BLOCK_ENTITY =
            register("bright_light", BrightLightBlockEntity::new, AstroCraftBlocks.BRIGHT_LIGHT_BLOCK);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of(AstroCraft.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
    public static void registerBlockEntities() {
        AstroCraft.LOGGER.info("Registering Block Entities for astrocraft");
    }
}
