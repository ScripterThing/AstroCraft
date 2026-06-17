package com.cubikore.astro.block.special;

import com.cubikore.astro.util.CommonStorage;
import com.cubikore.astro.entity.SeatingEntity;
import com.cubikore.astro.networking.payload.CaptainSeatMountedPayload;
import com.cubikore.astro.server.ServerStorage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CaptainSeatBlock extends Block {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public CaptainSeatBlock() {
        super(AbstractBlock.Settings.create()
                .strength(4f)
                .dropsNothing()
                .sounds(BlockSoundGroup.METAL)
                .nonOpaque()
                .allowsSpawning(Blocks::never)
                .solidBlock(Blocks::never)
                .suffocates(Blocks::never)
                .blockVision(Blocks::never)
                .noCollision()
        );

        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.appendProperties(builder);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos blockPos, PlayerEntity player, BlockHitResult hit) {
        if(!world.isClient) {
            if(!ServerStorage.seatingEntities.containsKey(blockPos)) {
                SeatingEntity entity = new SeatingEntity(world);

                Vec3d pos = new Vec3d(blockPos.getX() + 0.5, blockPos.getY()+0.5625,  blockPos.getZ() + 0.5);

                entity.setPosition(pos);
                entity.tags.add("captain_seat");

                ServerStorage.seatingEntities.put(blockPos, entity);
                CommonStorage.captainSeatEntities.put(blockPos, entity);

                world.spawnEntity(entity);

                player.startRiding(entity);

                ServerPlayNetworking.send((ServerPlayerEntity) player, new CaptainSeatMountedPayload(blockPos, entity.getId()));
            }
            else
                return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }
}
