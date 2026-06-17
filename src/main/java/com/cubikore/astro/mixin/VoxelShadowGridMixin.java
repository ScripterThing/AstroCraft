package com.cubikore.astro.mixin;

import foundry.veil.impl.client.render.light.VoxelShadowGrid;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VoxelShadowGrid.class)
public class VoxelShadowGridMixin {

    @Inject(at = @At("HEAD"), method = "voxelOccupancy", cancellable = true)
    private static void addToVoxelGrid(ClientWorld level, BlockPos pos, BlockState state, CallbackInfoReturnable<Byte> cir) {
        if(isLeaf(state)) {
            cir.setReturnValue((byte) 0xFF);
        }
    }

    private static boolean isLeaf(BlockState state) {
        return state.isIn(BlockTags.LEAVES);
    }
}
