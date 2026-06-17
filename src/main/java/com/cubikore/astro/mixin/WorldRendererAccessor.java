package com.cubikore.astro.mixin;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
    @Accessor("frustum")
    Frustum getFrustum();

    @Accessor("frustum")
    void setFrustum(Frustum frustum);

    @Accessor("bufferBuilders")
    BufferBuilderStorage getBufferBuilders();

    @Accessor("entityRenderDispatcher")
    EntityRenderDispatcher getEntityRenderDispatcher();

    @Invoker("setupTerrain")
    void invokeSetUpTerrain(Camera camera, Frustum frustum, boolean hasForcedFrustum, boolean spectator);

    @Invoker("applyFrustum")
    void invokeApplyFrustum(Frustum frustum);

    @Invoker("renderLayer")
    void invokeRenderLayer(RenderLayer renderLayer, double x, double y, double z, Matrix4f matrix4f, Matrix4f positionMatrix);

    @Invoker("renderEntity")
    void invokeRenderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers);
}
