package com.cubikore.astro.mixin;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.client.renderer.ShadowRenderer;
import com.cubikore.astro.dimension.DimensionKeys;
import com.cubikore.astro.util.PlayerLightAccess;
import com.cubikore.astro.weather.planet.ClientWeather;
import com.mojang.blaze3d.systems.RenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    private double lastTranslucentSortX;
    @Shadow
    private double lastTranslucentSortY;
    @Shadow
    private double lastTranslucentSortZ;
    @Shadow
    private ChunkBuilder chunkBuilder;
    @Shadow @Final @Mutable
    private ObjectArrayList<ChunkBuilder.BuiltChunk> builtChunks;
    @Shadow @Final @Mutable
    private EntityRenderDispatcher entityRenderDispatcher;

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "renderClouds", cancellable = true)
    private void skipCloudRendering(MatrixStack matrices, Matrix4f matrix4f, Matrix4f matrix4f2, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        ClientWeather weather = AstroCraftClient.weatherManager.get(client.world.getRegistryKey());

        if(DimensionKeys.isSpace(client.world) || !weather.canRenderClouds())
            ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderEntity", cancellable = true)
    private void offsetEntities(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        double d = MathHelper.lerp((double)tickDelta, entity.lastRenderX, entity.getX());
        double e = MathHelper.lerp((double)tickDelta, entity.lastRenderY, entity.getY());
        double f = MathHelper.lerp((double)tickDelta, entity.lastRenderZ, entity.getZ());
        float g = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());

        cameraX -= ClientStorage.terrainOffset[0];
        cameraY -= ClientStorage.terrainOffset[1];
        cameraZ -= ClientStorage.terrainOffset[2];

        this.entityRenderDispatcher
                .render(entity, d - cameraX, e - cameraY, f - cameraZ, g, tickDelta, matrices, vertexConsumers, this.entityRenderDispatcher.getLight(entity, tickDelta));
        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderLayer", cancellable = true)
    private void offset(RenderLayer renderLayer, double x, double y, double z, Matrix4f matrix4f, Matrix4f positionMatrix, CallbackInfo ci) {
        x -= ClientStorage.terrainOffset[0];
        y -= ClientStorage.terrainOffset[1];
        z -= ClientStorage.terrainOffset[2];

        if(client.player != null) {
            PlayerLightAccess access = (PlayerLightAccess) client.player;
            LightRenderHandle<PointLightData> handle = access.getLightHandle();
            if(handle != null && handle.isValid()) {
                access.getLightHandle().getLightData().setPosition(x, y, z);
            }
        }

        RenderSystem.assertOnRenderThread();
        renderLayer.startDrawing();
        MinecraftClient client = MinecraftClient.getInstance();
        if (renderLayer == RenderLayer.getTranslucent()) {
            client.getProfiler().push("translucent_sort");
            double d = x - this.lastTranslucentSortX;
            double e = y - this.lastTranslucentSortY;
            double f = z - this.lastTranslucentSortZ;
            if (d * d + e * e + f * f > 1.0) {
                int i = ChunkSectionPos.getSectionCoord(x);
                int j = ChunkSectionPos.getSectionCoord(y);
                int k = ChunkSectionPos.getSectionCoord(z);
                boolean bl = i != ChunkSectionPos.getSectionCoord(this.lastTranslucentSortX)
                        || k != ChunkSectionPos.getSectionCoord(this.lastTranslucentSortZ)
                        || j != ChunkSectionPos.getSectionCoord(this.lastTranslucentSortY);
                this.lastTranslucentSortX = x;
                this.lastTranslucentSortY = y;
                this.lastTranslucentSortZ = z;
                int l = 0;

                for (ChunkBuilder.BuiltChunk builtChunk : this.builtChunks) {
                    if (l < 15 && (bl || builtChunk.method_52841(i, j, k)) && builtChunk.scheduleSort(renderLayer, this.chunkBuilder)) {
                        l++;
                    }
                }
            }

            client.getProfiler().pop();
        }

        client.getProfiler().push("filterempty");
        client.getProfiler().swap((Supplier<String>)(() -> "render_" + renderLayer));
        boolean bl2 = renderLayer != RenderLayer.getTranslucent();
        ObjectListIterator<ChunkBuilder.BuiltChunk> objectListIterator = this.builtChunks.listIterator(bl2 ? 0 : this.builtChunks.size());
        ShaderProgram shaderProgram = RenderSystem.getShader();
        shaderProgram.initializeUniforms(VertexFormat.DrawMode.QUADS, matrix4f, positionMatrix, client.getWindow());
        shaderProgram.bind();
        GlUniform glUniform = shaderProgram.chunkOffset;

        while (bl2 ? objectListIterator.hasNext() : objectListIterator.hasPrevious()) {
            ChunkBuilder.BuiltChunk builtChunk2 = bl2 ? (ChunkBuilder.BuiltChunk)objectListIterator.next() : objectListIterator.previous();
            if (!builtChunk2.getData().isEmpty(renderLayer)) {
                VertexBuffer vertexBuffer = builtChunk2.getBuffer(renderLayer);
                BlockPos blockPos = builtChunk2.getOrigin();
                if (glUniform != null) {
                    glUniform.set((float)(blockPos.getX() - x), (float)(blockPos.getY() - y), (float)(blockPos.getZ() - z));
                    glUniform.upload();
                }

                vertexBuffer.bind();
                vertexBuffer.draw();
            }
        }

        if (glUniform != null) {
            glUniform.set(0.0F, 0.0F, 0.0F);
        }

        shaderProgram.unbind();
        VertexBuffer.unbind();
        client.getProfiler().pop();
        renderLayer.endDrawing();

        ci.cancel();
    }
}
