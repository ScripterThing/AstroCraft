package com.cubikore.astro.datagen;

import com.cubikore.astro.block.AstroCraftBlocks;
import com.cubikore.astro.item.AstroCraftItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class AstroCraftModelProvider extends FabricModelProvider {
    public AstroCraftModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        //generator.registerSimpleCubeAll(AstroCraftBlocks.PLANKS);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(AstroCraftItems.SPACE_SUIT_HELMET, Models.GENERATED);
        generator.register(AstroCraftItems.SPACE_SUIT_CHESTPLATE, Models.GENERATED);
        generator.register(AstroCraftItems.SPACE_SUIT_LEGGINGS, Models.GENERATED);
        generator.register(AstroCraftItems.SPACE_SUIT_BOOTS, Models.GENERATED);
    }

    @Override
    public String getName() {
        return "AstroCraftModelProvider";
    }
}
