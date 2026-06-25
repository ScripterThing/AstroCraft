package com.cubikore.astro.mixin;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.renderer.AstroCraftHudRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Final
    @Shadow
    private LayeredDrawer layeredDrawer;

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(at = @At("HEAD"), method = "renderMiscOverlays")
    private void doThing(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        AstroCraftHudRenderer hudRenderer = AstroCraftClient.renderer.getHudRenderer();
        hudRenderer.renderHud(context, tickCounter, this.client);
    }

    @Inject(at = @At("HEAD"), method = "renderVignetteOverlay", cancellable = true)
    private void doThing2(DrawContext context, @Nullable Entity entity, CallbackInfo ci) {
        ci.cancel();
    }
}
