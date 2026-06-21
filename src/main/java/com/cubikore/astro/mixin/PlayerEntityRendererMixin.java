package com.cubikore.astro.mixin;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.light.SpotLight;
import com.cubikore.astro.dimension.DimensionKeys;
import com.cubikore.astro.item.AstroCraftItems;
import com.cubikore.astro.util.EntityUtils;
import com.cubikore.astro.util.PlayerComponentAccess;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Unique
    private Camera dummyCam;

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    private void rendering(
            AbstractClientPlayerEntity clientPlayerEntity,
            float f,
            float g,
            MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider,
            int i,
            CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();

        if(client.player != null) {
            if(dummyCam == null)
                dummyCam = new Camera();

            World world = clientPlayerEntity.getWorld();

            if(!clientPlayerEntity.isRemoved() && world != null) {
                handlePlayerLight(client, clientPlayerEntity, world);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "setModelPose")
    private void handleSkinLayer(AbstractClientPlayerEntity player, CallbackInfo ci) {
        PlayerEntityRenderer playerEntityRenderer = ((PlayerEntityRenderer)(Object)this);

        PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = playerEntityRenderer.getModel();

        EntityUtils.hidePlayerPartsWithSuit(playerEntityModel, player);
    }

    private void handlePlayerLight(MinecraftClient client, PlayerEntity player, World world) {
        PlayerComponentAccess access = (PlayerComponentAccess) player;
        LightRenderHandle<PointLightData> handle = access.getPointLightHandle();

        if(handle == null || !handle.isValid()) {
            PointLightData light = new PointLightData();
            light.setBrightness(1);
            light.setRadius(10);
            light.setPosition(0, 0, 0);

            access.setPointLightHandle(VeilRenderSystem.renderer().getLightRenderer().addLight(light));

            SpotLight spotLight = new SpotLight();
            spotLight.setRadius(30);
            spotLight.setDistance(40f);
            spotLight.setBrightness(4);

            AstroCraftClient.renderer.addLight(spotLight);
            access.setPlayerSpotLight(spotLight);
        }

        PointLightData lightData = access.getPointLightHandle().getLightData();

        if(!client.options.getPerspective().isFirstPerson() || client.player != player) {
            Vec3d pos = player.getPos();
            access.getPointLightHandle().getLightData().setPosition(pos.x, player.getEyeY(), pos.z);
            access.getPlayerSpotLight().matchToView(player);
        }

        SpotLight flashLight = access.getPlayerSpotLight();
        if(flashLight != null) {
            flashLight.setBrightness(access.isFlashlightOn() && player.getEquippedStack(EquipmentSlot.HEAD).isOf(AstroCraftItems.SPACE_SUIT_HELMET) ? 4f : 0f);
        }

        if(!DimensionKeys.isSpace(world)) {
            BlockPos pPos = player.getBlockPos();
            BlockPos pos = new BlockPos(pPos.getX(), pPos.getY() + 1, pPos.getZ());

            int lightLevel = world.getBaseLightLevel(pos, 0);

            if(lightLevel <= 5)
                lightData.setBrightness(1);
            else
                lightData.setBrightness(0);
        }
        else {
            lightData.setBrightness(0);
        }
    }

    @Unique
    private static BipedEntityModel.ArmPose getArmPose(AbstractClientPlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            return BipedEntityModel.ArmPose.EMPTY;
        } else {
            if (player.getActiveHand() == hand && player.getItemUseTimeLeft() > 0) {
                UseAction useAction = itemStack.getUseAction();
                if (useAction == UseAction.BLOCK) {
                    return BipedEntityModel.ArmPose.BLOCK;
                }

                if (useAction == UseAction.BOW) {
                    return BipedEntityModel.ArmPose.BOW_AND_ARROW;
                }

                if (useAction == UseAction.SPEAR) {
                    return BipedEntityModel.ArmPose.THROW_SPEAR;
                }

                if (useAction == UseAction.CROSSBOW && hand == player.getActiveHand()) {
                    return BipedEntityModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useAction == UseAction.SPYGLASS) {
                    return BipedEntityModel.ArmPose.SPYGLASS;
                }

                if (useAction == UseAction.TOOT_HORN) {
                    return BipedEntityModel.ArmPose.TOOT_HORN;
                }

                if (useAction == UseAction.BRUSH) {
                    return BipedEntityModel.ArmPose.BRUSH;
                }
            } else if (!player.handSwinging && itemStack.isOf(Items.CROSSBOW) && CrossbowItem.isCharged(itemStack)) {
                return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
            }

            return BipedEntityModel.ArmPose.ITEM;
        }
    }
}
