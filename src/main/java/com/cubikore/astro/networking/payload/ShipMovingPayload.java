package com.cubikore.astro.networking.payload;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.ship.AstroCraftShip;
import com.cubikore.astro.util.CommonStorage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.joml.Vector4f;

public record ShipMovingPayload(float x, float y, float z, float yaw, boolean shipMoving) implements CustomPayload {
    public static final Identifier RL = Identifier.of(AstroCraft.MOD_ID, "ship_moving");
    public static final Id<ShipMovingPayload> ID = new Id<>(RL);

    public static final PacketCodec<RegistryByteBuf, ShipMovingPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, ShipMovingPayload::x,
            PacketCodecs.FLOAT, ShipMovingPayload::y,
            PacketCodecs.FLOAT, ShipMovingPayload::z,
            PacketCodecs.FLOAT, ShipMovingPayload::yaw,
            PacketCodecs.BOOL, ShipMovingPayload::shipMoving,
            ShipMovingPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void C2S(ShipMovingPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();

        if (player.hasVehicle() && CommonStorage.captainSeatEntities.containsKey(player.getVehicle().getBlockPos()) && !AstroCraftShip.autoThrust) {
            AstroCraftShip.moveShip(new Vector4f(payload.x, payload.y, payload.z, payload.yaw));
        }
    }

    public static void S2C(ShipMovingPayload payload, ClientPlayNetworking.Context context) {
        if(!ClientStorage.doingFTLJump) {
            float yaw = payload.yaw;
            Vector4f target = new Vector4f(payload.x(), payload.y(), payload.z(), yaw);
            AstroCraftClient.clientGameManager.targetOffsetPos.set(target);
        }
    }
}
