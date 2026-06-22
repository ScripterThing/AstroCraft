package com.cubikore.astro.networking.payload;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.components.AstComponents;
import com.cubikore.astro.ship.AstroCraftShip;
import com.cubikore.astro.util.EntityUtils;
import com.cubikore.astro.util.ItemUtils;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record ArmorCustomizationPayload(String piece, String color) implements CustomPayload {
    public static final Identifier RL = Identifier.of(AstroCraft.MOD_ID, "armor_customization");
    public static final Id<ArmorCustomizationPayload> ID = new Id<>(RL);

    public static final PacketCodec<RegistryByteBuf, ArmorCustomizationPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ArmorCustomizationPayload::piece,
            PacketCodecs.STRING, ArmorCustomizationPayload::color,
            ArmorCustomizationPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void C2S(ArmorCustomizationPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        String piece = payload.piece;
        String color = payload.color;

        EquipmentSlot slot = EntityUtils.getSlotByName(piece);
        ItemStack stack = player.getEquippedStack(slot);

        if(ItemUtils.isSpaceSuit(stack)) {
            stack.set(AstComponents.SUIT_COLOR_COMPONENT, color);
        }
    }
}
