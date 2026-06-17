package com.cubikore.astro.networking.payload;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.joml.Vector4f;

public record SyncWorldOffsetPayload(Vector3f worldOffset, float yaw) implements CustomPayload {
    public static final Identifier RL = Identifier.of(AstroCraft.MOD_ID, "sync_offset");
    public static final CustomPayload.Id<SyncWorldOffsetPayload> ID = new CustomPayload.Id<>(RL);

    public static final PacketCodec<RegistryByteBuf, SyncWorldOffsetPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.VECTOR3F, SyncWorldOffsetPayload::worldOffset,
            PacketCodecs.FLOAT, SyncWorldOffsetPayload::yaw,
            SyncWorldOffsetPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void S2C(SyncWorldOffsetPayload payload, ClientPlayNetworking.Context context) {
        Vector3f worldOffset3d = payload.worldOffset();
        float yaw = payload.yaw();

        Vector4f worldOffset = new Vector4f(worldOffset3d.x, worldOffset3d.y, worldOffset3d.z, yaw);

        ClientStorage.renderedWorldOffset.set(worldOffset);
    }
}
