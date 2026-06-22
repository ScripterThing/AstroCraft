package com.cubikore.astro.util;

import com.cubikore.astro.item.AstroCraftItems;
import net.minecraft.item.ItemStack;

public class ItemUtils {
    public static boolean isSpaceSuit(ItemStack stack) {
        return stack.isOf(AstroCraftItems.SPACE_SUIT_HELMET) ||stack.isOf(AstroCraftItems.SPACE_SUIT_CHESTPLATE)
                || stack.isOf(AstroCraftItems.SPACE_SUIT_LEGGINGS) || stack.isOf(AstroCraftItems.SPACE_SUIT_BOOTS);
    }
}
