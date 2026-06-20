package com.cubikore.astro.networking.payload;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.ship.AstroCraftShip;
import com.cubikore.astro.util.PlayerComponentAccess;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.UUID;

public record PlayerFlashlightPayload(UUID playerUuid, boolean val) implements CustomPayload {
    public static final Identifier RL = Identifier.of(AstroCraft.MOD_ID, "player_flashlight");
    public static final Id<PlayerFlashlightPayload> ID = new Id<>(RL);

    public static final PacketCodec<RegistryByteBuf, PlayerFlashlightPayload> CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC, PlayerFlashlightPayload::playerUuid,
            PacketCodecs.BOOL, PlayerFlashlightPayload::val,
            PlayerFlashlightPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void C2S(PlayerFlashlightPayload payload, ServerPlayNetworking.Context context) {
        PlayerEntity player = context.player();
        PlayerComponentAccess access = (PlayerComponentAccess) player;

        access.setFlashlightOn(!access.isFlashlightOn());

        for(ServerPlayerEntity serverPlayer : PlayerLookup.all(context.server())) {
            ServerPlayNetworking.send(serverPlayer, new PlayerFlashlightPayload(player.getUuid(), access.isFlashlightOn()));
        }
    }

    public static void S2C(PlayerFlashlightPayload payload, ClientPlayNetworking.Context context) {
        UUID playerUuid = payload.playerUuid;
        boolean val = payload.val;

        PlayerEntity player = context.client().world.getPlayerByUuid(playerUuid);

        if(player != null) {
            PlayerComponentAccess access = (PlayerComponentAccess) player;
            access.setFlashlightOn(val);
        }
    }
}
