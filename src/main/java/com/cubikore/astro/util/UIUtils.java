package com.cubikore.astro.util;

import com.cubikore.astro.mixin.DrawContextAccessor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;
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
}
