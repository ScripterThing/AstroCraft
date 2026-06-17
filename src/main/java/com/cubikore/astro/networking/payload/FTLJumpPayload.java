package com.cubikore.astro.networking.payload;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.ship.AstroCraftShip;
import com.cubikore.astro.weather.PlanetWeather;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.joml.Vector4f;

public record FTLJumpPayload(Vector3f destination, float destinationYaw) implements CustomPayload {
    public static final Identifier RL = Identifier.of(AstroCraft.MOD_ID, "ftl_jump");
    public static final Id<FTLJumpPayload> ID = new Id<>(RL);

    public static final PacketCodec<RegistryByteBuf, FTLJumpPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VECTOR3F, FTLJumpPayload::destination,
            PacketCodecs.FLOAT, FTLJumpPayload::destinationYaw,
            FTLJumpPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void C2S(FTLJumpPayload payload, ServerPlayNetworking.Context context) {
        AstroCraftShip.clientConf++;
    }

    public static void S2C(FTLJumpPayload payload, ClientPlayNetworking.Context context) {
        Vector3f destPos = payload.destination();
        float destYaw = payload.destinationYaw();

        ClientStorage.FTLJumpDestination = new Vector4f(destPos.x, destPos.y, destPos.z, destYaw);
        AstroCraftClient.cutsceneManager.playScene(context.client(), "FTLJump");
    }
}
