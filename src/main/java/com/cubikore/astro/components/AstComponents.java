package com.cubikore.astro.components;

import com.cubikore.astro.AstroCraft;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AstComponents {
    public static final ComponentType<String> SUIT_COLOR_COMPONENT = registerComponent(String.class, Codec.STRING, "suit_color");

    private static <T> ComponentType<T> registerComponent(Class<T> clazz, Codec<T> codec, String name) {
        return Registry.register(
                Registries.DATA_COMPONENT_TYPE,
                Identifier.of(AstroCraft.MOD_ID, name),
                ComponentType.<T>builder().codec(codec).build()
        );
    }

    public static void registerComponents() {
        AstroCraft.LOGGER.info("Registering data components for " + AstroCraft.MOD_ID);
    }
}
