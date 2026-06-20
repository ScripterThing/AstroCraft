package com.cubikore.astro.client.light;

import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.math.AstMath;
import com.cubikore.astro.util.CameraUtils;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.AreaLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class SpotLight {
    private long lastTime = System.currentTimeMillis();

    private Vector3f prevPosition;
    private Vector2f prevRotation;
    private Vector3f position;
    private Vector2f rotation;
    private Vector3f targetPosition;
    private Vector2f targetRotation;

    private float radius;
    private float distance;
    private float brightness;
    private float angle;

    private boolean removed = false;

    LightRenderHandle<AreaLightData> areaLightHandle;

    private Camera dummyCam;

    public SpotLight() {
        this(new Vector3f(0), new Vector2f(0), 3, 10, 1);
    }

    public SpotLight(Vector3f position, Vector2f rotation, float radius, float distance, float brightness) {
        this.position = position;
        this.rotation = rotation;
        this.prevPosition = new Vector3f(position);
        this.prevRotation = new Vector2f(rotation);
        this.targetPosition = new Vector3f(position);
        this.targetRotation = new Vector2f(rotation);
        this.radius = radius;
        this.distance = distance;
        this.brightness = brightness;

        updateLight();
    }

    public void matchToView(PlayerEntity player) {
        Vec3d playerPos = player.getPos();
        Vector3f playerEyePos = new Vector3f((float) playerPos.x, (float) player.getEyeY(), (float) playerPos.z);
        Vector2f playerRot = new Vector2f(player.getPitch(), player.getYaw());

        this.setPosition(playerEyePos);
        this.setRotation(playerRot);

        updateLight();
    }

    public void matchToView(Camera camera) {
        Vec3d camPos = camera.getPos();
        Vector3f playerEyePos = new Vector3f((float) camPos.x, (float) camPos.y, (float) camPos.z);
        Vector2f playerRot = new Vector2f(camera.getPitch(), camera.getYaw());

        this.setPosition(playerEyePos);
        this.setRotation(playerRot);

        updateLight();
    }

    private void updateLight() {
        if(isRemoved())
            return;

        if(dummyCam == null)
            dummyCam = new Camera();

        if(areaLightHandle == null || !areaLightHandle.isValid()) {
            AreaLightData lightData = new AreaLightData();
            lightData.setSize(0.1, 0.1);

            areaLightHandle = VeilRenderSystem.renderer().getLightRenderer().addLight(lightData);
        }

        float dt = getDeltaTime();
        float k = 20.0f;

        float alpha = 1f - (float)Math.exp(-k * dt);

        position.set(AstMath.lerp(prevPosition, targetPosition, alpha));
        rotation.set(AstMath.lerp(prevRotation, targetRotation, alpha));

        prevPosition.set(position);
        prevRotation.set(rotation);

        calcAngleRad();

        AreaLightData lightData = areaLightHandle.getLightData();

        CameraUtils.setCam(dummyCam, this.position, this.rotation.x, this.rotation.y);

        lightData.setBrightness(this.brightness / 2);
        lightData.setDistance(this.distance);
        lightData.setAngle(this.angle);
        lightData.setTo(dummyCam);
    }

    public boolean isRemoved() {
        return removed;
    }

    public void remove() {
        removed = true;

        areaLightHandle.free();
        areaLightHandle = null;
        dummyCam = null;
    }

    private void calcAngleRad() {
        this.angle = (float) Math.atan(radius / distance);
    }

    public float getAngle() {
        return this.angle;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.targetPosition = position;
        updateLight();
    }

    public Vector2f getRotation() {
        return rotation;
    }

    public void setRotation(Vector2f rotation) {
        this.targetRotation = rotation;
        updateLight();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        updateLight();
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
        updateLight();
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
        updateLight();
    }

    private float getDeltaTime() {
        float d = (float)(System.currentTimeMillis() - lastTime) / 1000;
        lastTime = System.currentTimeMillis();
        return d;
    }
}
