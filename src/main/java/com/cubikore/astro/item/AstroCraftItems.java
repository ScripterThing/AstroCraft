package com.cubikore.astro.item;

import com.cubikore.astro.AstroCraft;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AstroCraftItems {
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AstroCraft.MOD_ID, name), item);
    }

    public static void registerItems() {
        AstroCraft.LOGGER.info("Registering items for Astro Craft");
    }
}
