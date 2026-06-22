package com.cubikore.astro.mixin;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.client.PlayerBobbing;
import com.cubikore.astro.client.renderer.ShadowRenderer;
import com.cubikore.astro.math.AstMath;
import foundry.veil.api.client.render.VeilRenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Unique private static final Identifier shadowSolid = Identifier.of(AstroCraft.MOD_ID, "shadowmap/rendertype_solid");
    @Unique private static final Identifier shadowEntity = Identifier.of(AstroCraft.MOD_ID, "shadowmap/rendertype_entity");

    @Final
    @Shadow
    MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "renderHand")
    private void handlerHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        ClientStorage.renderingFpsHand = true;
    }

    @Inject(at = @At("RETURN"), method = "renderHand")
    private void handlerhandlerhandler(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        ClientStorage.renderingFpsHand = false;
    }

    @Inject(at = @At("HEAD"), method = "bobView", cancellable = true)
    private void changeBobbing(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if(this.client.cameraEntity instanceof PlayerEntity playerEntity) {
            PlayerBobbing.doBobbing(matrices, this.client, playerEntity, tickDelta);
        }

        ci.cancel();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void doInterpolation(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
    }

    @Inject(method = {
            "getRenderTypeSolidProgram",
            "getRenderTypeCutoutProgram",
            "getRenderTypeCutoutMippedProgram"
    }, at = @At("HEAD"), cancellable = true)
    private static void setSolidShader(CallbackInfoReturnable<ShaderProgram> cir) {
        if(ShadowRenderer.isRenderingShadowMap()) {
            foundry.veil.api.client.render.shader.program.ShaderProgram shader = VeilRenderSystem.setShader(shadowSolid);
            if (shader == null) {
                return;
            }
            cir.setReturnValue(shader.toShaderInstance());
        }
    }

    @Inject(method = {
            "getRenderTypeEntityTranslucentProgram",
            "getRenderTypeEntitySolidProgram",
            "getRenderTypeEntityCutoutProgram",
            "getRenderTypeEntityCutoutNoNullProgram",
            "getRenderTypeEntityTranslucentCullProgram",
            "getRenderTypeArmorCutoutNoCullProgram"
    }, at = @At("TAIL"), cancellable = true)
    private static void setEntityShader(CallbackInfoReturnable<ShaderProgram> cir) {
        if(ShadowRenderer.isRenderingShadowMap()) {
            foundry.veil.api.client.render.shader.program.ShaderProgram shader = VeilRenderSystem.setShader(shadowEntity);
            if (shader == null) {
                return;
            }
            cir.setReturnValue(shader.toShaderInstance());
        }
    }
}
