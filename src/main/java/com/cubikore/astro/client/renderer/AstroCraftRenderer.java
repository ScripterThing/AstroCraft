package com.cubikore.astro.client.renderer;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.block.AstroCraftBlocks;
import com.cubikore.astro.client.light.SpotLight;
import com.cubikore.astro.client.renderer.post.AstroCraftPostProcessingManager;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import foundry.veil.api.event.VeilRenderLevelStageEvent;
import foundry.veil.platform.VeilEventPlatform;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AstroCraftRenderer {
    public static final Identifier PARTICLE_LAYER_P = Identifier.of(AstroCraft.MOD_ID, "astparticle");
    public static final Identifier UNLIT_LAYER = Identifier.of(AstroCraft.MOD_ID, "unlit");
    public static final Identifier AST_G_LAYER = Identifier.of(AstroCraft.MOD_ID, "ast_info");

    private final AstroCraftPostProcessingManager postProcessingManager;
    private final AstroCraftHudRenderer hudRenderer;

    private boolean shaderLightsDirty = false;

    public List<SpotLight> spotLights = new ArrayList<>();

    private Camera camera;

    public AstroCraftRenderer() {
        postProcessingManager = new AstroCraftPostProcessingManager(this);
        hudRenderer = new AstroCraftHudRenderer(this);
    }

    public void init() {
        initEvents();

        postProcessingManager.init();
    }

    public void addLight(SpotLight spotLight) {
        spotLights.add(spotLight);
        markLightsDirty();
    }

    public void removeLights() {
        spotLights.clear();
        markLightsDirty();
    }

    public void clearRemovedLights() {
        spotLights.removeIf(SpotLight::isRemoved);
        markLightsDirty();
    }

    public Camera getCamera() {
        return this.camera;
    }

    public AstroCraftHudRenderer getHudRenderer() {
        return hudRenderer;
    }

    public void markLightsDirty() {
        shaderLightsDirty = true;
    }

    public void markLightsClean() {
        shaderLightsDirty = false;
    }

    public boolean lightsDirty() {
        return shaderLightsDirty;
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
