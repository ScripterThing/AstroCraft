package com.cubikore.astro;

import com.cubikore.astro.block.AstroCraftBlocks;
import com.cubikore.astro.block.entity.AstroCraftBlockEntities;
import com.cubikore.astro.block.fluid.AstroCraftFluids;
import com.cubikore.astro.client.input.AstroCraftKeyBinds;
import com.cubikore.astro.command.AstroCraftCommands;
import com.cubikore.astro.game.server.AstroCraftServerGameManager;
import com.cubikore.astro.item.AstroCraftArmorMaterials;
import com.cubikore.astro.item.AstroCraftItems;
import com.cubikore.astro.networking.AstroCraftNetworking;
import com.cubikore.astro.sound.AstroCraftSounds;
import com.cubikore.astro.world.worldgen.AstroCraftChunkGenerators;
import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;

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

        AstroCraftKeyBinds.registerKeyBinds();
        AstroCraftArmorMaterials.registerMaterials();
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