package com.cubikore.astro.client.renderer;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.renderer.post.AstroCraftPostProcessingManager;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.platform.VeilEventPlatform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class AstroCraftRenderer {
    public static final Identifier PARTICLE_LAYER_P = Identifier.of(AstroCraft.MOD_ID, "astparticle");
    public static final Identifier UNLIT_LAYER = Identifier.of(AstroCraft.MOD_ID, "unlit");

    private final AstroCraftPostProcessingManager postProcessingManager;

    private Camera camera;

    public AstroCraftRenderer() {
        postProcessingManager = new AstroCraftPostProcessingManager(this);
    }

    public void init() {
        initEvents();

        postProcessingManager.init();
    }

    public Camera getCamera() {
        return this.camera;
    }

    private void initEvents() {
        VeilEventPlatform.INSTANCE.onVeilRenderLevelStage(((stage, levelRenderer, bufferSource, matrixStack, frustumMatrix, projectionMatrix, renderTick, deltaTracker, camera, frustum) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if(this.camera == null) {
                this.camera = camera;
            }

            if(stage == VeilRenderLevelStageEvent.Stage.AFTER_SKY) {
                if (camera != null) {
                    ShadowRenderer.render(client, camera, (MatrixStack) matrixStack, bufferSource, deltaTracker.getTickDelta(true));
                }
            }

            if(stage == VeilRenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                if(camera != null) {
                    RenderLayer layer = VeilRenderType.get(AstroCraftRenderer.PARTICLE_LAYER_P);
                    AstroCraftClient.clientGameManager.particleManager.renderParticles(camera, matrixStack.toPoseStack(), bufferSource.getBuffer(layer));
                }
            }
        }));
    }
}
