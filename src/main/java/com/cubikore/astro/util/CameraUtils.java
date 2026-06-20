package com.cubikore.astro.util;

import com.cubikore.astro.mixin.CameraAccessor;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public class CameraUtils {
    public static void setCamPos(Camera camera, Vector3f position) {
        setCam(camera, position, camera.getPitch(), camera.getYaw());
    }

    public static void setCamRot(Camera camera, float pitch, float yaw) {
        Vec3d pos = camera.getPos();
        setCam(camera, new Vector3f((float) pos.x, (float) pos.y, (float) pos.z), pitch, yaw);
    }

    public static void setCam(Camera camera, Vector3f position, float pitch, float yaw) {
        CameraAccessor accessor = (CameraAccessor) camera;

        accessor.setCamPos(new Vec3d(position.x, position.y, position.z));
        accessor.invokeSetRotation(yaw, pitch);
    }
}
