package com.cubikore.astro.command;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.networking.payload.FTLJumpPayload;
import com.cubikore.astro.server.ServerStorage;
import com.cubikore.astro.universe.Planet;
import com.cubikore.astro.universe.Universe;
import com.cubikore.astro.weather.PlanetWeather;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class AstroCraftCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                    dispatcher.register(
                            CommandManager.literal("setplanetweather")
                                    .then(CommandManager.argument("planet", StringArgumentType.word())
                                            .suggests((context, builder) -> {
                                                for (String planet : AstroCraftClient.weatherManager.supportedPlanets) {
                                                    builder.suggest(planet);
                                                }
                                                return builder.buildFuture();
                                            })
                                            .then(CommandManager.argument("weather_type", StringArgumentType.word())
                                                    .suggests((context, builder) -> {
                                                        for (String type : AstroCraftClient.weatherManager.clientWeatherTypes) {
                                                            builder.suggest(type);
                                                        }
                                                        return builder.buildFuture();
                                                    })
                                                    .then(CommandManager.argument("duration_ticks", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                                                            .then(CommandManager.argument("intensity", FloatArgumentType.floatArg(0, 10))
                                                                    .executes(context -> {
                                                                        String planet = StringArgumentType.getString(context, "planet");
                                                                        String type = StringArgumentType.getString(context, "weather_type");
                                                                        int duration = IntegerArgumentType.getInteger(context, "duration_ticks");
                                                                        float intensity = FloatArgumentType.getFloat(context, "intensity");

                                                                        Identifier planetId = Identifier.tryParse(planet);

                                                                        AstroCraft.weatherManager.setWeather(planetId, new PlanetWeather(type, duration, intensity));

                                                                        String feedbackString = "Set weather of " + planet + " to " + type + " for " + duration + " ticks with intensity of " + intensity;

                                                                        context.getSource().sendFeedback(() -> Text.literal(feedbackString), false);

                                                                        return 1;
                                                                    })
                                                            )
                                                    )
                                            )
                                    )
                    );

                    dispatcher.register(
                            CommandManager.literal("executeFTLJump")
                                    .then(CommandManager.argument("target", StringArgumentType.word())
                                            .suggests(((context, builder) -> {
                                                for(Planet planet : Universe.planets) {
                                                    if(!planet.planetId.equals(Universe.SUN_ID))
                                                        builder.suggest(planet.planetId.toString());
                                                }

                                                return builder.buildFuture();
                                            }))
                                            .executes(context -> {
                                                String targetPlanetId = StringArgumentType.getString(context, "target");

                                                Planet targetPlanet = Universe.getPlanet(Identifier.tryParse(targetPlanetId));
                                                Planet sun = Universe.getPlanet(Universe.SUN_ID);

                                                if(targetPlanet != null && sun != null) {
                                                    Vector3f sunPos = new Vector3f(sun.position[0], sun.position[1], sun.position[2]);
                                                    Vector3f planetPos = new Vector3f(targetPlanet.position[0], targetPlanet.position[1], targetPlanet.position[2]);

                                                    Vector3f direction = new Vector3f(sunPos).sub(planetPos).normalize();
                                                    Vector3f targetPosition = direction.mul(targetPlanet.radius[0] / 2.0f).add(planetPos);

                                                    Vector3f targetDir = new Vector3f(planetPos).sub(targetPosition).normalize();

                                                    float yaw = (float)Math.toDegrees(Math.atan2(targetDir.x, -targetDir.z));

                                                    targetPosition.y = (targetPlanet.radius[0] + planetPos.y) + 1f;

                                                    ServerStorage.worldOffset = new Vector4f(targetPosition.x, targetPosition.y, targetPosition.z, yaw);
                                                    ServerStorage.prevWorldOffset.set(ServerStorage.worldOffset);

                                                    for(ServerPlayerEntity player : PlayerLookup.all(context.getSource().getServer())) {
                                                        ServerPlayNetworking.send(player, new FTLJumpPayload(targetPosition, yaw));
                                                    }
                                                }

                                                return 1;
                                            })
                                    )
                    );
                }
        );
    }
}
