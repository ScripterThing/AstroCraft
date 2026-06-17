package com.cubikore.astro.particle.emitters;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.util.VectorMath;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class VenusAtmosphereParticleEmitter extends AstParticleEmitter {
    private Random[] random = new Random[CAPACITY];

    private SimplexNoiseSampler[] noise = new SimplexNoiseSampler[CAPACITY];

    private java.util.Random[] rand = new java.util.Random[CAPACITY];

    float[] targetX;
    float[] targetY;
    float[] targetZ;

    private boolean[] justDespawned;

    private float speed;

    private static SimplexNoiseSampler windNoise = new SimplexNoiseSampler(Random.create(90));

    public VenusAtmosphereParticleEmitter(Vector2f size, float speed, int r, int g, int b, int count) {
        super(new Vector3f(0, 100, 0), size, count, Float.POSITIVE_INFINITY, null);

        this.speed = speed;

        this.gravity = 0;

        this.r = r;
        this.g = g;
        this.b = b;

        targetX = new float[CAPACITY];
        targetY = new float[CAPACITY];
        targetZ = new float[CAPACITY];

        justDespawned = new boolean[CAPACITY];

        for(int i = 0; i < count; i++) {
            rand[i] = new java.util.Random(id[i]);
            random[i] = Random.create(rand[i].nextLong());
            noise[i] = new SimplexNoiseSampler(random[i]);

            targetX[i] = 0;
            targetY[i] = 0;
            targetZ[i] = 0;

            justDespawned[i] = false;
        }
    }

    @Override
    protected void initParticle(int i) {
        super.initParticle(i);

        float[] targ = calculateInitPos(i);

        x[i] = targ[0];
        y[i] = targ[1];
        z[i] = targ[2];

        prevX[i] = targ[0];
        prevY[i] = targ[1];
        prevZ[i] = targ[2];

        vx[i] = 0;
        vy[i] = 0;
        vz[i] = 0;

        ax[i] = 0;
        ay[i] = 0;
        az[i] = 0;

        targetX[i] = targ[0];
        targetY[i] = targ[1];
        targetZ[i] = targ[2];
    }

    @Override
    public void update(float deltaTime) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        Vec3d playerPos = player.getPos();

        float eyeHeight = player.getEyeHeight(player.getPose());

        Vector3f pos = new Vector3f((float) playerPos.x, (float) (playerPos.y + eyeHeight), (float) playerPos.z);

        Vec3d vel = player.getVelocity();

        for(int i = 0; i < count; i++) {
            if(despawned[i]) {
                continue;
            }

            float strength = 0.1f * speed;

            float nx = (float) noise[i].sample(x[i], y[i], age[i]);
            float ny = (float) noise[i].sample(y[i], z[i], age[i] + 100);
            float nz = (float) noise[i].sample(x[i], z[i], age[i] + 200);

            float[] res = VectorMath.add(ax[i], ay[i], az[i], nx * strength, ny * strength, nz * strength);

            ax[i] = res[0];
            ay[i] = res[1];
            az[i] = res[2];

            if(emittingStopped) {
                ay[i] -= 0.01f;
            }

            if (pos.distance(x[i], y[i], z[i]) < 0.5) {
                float[] dir = VectorMath.sub(x[i], y[i], z[i], pos.x, pos.y, pos.z);
                dir = VectorMath.normalize(dir[0], dir[1], dir[2]);
                dir = VectorMath.mul(dir[0], dir[1], dir[2], 0.5f);

                float[] res1 = VectorMath.add(vx[i], vy[i], vz[i], dir[0], dir[1], dir[2]);

                vx[i] = res1[0];
                vy[i] = res1[1];
                vz[i] = res1[2];
            }

//            if(Math.abs(y[i] - pos.y) > 20) {
//                if(y[i] > pos.y) {
//                    y[i] = pos.y - 20;
//                }
//                else {
//                    y[i] = pos.y + 20;
//                }
//            }

            if (playerPos.y > 64) {
                double frequency = 0.03;

                float windX = (float) windNoise.sample((AstroCraftClient.getGameTime() * frequency) , (AstroCraftClient.getGameTime() * frequency), (AstroCraftClient.getGameTime() * frequency));
                float windZ = (float) windNoise.sample((AstroCraftClient.getGameTime() + 856 * frequency), (AstroCraftClient.getGameTime() + 893 * frequency), (AstroCraftClient.getGameTime() - 254 * frequency));

                float windStrength = (0.1f * speed) * ClientStorage.windStrength;

                float[] res1 = VectorMath.add(ax[i], ay[i], az[i], windX * windStrength, 0, windZ * windStrength);

                ax[i] = res1[0];
                ay[i] = res1[1];
                az[i] = res1[2];
            }
            float[] dir = VectorMath.mul((float)vel.x, (float)vel.y, (float)vel.z, 0.01f);
            float[] res1 = VectorMath.add(vx[i], vy[i], vz[i], dir[0], 0, dir[2]);

            vx[i] = res1[0];
            vy[i] = res1[1];
            vz[i] = res1[2];

            vy[i] -= 0.003f;

            float[] center = new float[]{(float) playerPos.x, (float) playerPos.y, (float) playerPos.z};

            float dst = VectorMath.distance(x[i], y[i], z[i], center[0], center[1], center[2]);

            float rad = 30f;

            if(dst > rad && !emittingStopped) {
                float[] dirToP = VectorMath.sub(x[i], y[i], z[i], center[0], center[1], center[2]);
                dirToP = VectorMath.normalize(dirToP[0], dirToP[1], dirToP[2]);

                float[] distFC = VectorMath.mul(dirToP[0], dirToP[1], dirToP[2], rad - 5);
                float[] newP = VectorMath.sub(center[0], center[1], center[2], distFC[0], distFC[1], distFC[2]);

                x[i] = newP[0];
                y[i] = newP[1];
                z[i] = newP[2];
            }
        }

        super.update(deltaTime);
    }
    private float[] calculateInitPos(int i) {
        float x = (rand[i].nextFloat() * 2.0f - 1.0f) * 30;
        float y = (rand[i].nextFloat() * 2.0f - 1.0f) * 30;
        float z = (rand[i].nextFloat() * 2.0f - 1.0f) * 30;

        Vec3d playerPos = MinecraftClient.getInstance().player.getPos();
        //return new Vector3f(x, (float) (playerPos.y + y), z);
        return new float[]{(float) (playerPos.x + x), (float) (playerPos.y + y), (float) (playerPos.z + z)};
    }

    private float[] calculateTargetPos(int i) {
        float x = (rand[i].nextFloat() * 2.0f - 1.0f) * 300;
        float y = (rand[i].nextFloat() * 100) - 40;
        float z = (rand[i].nextFloat() * 2.0f - 1.0f) * 300;

        Vec3d playerPos = MinecraftClient.getInstance().player.getPos();
        //return new Vector3f(x, (float) (playerPos.y + y), z);
        return new float[]{(float) (playerPos.x + x), (float) (playerPos.y + y), (float) (playerPos.z + z)};
    }

    @Override
    protected void stopped() {
        super.stopped();
        for(int i = 0; i < count; i++) {
            lifetime[i] = 6f;
            age[i] = 0;
        }
    }

    @Override
    protected void despawn(int i) {
        if(!emittingStopped) {
            float[] targ = calculateTargetPos(i);

            targetX[i] = targ[0];
            targetY[i] = targ[1];
            targetZ[i] = targ[2];

            this.age[i] = 0;
            this.justDespawned[i] = true;
        }
        else
            super.despawn(i);
    }
}
