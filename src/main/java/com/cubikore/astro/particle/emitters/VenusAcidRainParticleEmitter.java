package com.cubikore.astro.particle.emitters;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.util.VectorMath;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class VenusAcidRainParticleEmitter extends  AstParticleEmitter{
    private Random random = new Random();

    private net.minecraft.util.math.random.Random rand;

    private SimplexNoiseSampler noise;

    public VenusAcidRainParticleEmitter(Vector2f size, int count) {
        super(new Vector3f(0), size, count, Float.POSITIVE_INFINITY, null);

        rand = net.minecraft.util.math.random.Random.create(System.nanoTime());
        noise = new SimplexNoiseSampler(rand);

        this.r = 140;
        this.g = 255;
        this.b = 74;
    }

    @Override
    protected void initParticle(int i) {
        super.initParticle(i);

        this.drag = 0.1f;

        float[] nPos = calculateSpawnPos();

        x[i] = nPos[0];
        y[i] = nPos[1];
        z[i] = nPos[2];
    }

    @Override
    public void update(float deltaTime) {
        this.gravity = -7.2f;
        for(int i = 0; i < count; i++) {
            float strength = 1f;
            float div = 10f;

            float nx = (float) noise.sample(emitterAge / div, emitterAge / div, emitterAge / div);
            float nz = (float) noise.sample(emitterAge / div, emitterAge / div, emitterAge / div);

            float[] res = VectorMath.add(ax[i], ay[i], az[i], nx * strength, 0, nz * strength);

            ax[i] = res[0];
            ay[i] = res[1];
            az[i] = res[2];

            if (!MinecraftClient.getInstance().world.getBlockState(new BlockPos((int) x[i], (int) y[i], (int) z[i])).isAir()) {
                this.despawn(i);
            }
        }

        super.update(deltaTime);
    }

    private float[] calculateSpawnPos() {
        Vec3d plPos = MinecraftClient.getInstance().player.getPos();

        float[] playerPos = new float[]{(float) plPos.x, (float) plPos.y, (float) plPos.z};

        float nx = (random.nextFloat() * 2.0f - 1.0f) * 11f;
        float ny = 20 + (random.nextFloat() * 30f);
        float nz = (random.nextFloat() * 2.0f - 1.0f) * 11f;

        float[] nPos = new float[]{playerPos[0] + nx, playerPos[1] + ny, playerPos[2] + nz};
        nPos[1] = Math.max(120, nPos[1]);

        return nPos;
    }

    @Override
    protected void despawn(int i) {
        if(!emittingStopped) {
            if (random.nextFloat() <= 0.2f)
                AstroCraftClient.particleManager.addEmitter(new VenusAcidRainSplashParticleEmitter(new Vector3f(x[i], y[i] + 0.5f, z[i]), 1, random));

            float[] nPos = calculateSpawnPos();

            x[i] = nPos[0];
            y[i] = nPos[1];
            z[i] = nPos[2];

            this.age[i] = 0;
        }
        else
            super.despawn(i);
    }
}
