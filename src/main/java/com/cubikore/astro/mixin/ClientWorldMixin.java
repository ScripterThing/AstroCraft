package com.cubikore.astro.mixin;

import com.cubikore.astro.util.PlayerComponentAccess;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(at = @At("HEAD"), method = "removeEntity")
    private void thisRemoved(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
        ClientWorld world = (ClientWorld)(Object)this;
        Entity entity = world.getEntityById(entityId);

        if(entity instanceof PlayerEntity player) {
            PlayerComponentAccess access = (PlayerComponentAccess) player;
            access.removeLights();
        }
    }
}
