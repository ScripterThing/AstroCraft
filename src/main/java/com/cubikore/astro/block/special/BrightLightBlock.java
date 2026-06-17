package com.cubikore.astro.block.special;

import com.cubikore.astro.block.entity.BrightLightBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BrightLightBlock extends BlockWithEntity {
    public static final DirectionProperty LIGHT_DIR = Properties.FACING;

    public BrightLightBlock(Settings settings) {
        super(settings);

        this.setDefaultState(this.getStateManager().getDefaultState().with(LIGHT_DIR, Direction.DOWN));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_DIR);
        super.appendProperties(builder);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(LIGHT_DIR, Direction.DOWN);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(BrightLightBlock::new);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BrightLightBlockEntity(pos, state);
    }
}
