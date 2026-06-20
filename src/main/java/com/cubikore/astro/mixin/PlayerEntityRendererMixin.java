package com.cubikore.astro.mixin;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.light.SpotLight;
import com.cubikore.astro.dimension.DimensionKeys;
import com.cubikore.astro.util.PlayerComponentAccess;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
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

            PlayerComponentAccess access = (PlayerComponentAccess) clientPlayerEntity;
            LightRenderHandle<PointLightData> handle = access.getPointLightHandle();

            World world = clientPlayerEntity.getWorld();

            if(!clientPlayerEntity.isRemoved() && world != null) {
                if(handle == null || !handle.isValid()) {
                    PointLightData light = new PointLightData();
                    light.setBrightness(1);
                    light.setRadius(10);
                    light.setPosition(0, 0, 0);

                    access.setPointLightHandle(VeilRenderSystem.renderer().getLightRenderer().addLight(light));

                    SpotLight spotLight = new SpotLight();
                    spotLight.setRadius(20);
                    spotLight.setDistance(40f);
                    spotLight.setBrightness(4);

                    AstroCraftClient.renderer.addLight(spotLight);
                    access.setPlayerSpotLight(spotLight);
                }

                PointLightData lightData = access.getPointLightHandle().getLightData();

                if(client.player != clientPlayerEntity) {
                    Vec3d pos = clientPlayerEntity.getPos();
                    access.getPointLightHandle().getLightData().setPosition(pos.x, clientPlayerEntity.getEyeY(), pos.z);
                    access.getPlayerSpotLight().matchToView(clientPlayerEntity);
                }

                SpotLight flashLight = access.getPlayerSpotLight();
                if(flashLight != null) {
                    flashLight.setBrightness(access.isFlashlightOn() ? 4f : 0f);
                }

                if(!DimensionKeys.isSpace(world)) {
                    BlockPos pPos = clientPlayerEntity.getBlockPos();
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
        }
    }
}
