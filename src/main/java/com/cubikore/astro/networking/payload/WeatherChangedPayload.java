package com.cubikore.astro.networking.payload;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.weather.PlanetWeather;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record WeatherChangedPayload(String weatherJson, boolean planetChanged, Identifier fromPlanetName) implements CustomPayload {
    public static final Identifier RL = Identifier.of(AstroCraft.MOD_ID, "weather_changed");
    public static final Id<WeatherChangedPayload> ID = new Id<>(RL);

    public static final PacketCodec<RegistryByteBuf, WeatherChangedPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, WeatherChangedPayload::weatherJson,
            PacketCodecs.BOOL, WeatherChangedPayload::planetChanged,
            Identifier.PACKET_CODEC, WeatherChangedPayload::fromPlanetName,
            WeatherChangedPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void C2S(WeatherChangedPayload payload, ServerPlayNetworking.Context context) {

    }

    public static void S2C(WeatherChangedPayload payload, ClientPlayNetworking.Context context) {
        String weatherJson = payload.weatherJson();

        ClientStorage.currentWeather = AstroCraft.gson.fromJson(weatherJson, PlanetWeather.class);

        AstroCraftClient.weatherChanged(context.client(), context.player(), payload.planetChanged, payload.fromPlanetName());
    }
}
