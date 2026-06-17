package com.cubikore.astro.block.fluid;

import com.cubikore.astro.AstroCraft;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AstroCraftFluids {
    public static FlowableFluid STILL_SULFURIC_ACID = registerFluid("sulfuric_acid", new SulfuricAcidFluid.Still());
    public static FlowableFluid FLOWING_SULFURIC_ACID = registerFluid("sulfuric_acid_flowing", new SulfuricAcidFluid.Flowing());

    public static FluidBlock SULFURIC_ACID = registerFluidBlock("sulfuric_acid", new FluidBlock(STILL_SULFURIC_ACID, FabricBlockSettings.copyOf(Blocks.WATER)));

    private static FlowableFluid registerFluid(String name, FlowableFluid fluid) {
        return Registry.register(Registries.FLUID, Identifier.of(AstroCraft.MOD_ID, name), fluid);
    }

    private static FluidBlock registerFluidBlock(String name, FluidBlock fluidBlock) {
        return Registry.register(Registries.BLOCK, Identifier.of(AstroCraft.MOD_ID, name), fluidBlock);
    }

    public static void registerFluids() {
        AstroCraft.LOGGER.info("Registering fluids for astrocraft");
    }
}
