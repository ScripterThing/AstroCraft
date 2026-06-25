package com.cubikore.astro.util;

import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;

public class RenderUtils {
    public static void renderCube(
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
                0, 0, -1,
                light, overlay);

        // Back face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                1,1,1,
                0,1,1,
                0,0,1,
                1,0,1,
                0, 0, 1,
                light, overlay);

        // Left face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                0,1,1,
                0,1,0,
                0,0,0,
                0,0,1,
                0, 0, -1,
                light, overlay);

        // Right face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                1,1,0,
                1,1,1,
                1,0,1,
                1,0,0,
                0, 0, 1,
                light, overlay);

        // Top face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                0,1,1,
                1,1,1,
                1,1,0,
                0,1,0,
                0, 1, 0,
                light, overlay);

        // Bottom face
        quad(consumer, matrix,
                u0, v0, u1, v1,
                0,0,0,
                1,0,0,
                1,0,1,
                0,0,1,
                0, -1, 0,
                light, overlay);
    }

    public static void quad(
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
            float nx, float ny, float nz,
            int light,
            int overlay) {

        consumer.vertex(matrix, x1, y1, z1)
                .texture(u0, v1)
                .color(255, 255, 255, 255).light(light);

        consumer.vertex(matrix, x2, y2, z2)
                .texture(u1, v1)
                .color(255, 255, 255, 255).light(light);

        consumer.vertex(matrix, x3, y3, z3)
                .texture(u1, v0)
                .color(255, 255, 255, 255).light(light);

        consumer.vertex(matrix, x4, y4, z4)
                .texture(u0, v0)
                .color(255, 255, 255, 255).light(light);
    }
}
