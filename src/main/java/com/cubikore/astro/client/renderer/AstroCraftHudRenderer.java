package com.cubikore.astro.client.renderer;

import com.cubikore.astro.client.input.AstroCraftKeyBinds;
import com.cubikore.astro.texture.AstrocraftTextures;
import com.cubikore.astro.util.EntityUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class AstroCraftHudRenderer {

    private final AstroCraftRenderer astroCraftRenderer;

    private TextRenderer textRenderer;

    private int screenWidth;
    private int screenHeight;

    public AstroCraftHudRenderer(AstroCraftRenderer astroCraftRenderer) {
        this.astroCraftRenderer = astroCraftRenderer;
    }

    public void renderHud(DrawContext context, RenderTickCounter tickCounter, MinecraftClient client) {
        int scale = client.options.getGuiScale().getValue();

        this.screenWidth = scale != 0 ? client.getFramebuffer().textureWidth / scale : 0;
        this.screenHeight = scale != 0 ? client.getFramebuffer().textureHeight / scale : 0;

        this.textRenderer = client.textRenderer;

        ClientPlayerEntity player = client.player;

        if(player != null) {
            if(EntityUtils.wearingSpaceSuit(player, EquipmentSlot.HEAD))
                renderHelmetHud(context, tickCounter, client);
        }
    }

    private void renderVignetteOverlay(DrawContext context, MinecraftClient client) {
        ClientPlayerEntity player = client.player;

        if(player != null && !EntityUtils.wearingSpaceSuit(player, EquipmentSlot.HEAD) || !client.options.getPerspective().isFirstPerson())
            return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO
        );

        context.drawTexture(
                AstrocraftTextures.HELMET_OVERLAY,
                0,
                0,
                -90,
                0.0F,
                0.0F,
                context.getScaledWindowWidth(),
                context.getScaledWindowHeight(),
                context.getScaledWindowWidth(),
                context.getScaledWindowHeight()
        );

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void renderHelmetHud(DrawContext context, RenderTickCounter counter, MinecraftClient client) {
        renderVignetteOverlay(context, client);

        Text flashlightKey = AstroCraftKeyBinds.FLASHLIGHT_KEY.getBoundKeyLocalizedText();
        Text flashlightText = Text.translatable("text.astrocraft.flashlight");

        String flashlightTipText = flashlightText.getString() + " (" + flashlightKey.getString() + ")";

        context.drawText(this.textRenderer, flashlightTipText, 4, this.screenHeight - 10, 0xffffffff, true);
    }
}
