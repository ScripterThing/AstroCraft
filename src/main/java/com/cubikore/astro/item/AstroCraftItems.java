package com.cubikore.astro.item;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.components.AstComponents;
import com.cubikore.astro.item.special.SpaceSuitArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AstroCraftItems {
    public static final Item SPACE_SUIT_HELMET = registerItem("space_suit_helmet",
            new SpaceSuitArmorItem(AstroCraftArmorMaterials.SPACE_SUIT, ArmorItem.Type.HELMET, new Item.Settings().component(AstComponents.SUIT_COLOR_COMPONENT, "white")));
    public static final Item SPACE_SUIT_CHESTPLATE = registerItem("space_suit_chestplate",
            new SpaceSuitArmorItem(AstroCraftArmorMaterials.SPACE_SUIT, ArmorItem.Type.CHESTPLATE, new Item.Settings().component(AstComponents.SUIT_COLOR_COMPONENT, "white")));
    public static final Item SPACE_SUIT_LEGGINGS = registerItem("space_suit_leggings",
            new SpaceSuitArmorItem(AstroCraftArmorMaterials.SPACE_SUIT, ArmorItem.Type.LEGGINGS, new Item.Settings().component(AstComponents.SUIT_COLOR_COMPONENT, "white")));
    public static final Item SPACE_SUIT_BOOTS = registerItem("space_suit_boots",
            new SpaceSuitArmorItem(AstroCraftArmorMaterials.SPACE_SUIT, ArmorItem.Type.BOOTS, new Item.Settings().component(AstComponents.SUIT_COLOR_COMPONENT, "white")));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AstroCraft.MOD_ID, name), item);
    }

    public static void registerItems() {
        AstroCraft.LOGGER.info("Registering items for Astro Craft");
    }
}
