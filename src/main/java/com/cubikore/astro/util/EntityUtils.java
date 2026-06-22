package com.cubikore.astro.util;

import com.cubikore.astro.item.AstroCraftItems;
import com.cubikore.astro.mixin.PlayerEntityModelAccessor;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityUtils {
    public static EquipmentSlot getSlotByName(String name) {
        return switch (name) {
            case "head" -> EquipmentSlot.HEAD;
            case "chest" -> EquipmentSlot.CHEST;
            case "legs" -> EquipmentSlot.LEGS;
            case "feet" -> EquipmentSlot.FEET;
            default -> EquipmentSlot.MAINHAND;
        };
    }

    public static boolean isWearingFullSetSpaceSuit(LivingEntity entity) {
        return entity.getEquippedStack(EquipmentSlot.HEAD).isOf(AstroCraftItems.SPACE_SUIT_HELMET)
                && entity.getEquippedStack(EquipmentSlot.CHEST).isOf(AstroCraftItems.SPACE_SUIT_CHESTPLATE)
                && entity.getEquippedStack(EquipmentSlot.LEGS).isOf(AstroCraftItems.SPACE_SUIT_LEGGINGS)
                && entity.getEquippedStack(EquipmentSlot.FEET).isOf(AstroCraftItems.SPACE_SUIT_BOOTS);
    }

    public static void hidePlayerPartsWithSuit(PlayerEntityModel<AbstractClientPlayerEntity> playerModel, PlayerEntity player) {
        if(player.getEquippedStack(EquipmentSlot.HEAD).isOf(AstroCraftItems.SPACE_SUIT_HELMET))
            playerModel.hat.visible = false;
        if(player.getEquippedStack(EquipmentSlot.CHEST).isOf(AstroCraftItems.SPACE_SUIT_CHESTPLATE)) {
            PlayerEntityModelAccessor accessor = (PlayerEntityModelAccessor) playerModel;
            playerModel.jacket.visible = false;
            playerModel.leftSleeve.visible = false;
            playerModel.rightSleeve.visible = false;
            accessor.getCloak().visible = false;
        }
        if(player.getEquippedStack(EquipmentSlot.LEGS).isOf(AstroCraftItems.SPACE_SUIT_LEGGINGS)) {
            playerModel.leftPants.visible = false;
            playerModel.rightPants.visible = false;
        }
    }
}
