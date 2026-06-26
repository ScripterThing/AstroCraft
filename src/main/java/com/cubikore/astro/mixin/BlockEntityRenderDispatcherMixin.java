package com.cubikore.astro.mixin;

import com.cubikore.astro.client.ClientStorage;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin implements SynchronousResourceReloader {
    @Shadow
    private static void runReported(BlockEntity blockEntity, Runnable runnable) {
        throw new AssertionError();
    }

    @Shadow
    public abstract <E extends BlockEntity> BlockEntityRenderer<E> get(E blockEntity);

    @Shadow
    private static <T extends BlockEntity> void render(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        throw new AssertionError();
    }

    @Shadow
    public Camera camera;

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", cancellable = true)
    private <E extends BlockEntity> void offsetBlockEntities(E blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        matrices.translate(-ClientStorage.terrainOffset[0], -ClientStorage.terrainOffset[1], -ClientStorage.terrainOffset[2]);
        BlockEntityRenderer<E> blockEntityRenderer = this.get(blockEntity);
        if (blockEntityRenderer != null) {
            if (blockEntity.hasWorld() && blockEntity.getType().supports(blockEntity.getCachedState())) {
                if (blockEntityRenderer.isInRenderDistance(blockEntity, this.camera.getPos())) {
                    runReported(blockEntity, () -> render(blockEntityRenderer, blockEntity, tickDelta, matrices, vertexConsumers));
                }
            }
        }
        ci.cancel();
    }

    @Override
    @Unique
    public void reload(ResourceManager manager) {
    }
}
