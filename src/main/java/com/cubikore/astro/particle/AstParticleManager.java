package com.cubikore.astro.particle;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.mixin.ParticleManagerAccessor;
import com.cubikore.astro.particle.emitters.AstParticleEmitter;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class AstParticleManager {
    private List<AstParticleEmitter> particleEmitters = new ArrayList<>();

    private List<AstParticleEmitter> toAdd = new ArrayList<>();
    private List<AstParticleEmitter> toRemove = new ArrayList<>();

    private long lastTime = System.currentTimeMillis();

    public static int numParticles = 0;

    private SpriteAtlasTexture atlasTexture;

    public AstParticleManager(TextureManager textureManager) {
        ParticleManager manager = MinecraftClient.getInstance().particleManager;

        atlasTexture = ((ParticleManagerAccessor) manager).getAtlas();
    }

    public void updateParticles() {
        float deltaTime = getDeltaTime();

        for(AstParticleEmitter particleEmitter : particleEmitters) {
            if(particleEmitter.getNumDespawned() >= particleEmitter.count) {
                removeEmitter(particleEmitter);
            }

            if(MinecraftClient.getInstance().player != null) {
                particleEmitter.update(deltaTime);
            }
        }
    }

    public void addEmitter(AstParticleEmitter astParticleEmitter) {
        toAdd.add(astParticleEmitter);
    }

    public <T extends AstParticleEmitter>
    void removeEmitterType(Class<T> type) {
        particleEmitters.removeIf(type::isInstance);
    }

    public <T extends AstParticleEmitter>
    void stopEmitterType(Class<T> type) {
        for(AstParticleEmitter emitter : particleEmitters) {
            if(type.isInstance(emitter))
                emitter.stopEmitting();
        }
    }

    public void removeEmitter(AstParticleEmitter astParticleEmitter) {
        toRemove.add(astParticleEmitter);
    }

    public void clearParticles() {
        toRemove.addAll(particleEmitters);
    }

    public void renderParticles(Camera camera, MatrixStack matrices, VertexConsumer buffer) {
        updateParticles();
        Vec3d camPos = camera.getPos();

        PlayerEntity player = MinecraftClient.getInstance().player;

        Vec3d playerPos = player.getPos();

        for(AstParticleEmitter particleEmitter : particleEmitters) {
            for(int i = 0; i < particleEmitter.count; i++) {
                if(particleEmitter.isDespawned(i))
                    continue;

                matrices.push();

                float[] particlePos = particleEmitter.getPos(i);

                double x = particlePos[0] - camPos.x;
                double y = particlePos[1] - camPos.y;
                double z = particlePos[2] - camPos.z;

                if (particleEmitter.relative) {
                    x += camPos.x;
                    //y += camPos.y;
                    z += camPos.z;
                }

                matrices.translate(x, y, z);

                Quaternionf rotation = camera.getRotation();

                if(!particleEmitter.billboardAxis[0]) {
                    rotation.x = 0;
                    rotation.normalize();
                }
                if(!particleEmitter.billboardAxis[1]) {
                    rotation.y = 0;
                    rotation.normalize();
                }
                if(!particleEmitter.billboardAxis[2]) {
                    rotation.z = 0;
                    rotation.normalize();
                }

                matrices.multiply(rotation);

                matrices.scale(particleEmitter.size.x * particleEmitter.scale, particleEmitter.size.y * particleEmitter.scale, 1.0f);

                MatrixStack.Entry entry = matrices.peek();
                Matrix4f positionMatrix = entry.getPositionMatrix();

                Vector3f particleWorldPos = particleEmitter.relative ? new Vector3f((float) (playerPos.x + particlePos[0]), (float) (playerPos.y + particlePos[1]), (float) (playerPos.z + particlePos[2])) : new Vector3f(particlePos[0], particlePos[1], particlePos[2]);

                int blockLight = MinecraftClient.getInstance().world.getLightLevel(
                        LightType.BLOCK,
                        BlockPos.ofFloored(particleWorldPos.x, particleWorldPos.y, particleWorldPos.z)
                );

                int skyLight = MinecraftClient.getInstance().world.getLightLevel(
                        LightType.SKY,
                        BlockPos.ofFloored(particleWorldPos.x, particleWorldPos.y, particleWorldPos.z)
                );

                int light = Math.min(skyLight + blockLight, 15);

                float skyBrightness = Math.max(light / 15.0f, 0.1f);

                int r = (int) (particleEmitter.r );
                int g = (int) (particleEmitter.g );
                int b = (int) (particleEmitter.b );

                Sprite sprite = atlasTexture.getSprite(particleEmitter.getSprite());

                float u0 = sprite.getMinU();
                float u1 = sprite.getMaxU();
                float v0 = sprite.getMinV();
                float v1 = sprite.getMaxV();

                emit(buffer, positionMatrix, -particleEmitter.scale, -particleEmitter.scale, r, g, b, u0, v1, LightmapTextureManager.MAX_LIGHT_COORDINATE);
                emit(buffer, positionMatrix, particleEmitter.scale, -particleEmitter.scale, r, g, b, u1, v1, LightmapTextureManager.MAX_LIGHT_COORDINATE);
                emit(buffer, positionMatrix, particleEmitter.scale, particleEmitter.scale, r, g, b, u1, v0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
                emit(buffer, positionMatrix, -particleEmitter.scale, particleEmitter.scale, r, g, b, u0, v0, LightmapTextureManager.MAX_LIGHT_COORDINATE);

                matrices.pop();
            }
        }

        particleEmitters.addAll(toAdd);
        particleEmitters.removeAll(toRemove);

        toAdd.clear();
        toRemove.clear();
    }

    private void emit(VertexConsumer buffer, Matrix4f positionMatrix, float posX, float posY, int r, int g, int b, float u, float v, int light) {
        buffer.vertex(positionMatrix, -posX, -posY, 0)
                .color(r, g, b, 255)
                .texture(u, v)
                .light(light)
                .normal(0, 1, 0);
    }

    private float getDeltaTime() {
        float d = (float)(System.currentTimeMillis() - lastTime) / 1000;
        lastTime = System.currentTimeMillis();
        MinecraftClient client = MinecraftClient.getInstance();
        if(client.isPaused())
            return 0;

        return d;
    }

    public int numSystems() {
        return particleEmitters.size();
    }
}
