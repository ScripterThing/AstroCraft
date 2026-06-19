package com.cubikore.astro;

import com.cubikore.astro.block.AstroCraftBlocks;
import com.cubikore.astro.block.entity.AstroCraftBlockEntities;
import com.cubikore.astro.block.fluid.AstroCraftFluids;
import com.cubikore.astro.command.AstroCraftCommands;
import com.cubikore.astro.events.client.AstroCraftPlanetRegisterEvents;
import com.cubikore.astro.game.server.AstroCraftServerGameManager;
import com.cubikore.astro.item.AstroCraftItems;
import com.cubikore.astro.networking.AstroCraftNetworking;
import com.cubikore.astro.networking.payload.ShipMovingPayload;
import com.cubikore.astro.networking.payload.SyncWorldOffsetPayload;
import com.cubikore.astro.server.ServerStorage;
import com.cubikore.astro.ship.AstroCraftShip;
import com.cubikore.astro.ship.ShipParameters;
import com.cubikore.astro.sound.AstroCraftSounds;
import com.cubikore.astro.universe.Atmosphere;
import com.cubikore.astro.universe.Planet;
import com.cubikore.astro.universe.Universe;
import com.cubikore.astro.util.CommonStorage;
import com.cubikore.astro.weather.PlanetWeather;
import com.cubikore.astro.weather.WeatherManager;
import com.cubikore.astro.world.worldgen.AstroCraftChunkGenerators;
import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AstroCraft implements ModInitializer {
	public static final String MOD_ID = "astrocraft";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Gson gson = new Gson();

    public static AstroCraftServerGameManager serverGameManager = new AstroCraftServerGameManager();

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

        AstroCraftItems.registerItems();
        AstroCraftBlocks.registerBlocks();
        AstroCraftBlockEntities.registerBlockEntities();
        AstroCraftNetworking.registerPayloads();
        AstroCraftNetworking.registerC2SPayloads();
        AstroCraftFluids.registerFluids();
        AstroCraftSounds.registerSounds();
        AstroCraftChunkGenerators.registerChunkGenerators();
        AstroCraftCommands.registerCommands();

        serverGameManager.init();
	}
}