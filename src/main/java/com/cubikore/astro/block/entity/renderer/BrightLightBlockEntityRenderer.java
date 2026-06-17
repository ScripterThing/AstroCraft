package com.cubikore.astro.block.entity.renderer;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.block.entity.BrightLightBlockEntity;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class BrightLightBlockEntityRenderer implements BlockEntityRenderer<BrightLightBlockEntity> {
    public BrightLightBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

    }

    @Override
    public void render(BrightLightBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        RenderLayer layer = VeilRenderType.get(AstroCraftClient.UNLIT_LAYER);

        matrices.push();

        VertexConsumer consumer = vertexConsumers.getBuffer(layer);
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

        MinecraftClient client = MinecraftClient.getInstance();

        BakedModel model = client.getBlockRenderManager()
                .getModel(entity.getCachedState());

        Sprite sprite = model.getParticleSprite();

        float u0 = sprite.getMinU();
        float u1 = sprite.getMaxU();
        float v0 = sprite.getMinV();
        float v1 = sprite.getMaxV();

        renderCube(consumer, positionMatrix, u0, v0, u1, v1, light, overlay);

        matrices.pop();
    }

    private static void renderCube(
            VertexConsumer consumer,
            Matrix4f matrix,
            float u0,
            float v0,
            float u1,
            float v1,
            int light,
            int overlay) {

        // Front face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                0,1,0,
                1,1,0,
                1,0,0,
                0,0,0,
                light, overlay);

        // Back face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                1,1,1,
                0,1,1,
                0,0,1,
                1,0,1,
                light, overlay);

        // Left face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                0,1,1,
                0,1,0,
                0,0,0,
                0,0,1,
                light, overlay);

        // Right face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                1,1,0,
                1,1,1,
                1,0,1,
                1,0,0,
                light, overlay);

        // Top face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                0,1,1,
                1,1,1,
                1,1,0,
                0,1,0,
                light, overlay);

        // Bottom face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                0,0,0,
                1,0,0,
                1,0,1,
                0,0,1,
                light, overlay);
    }

    private static void quad(
            VertexConsumer consumer,
            Matrix4f matrix,
            float u0,
            float v0,
            float u1,
            float v1,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float x4, float y4, float z4,
            int light,
            int overlay) {

        consumer.vertex(matrix, x1, y1, z1)
                .texture(u0, v1)
                .color(255, 255, 255, 255);

        consumer.vertex(matrix, x2, y2, z2)
                .texture(u1, v1)
                .color(255, 255, 255, 255);

        consumer.vertex(matrix, x3, y3, z3)
                .texture(u1, v0)
                .color(255, 255, 255, 255);

        consumer.vertex(matrix, x4, y4, z4)
                .texture(u0, v0)
                .color(255, 255, 255, 255);
    }
}
