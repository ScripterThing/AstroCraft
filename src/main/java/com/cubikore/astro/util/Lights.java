package com.cubikore.astro.util;

import com.cubikore.astro.mixin.CameraAccessor;
import foundry.veil.api.client.render.light.data.AreaLightData;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Lights {
    public static void setToView(PlayerEntity player, Camera camera, AreaLightData light) {
        Vec3d pos = player.getPos();
        CameraAccessor accessor = (CameraAccessor) camera;

        accessor.setCamPos(new Vec3d(pos.x, player.getEyeY(), pos.z));
        accessor.invokeSetRotation(player.getYaw(), player.getPitch());

        light.setTo(camera);
    }

    public static void setToView(Camera mainCam, Camera camera, AreaLightData light) {
        Vec3d pos = mainCam.getPos();
        CameraAccessor accessor = (CameraAccessor) camera;

        accessor.setCamPos(new Vec3d(pos.x, pos.y, pos.z));
        accessor.invokeSetRotation(mainCam.getYaw(), mainCam.getPitch());

        light.setTo(camera);
    }
}
