package com.cubikore.astro.client.input;

import com.cubikore.astro.AstroCraft;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AstroCraftKeyBinds {
    public static KeyBinding CUSTOMIZATION_KEY = register("customization_screen", GLFW.GLFW_KEY_G);
    public static KeyBinding FLASHLIGHT_KEY = register("flashlight", GLFW.GLFW_KEY_F);

    private static KeyBinding register(String name, int key) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.astrocraft." + name, // translation key
                InputUtil.Type.KEYSYM,
                key,          // default key
                "category.astrocraft.keys" // category in controls menu
        ));
    }

    public static void registerKeyBinds() {
        AstroCraft.LOGGER.info("Registerting keybinds for " + AstroCraft.MOD_ID);
    }
}
