package com.cubikore.astro;

import com.cubikore.astro.block.AstroCraftBlocks;
import com.cubikore.astro.block.entity.AstroCraftBlockEntities;
import com.cubikore.astro.block.fluid.AstroCraftFluids;
import com.cubikore.astro.command.AstroCraftCommands;
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

    public static WeatherManager weatherManager = new WeatherManager();

    public static Universe universe = new Universe();

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

        universe.init();

        registerServerEvents();

        initializePlanets();

        AstroCraftCommands.registerCommands();
	}

    private static void initializePlanets() {
        universe.add(new Planet(new Vector3f(-1030, 0, 0), 1000.0f, new Vector3f(1.0f), new Atmosphere(1.0f, 4f), "Earth"));
        universe.add(new Planet(new Vector3f(200000, 0, 0), 3000f, new Vector3f(1.0f, 0.944f, 0.852f), new Atmosphere(54649.0f, 3.19f), "Sun"));
        universe.add(new Planet(new Vector3f(93933, 0, 106066), 1000f, new Vector3f(1.0f), new Atmosphere(0, 3.19f), "Mercury"));
        universe.add(new Planet(new Vector3f(30855, 0, -61563), 950f, new Vector3f(1.0f), new Atmosphere(0.5f, 3.19f, new Vector3f(483, 530, 669)), "Venus"));
        universe.add(new Planet(new Vector3f(-75000, 0, -216506), 950f, new Vector3f(1.0f), new Atmosphere(0.5f, 3.19f, new Vector3f(546, 649, 643)), "Mars"));
        universe.add(new Planet(new Vector3f(892820, 0, -400000), 10973f, new Vector3f(1.0f), new Atmosphere(0.3f, 3.19f, new Vector3f(700, 700, 642)), "Jupiter"));
        universe.add(new Planet(new Vector3f(2337171, -10000, 923454), 9140f, new Vector3f(1.0f), new Atmosphere(0.3f, 4.0f, new Vector3f(700, 612, 554)), "Saturn"));
        universe.add(new Planet(new Vector3f(2361192, 100000, 786646), 3865f, new Vector3f(1.0f), new Atmosphere(0.3f, 3.19f, new Vector3f(678, 700, 471)), "Uranus"));
        universe.add(new Planet(new Vector3f(2278461, -300000, -1200000), 3865f, new Vector3f(1.0f), new Atmosphere(0.3f, 3.19f, new Vector3f(700, 554, 408)), "Neptune"));

        weatherManager.add(Universe.MERCURY_ID, PlanetWeather.getClear());
        weatherManager.add(Universe.VENUS_ID, new PlanetWeather("clear", 200, 1));
        weatherManager.add(Universe.MARS_ID, PlanetWeather.getClear());
    }

    private void registerServerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            Vector3f worldOffset3d = new Vector3f(ServerStorage.worldOffset.x, ServerStorage.worldOffset.y, ServerStorage.worldOffset.z);
            ServerPlayNetworking.send(handler.player, new SyncWorldOffsetPayload(worldOffset3d, ServerStorage.worldOffset.w));

            weatherManager.sendWeatherState(handler.player, true, Identifier.of(MOD_ID, "earth"));
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            Identifier from = AstroCraft.universe.getPlanetIdFromWorld(origin.getRegistryKey());
            weatherManager.sendWeatherState(player, true, from);
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            handleShip(server);
            handlePlanetWeather(server);
        });
    }

    private void handlePlanetWeather(MinecraftServer server) {
        weatherManager.tick(server);
    }

    private void handleShip(MinecraftServer server) {
        Vector3f shipPos = new Vector3f(ServerStorage.worldOffset.x, ServerStorage.worldOffset.y, ServerStorage.worldOffset.z);

        if(!AstroCraftShip.doingFTLJump) {
            if(AstroCraftShip.autoThrust) {
                AstroCraftShip.moveShip(new Vector4f(1, 0, 0, 0));
            }

            float closestSurface = Float.MAX_VALUE;

            for(Planet planet : universe.planets) {
                float dst = shipPos.distance(new Vector3f(planet.position[0], planet.position[1], planet.position[2])) - planet.radius[0];

                if(dst < closestSurface) {
                    closestSurface = dst;
                }
            }

            ShipParameters.shipSpeed = ShipParameters.getSpeed(closestSurface);

            boolean allow = false;

            for(Entity seatingEntity : CommonStorage.captainSeatEntities.values()) {
                if(seatingEntity.hasPassengers()) {
                    allow = true;
                }
                else {
                    allow = AstroCraftShip.autoThrust;
                }
            }

            if(allow) {
                ServerStorage.worldOffset.add(new Vector4f(ServerStorage.directionMoving).mul(ShipParameters.shipSpeed, ShipParameters.shipSpeed, ShipParameters.shipSpeed, 1));

                if(!ServerStorage.directionMoving.equals(0, 0, 0, 0)) {
                    for (ServerPlayerEntity sPlayer : PlayerLookup.all(server)) {
                        ServerPlayNetworking.send(sPlayer, new ShipMovingPayload(ServerStorage.worldOffset.x, ServerStorage.worldOffset.y, ServerStorage.worldOffset.z, ServerStorage.worldOffset.w, true));
                    }
                }
                else if(!ServerStorage.worldOffset.equals(ServerStorage.prevWorldOffset)) {
                    for (ServerPlayerEntity sPlayer : PlayerLookup.all(server)) {
                        ServerPlayNetworking.send(sPlayer, new ShipMovingPayload(ServerStorage.worldOffset.x, ServerStorage.worldOffset.y, ServerStorage.worldOffset.z, ServerStorage.worldOffset.w, false));
                    }
                }
            }

            ServerStorage.prevWorldOffset.set(ServerStorage.worldOffset);
        }
        else {
            int size = PlayerLookup.all(server).size();

            if((float) AstroCraftShip.clientConf / size >= 0.3f) {
                AstroCraftShip.doingFTLJump = false;
                AstroCraftShip.clientConf = 0;

                ServerStorage.worldOffset.set(AstroCraftShip.ftlJumpDestination);
                ServerStorage.prevWorldOffset.set(ServerStorage.worldOffset);
            }
        }
    }
}