package com.cubikore.astro.gui;

import com.cubikore.astro.AstroCraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class AstroCraftSprites {
    public static Identifier EARTH_SPRITE = Identifier.of(AstroCraft.MOD_ID, "earth");
    public static Identifier VENUS_SPRITE = Identifier.of(AstroCraft.MOD_ID, "venus");
    public static Identifier MERCURY_SPRITE = Identifier.of(AstroCraft.MOD_ID, "mercury");
    public static Identifier MARS_SPRITE = Identifier.of(AstroCraft.MOD_ID, "mars");
    public static Identifier JUPITER_SPRITE = Identifier.of(AstroCraft.MOD_ID, "jupiter");
    public static Identifier SATURN_SPRITE = Identifier.of(AstroCraft.MOD_ID, "saturn");
    public static Identifier URANUS_SPRITE = Identifier.of(AstroCraft.MOD_ID, "uranus");
    public static Identifier NEPTUNE_SPRITE = Identifier.of(AstroCraft.MOD_ID, "neptune");

    public static Identifier SUN_SPRITE = Identifier.of(AstroCraft.MOD_ID, "sun");

    public static Identifier PLANET_SELECT_SPRITE = Identifier.of(AstroCraft.MOD_ID, "planet_selected");

    public static Identifier SATURN_RING_SPRITE = Identifier.of(AstroCraft.MOD_ID, "saturn_ring");
    public static Identifier URANUS_RING_SPRITE = Identifier.of(AstroCraft.MOD_ID, "uranus_ring");

    public static Identifier BLACK_SPRITE = Identifier.of(AstroCraft.MOD_ID, "black");

    public static Identifier getSpriteFromPlanetName(String planetName) {
        return switch (planetName) {
            case "Earth" -> EARTH_SPRITE;
            case "Venus" -> VENUS_SPRITE;
            case "Mercury" -> MERCURY_SPRITE;
            case "Mars" -> MARS_SPRITE;
            case "Jupiter" -> JUPITER_SPRITE;
            case "Saturn" -> SATURN_SPRITE;
            case "Uranus" -> URANUS_SPRITE;
            case "Neptune" -> NEPTUNE_SPRITE;
            default -> Identifier.ofVanilla("");
        };
    }
}
