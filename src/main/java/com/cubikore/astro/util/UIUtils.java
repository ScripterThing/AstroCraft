package com.cubikore.astro.util;

import com.cubikore.astro.mixin.DrawContextAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class UIUtils {
    public static void drawBorder(DrawContext context, int x1, int y1, int x2, int y2, int size, int color) {
        context.fill(x1 - size, y1 - size, x2 + size, y1, color);
        context.fill(x1 - size, y1, x1, y2 + size, color);
        context.fill(x1, y2, x2 + size, y2 + size, color);
        context.fill(x2, y2, x2 + size, y1, color);
    }

    public static Sprite getSprite(DrawContext context, Identifier id) {
        DrawContextAccessor accessor = (DrawContextAccessor) context;
        return accessor.getGuiAtlasManager().getSprite(id);
    }

    public static void drawCircle(DrawContext context, int centerX, int centerY, int radius, int color) {
        MatrixStack matrices = context.getMatrices();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        BufferBuilder buffer = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);

        int segments = 100;

        for (int i = 0; i <= segments; i++) {
            double angle = (Math.PI * 2 * i) / segments;
            float x = centerX + (float)(Math.cos(angle) * radius);
            float y = centerY + (float)(Math.sin(angle) * radius);

            buffer.vertex(matrices.peek().getPositionMatrix(), x, y, 0)
                    .color(color);
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
}
