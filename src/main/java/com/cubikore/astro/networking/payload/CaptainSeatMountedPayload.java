package com.cubikore.astro.networking.payload;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.util.CommonStorage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record CaptainSeatMountedPayload(BlockPos blockPos, int entityId) implements CustomPayload {
    public static final Identifier RL = Identifier.of(AstroCraft.MOD_ID, "captain_seat_mounted");
    public static final Id<CaptainSeatMountedPayload> ID = new Id<>(RL);

    public static final PacketCodec<RegistryByteBuf, CaptainSeatMountedPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, CaptainSeatMountedPayload::blockPos,
            PacketCodecs.INTEGER, CaptainSeatMountedPayload::entityId,
            CaptainSeatMountedPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void C2S(CaptainSeatMountedPayload payload, ServerPlayNetworking.Context context) {

    }

    public static void S2C(CaptainSeatMountedPayload payload, ClientPlayNetworking.Context context) {
        BlockPos pos = payload.blockPos();
        int seatId = payload.entityId();

        Entity e = context.client().world.getEntityById(seatId);

        if(e instanceof DisplayEntity.TextDisplayEntity entity) {
            CommonStorage.captainSeatEntities.put(pos, entity);
        }
    }
}
