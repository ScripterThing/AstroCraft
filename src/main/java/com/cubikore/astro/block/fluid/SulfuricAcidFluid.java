package com.cubikore.astro.block.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Optional;

public abstract class SulfuricAcidFluid extends FlowableFluid {
    @Override
    public Fluid getStill() {
        return AstroCraftFluids.STILL_SULFURIC_ACID;
    }

    @Override
    public Fluid getFlowing() {
        return AstroCraftFluids.FLOWING_SULFURIC_ACID;
    }

    @Override
    public int getTickRate(WorldView world) {
        return 5;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

    //CHANGE
    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == AstroCraftFluids.STILL_SULFURIC_ACID || fluid == AstroCraftFluids.FLOWING_SULFURIC_ACID;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public Item getBucketItem() {
        return Items.WATER_BUCKET;
    }

    @Override
    protected boolean isInfinite(World world) {
        return false;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {

    }

    @Override
    protected int getMaxFlowDistance(WorldView world) {
        return 4;
    }

    @Override
    protected float getBlastResistance() {
        return 100;
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return AstroCraftFluids.SULFURIC_ACID.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    public static class Still extends SulfuricAcidFluid {
        @Override
        public boolean isStill(FluidState state) {
            return true;
        }

        @Override
        public int getLevel(FluidState state) {
            return 8;
        }
    }

    public static class Flowing extends SulfuricAcidFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public boolean isStill(FluidState state) {
            return false;
        }

        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }
    }
}
