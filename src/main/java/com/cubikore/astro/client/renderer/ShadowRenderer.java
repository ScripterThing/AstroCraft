package com.cubikore.astro.client.renderer;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.mixin.WorldRendererAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.framebuffer.AdvancedFbo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class ShadowRenderer {
    public static Matrix4f lightSpaceMatrix = new Matrix4f();

    private static boolean renderingShadowMap = false;

    public static void render(MinecraftClient client, Camera camera, MatrixStack matrices, VertexConsumerProvider.Immediate bufferSource, float tickDelta) {
        Vec3d camPos = camera.getPos();
        WorldRendererAccessor accessor = (WorldRendererAccessor) client.worldRenderer;

        MatrixStack shadowModelView = createShadowModelView(camPos.x, camPos.y, camPos.z);
        Matrix4f shadowProjMat = createProjMat();
        Matrix4f backUpProjMat = RenderSystem.getProjectionMatrix();

        int width = client.getFramebuffer().textureWidth;
        int height = client.getFramebuffer().textureHeight;
        Frustum oldFrustum = accessor.getFrustum();
        Frustum frustum;

        AdvancedFbo shadowMap = VeilRenderSystem.renderer().getFramebufferManager().getFramebuffer(Identifier.of(AstroCraft.MOD_ID, "shadowmap"));
        if(shadowMap != null) {
            shadowMap.bind(true);
            setRenderingShadowMap(true);

            RenderSystem.setProjectionMatrix(shadowProjMat, VertexSorter.BY_Z);
            frustum = new Frustum(shadowModelView.peek().getPositionMatrix(), shadowProjMat);
            frustum.setPosition(camPos.x, camPos.y, camPos.z);
            accessor.setFrustum(frustum);
            accessor.invokeApplyFrustum(frustum);

            client.chunkCullingEnabled = false;

            ((WorldRenderer) accessor).scheduleTerrainUpdate();
            accessor.invokeSetUpTerrain(camera, frustum, false, false);

            RenderSystem.disableCull();

            accessor.invokeRenderLayer(RenderLayer.getSolid(), camPos.x, camPos.y, camPos.z, shadowModelView.peek().getPositionMatrix(), shadowProjMat);
            accessor.invokeRenderLayer(RenderLayer.getCutoutMipped(), camPos.x, camPos.y, camPos.z, shadowModelView.peek().getPositionMatrix(), shadowProjMat);
            accessor.invokeRenderLayer(RenderLayer.getCutout(), camPos.x, camPos.y, camPos.z, shadowModelView.peek().getPositionMatrix(), shadowProjMat);

            if(client.world != null) {
                VertexConsumerProvider.Immediate immediate = accessor.getBufferBuilders().getEntityVertexConsumers();

                for(Entity entity : client.world.getEntities()) {
                    if(accessor.getEntityRenderDispatcher().shouldRender(entity, accessor.getFrustum(), camPos.x, camPos.y, camPos.z) || entity.isSpectator()) {
                        accessor.invokeRenderEntity(entity, camPos.x, camPos.y, camPos.z, tickDelta, shadowModelView, immediate);
                    }
                }

                immediate.draw();
            }

            client.chunkCullingEnabled = true;

            RenderSystem.enableCull();

            setRenderingShadowMap(false);
            AdvancedFbo.unbind();
            RenderSystem.viewport(0, 0, width, height);
            RenderSystem.setProjectionMatrix(backUpProjMat, VertexSorter.BY_DISTANCE);
            accessor.setFrustum(oldFrustum);
            accessor.invokeApplyFrustum(oldFrustum);

            lightSpaceMatrix = new Matrix4f(shadowProjMat).mul(shadowModelView.peek().getPositionMatrix());
        }
    }

    public static MatrixStack createShadowModelView(double camX, double camY, double camZ) {
        MatrixStack shadowModelView = new MatrixStack();

        shadowModelView.peek().getNormalMatrix().identity();
        shadowModelView.peek().getPositionMatrix().identity();

        shadowModelView.peek().getPositionMatrix().translate(0.0f, 0.0f, -100.0f);
        rotateShadowModelView(shadowModelView.peek().getPositionMatrix());

        float offsetX = (float) camX % 2.0f;
        float offsetY = (float) camY % 2.0f;
        float offsetZ = (float) camZ % 2.0f;

        float halfIntervalSize = 1.0f;

        offsetX -= halfIntervalSize;
        offsetY -= halfIntervalSize;
        offsetZ -= halfIntervalSize;

        shadowModelView.peek().getPositionMatrix().translate(offsetX, offsetY, offsetZ);

        return shadowModelView;
    }

    public static void rotateShadowModelView(Matrix4f shadowModelView){
        MinecraftClient client = MinecraftClient.getInstance();

        //Comment these if shadows are following the sun
        shadowModelView.rotateX((float) Math.toRadians(ClientStorage.sunOrientation[0]));
        shadowModelView.rotateY((float) Math.toRadians(ClientStorage.sunOrientation[1]));

        //Un-comment these to make the shadows follow the sun
//        shadowModelView.rotate(Axis.XP.rotationDegrees(80.0F));
//        float j = Mth.sin(client.level.getSunAngle(tickDelta));
//        shadowModelView.rotate(Axis.ZN.rotationDegrees((float)Math.toDegrees(j)));
    }

    public static MatrixStack shadowModelView(Camera camera) {
        return createShadowModelView(camera.getPos().x, camera.getPos().y, camera.getPos().z);
    }

    public static Matrix4f createProjMat(){
        return orthographicMatrix(160, 0.05f, 256.0f);
    }

    public static Matrix4f orthographicMatrix(float halfPlaneLength, float nearPlane, float farPlane) {
        return new Matrix4f(
                1.0f / halfPlaneLength, 0f, 0f, 0f,
                0f, 1.0f / halfPlaneLength, 0f, 0f,
                0f, 0f, 2.0f / (nearPlane - farPlane), 0f,
                0f, 0f, -(farPlane + nearPlane) / (farPlane - nearPlane), 1f
        );
    }

    public static Matrix4f getLightSpaceMatrix() {
        return lightSpaceMatrix;
    }

    public static boolean isRenderingShadowMap() {
        return renderingShadowMap;
    }

    public static void setRenderingShadowMap(boolean l) {
        renderingShadowMap = l;
    }
}
