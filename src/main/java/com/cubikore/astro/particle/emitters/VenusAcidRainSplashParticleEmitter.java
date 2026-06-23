package com.cubikore.astro.particle.emitters;

import com.cubikore.astro.math.VectorMath;
import com.cubikore.astro.sound.AstroCraftSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class VenusAcidRainSplashParticleEmitter extends AstParticleEmitter{
    private Random random;

    public VenusAcidRainSplashParticleEmitter(Vector3f position, int count, Random random, int[] color) {
        super(position, new Vector2f(0.25f), count, 3f, null);

        this.r = color[0];
        this.g = color[1];
        this.b = color[2];

        this.random = random;

        this.size = new Vector2f(0.025f);
    }

    @Override
    public void update(float deltaTime) {
        this.gravity = -5f;
        super.update(deltaTime);
        for(int i = 0; i < count; i++) {
            if (!MinecraftClient.getInstance().world.getBlockState(new BlockPos((int) x[i], (int) y[i], (int) z[i])).isAir()) {
                y[i] = (int) y[i] + 1;
                stopPhysicsCalculations(i);
            }
        }
    }

    @Override
    protected void init() {
        super.init();

        MinecraftClient client = MinecraftClient.getInstance();

        BlockPos pos = new BlockPos(new Vec3i((int) x[0], (int) y[0], (int) z[0]));

        client.getSoundManager().play(new PositionedSoundInstance(AstroCraftSounds.WATER_DROP, SoundCategory.AMBIENT, 0.1f, 1, client.world.getRandom(), pos));
    }

    @Override
    protected void initParticle(int i) {
        super.initParticle(i);

        float nx = (random.nextFloat() * 2.0f - 1.0f) * 10;
        float nz = (random.nextFloat() * 2.0f - 1.0f) * 10;

        float ny = 10f;

        this.lifetime[i] = 1f;

        float[] res = VectorMath.add(vx[i], vy[i], vz[i], nx, ny, nz);

        vx[i] = res[0];
        vy[i] = res[1];
        vz[i] = res[2];
    }
}
