package com.cubikore.astro.networking;

import com.cubikore.astro.networking.payload.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class AstroCraftNetworking {
    public static void registerC2SPayloads() {
        ServerPlayNetworking.registerGlobalReceiver(ShipMovingPayload.ID, ShipMovingPayload::C2S);
        ServerPlayNetworking.registerGlobalReceiver(CaptainSeatMountedPayload.ID, CaptainSeatMountedPayload::C2S);
        ServerPlayNetworking.registerGlobalReceiver(ShipCommandPayload.ID, ShipCommandPayload::C2S);
        ServerPlayNetworking.registerGlobalReceiver(FTLJumpPayload.ID, FTLJumpPayload::C2S);
    }

    public static void registerS2CPayloads() {
        ClientPlayNetworking.registerGlobalReceiver(ShipMovingPayload.ID, ShipMovingPayload::S2C);
        ClientPlayNetworking.registerGlobalReceiver(CaptainSeatMountedPayload.ID, CaptainSeatMountedPayload::S2C);
        ClientPlayNetworking.registerGlobalReceiver(ShipMovingPayload.ID, ShipMovingPayload::S2C);
        ClientPlayNetworking.registerGlobalReceiver(SyncWorldOffsetPayload.ID, SyncWorldOffsetPayload::S2C);
        ClientPlayNetworking.registerGlobalReceiver(WeatherChangedPayload.ID, WeatherChangedPayload::S2C);
        ClientPlayNetworking.registerGlobalReceiver(FTLJumpPayload.ID, FTLJumpPayload::S2C);
        ClientPlayNetworking.registerGlobalReceiver(ShipCommandPayload.ID, ShipCommandPayload::S2C);
    }

    public static void registerPayloads() {
        PayloadTypeRegistry.playC2S().register(ShipMovingPayload.ID, ShipMovingPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ShipMovingPayload.ID, ShipMovingPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(CaptainSeatMountedPayload.ID, CaptainSeatMountedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CaptainSeatMountedPayload.ID, CaptainSeatMountedPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncWorldOffsetPayload.ID, SyncWorldOffsetPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(WeatherChangedPayload.ID, WeatherChangedPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(FTLJumpPayload.ID, FTLJumpPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(FTLJumpPayload.ID, FTLJumpPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(ShipCommandPayload.ID, ShipCommandPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ShipCommandPayload.ID, ShipCommandPayload.CODEC);
    }
}
