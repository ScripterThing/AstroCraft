package com.cubikore.astro.util;

import net.minecraft.util.Formatting;

public class ColorUtil {
    public static int toARGB(int rgb) {
        return 0xFF000000 | rgb;
    }

    public static int toARGB(String color) {
        Formatting formatting = Formatting.byName(color);

        if(formatting == null)
            return 0xFFFFFFFF;

        return toARGB(formatting.getColorValue());
    }

    public static int getSuitARGB(String color) {
        if(color.equals("black"))
            return 0xFF141414;

        return toARGB(color);
    }
}
