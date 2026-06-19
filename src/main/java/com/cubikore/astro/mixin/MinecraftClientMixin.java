package com.cubikore.astro.mixin;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.particle.AstParticleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private ReloadableResourceManagerImpl resourceManager;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void doInterpolation(RunArgs runArgs, CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        AstroCraftClient.clientGameManager.particleManager = new AstParticleManager(client.getTextureManager());
    }
}
