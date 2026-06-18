package com.cubikore.astro.networking.payload;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.ship.AstroCraftShip;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.joml.Vector4f;

public record ShipCommandPayload(String commandType, String param) implements CustomPayload {
    public static final Identifier RL = Identifier.of(AstroCraft.MOD_ID, "ship_command");
    public static final Id<ShipCommandPayload> ID = new Id<>(RL);

    public static final PacketCodec<RegistryByteBuf, ShipCommandPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ShipCommandPayload::commandType,
            PacketCodecs.STRING, ShipCommandPayload::param,
            ShipCommandPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void C2S(ShipCommandPayload payload, ServerPlayNetworking.Context context) {
        String command = payload.commandType();
        String param = payload.param();

        if(command.equals("ftl_jump")) {
            AstroCraftShip.doFTLJump(context.server(), Identifier.tryParse(param));
        }
        else if(command.equals("auto_thrust")) {
            AstroCraftShip.toggleAutoThrust();
            context.player().sendMessage(Text.literal("Auto Thrust " + (AstroCraftShip.autoThrust ? "enabled" : "disabled")), true);
        }
    }

    public static void S2C(ShipCommandPayload payload, ClientPlayNetworking.Context context) {

    }
}
