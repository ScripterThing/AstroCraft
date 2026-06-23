package com.cubikore.astro.particle.emitters;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.math.VectorMath;
import com.cubikore.astro.particle.AstParticleManager;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class AstParticleEmitter {
    public float scale = 1.0f;
    public Vector2f size;

    public int r, g, b = 1;

    public boolean relative = false;

    protected final int CAPACITY = 10000;

    public float drag = 0.98f;        // air resistance
    public float gravity = -9.81f;    // units/sec² (Y axis)

    protected boolean[] despawned;
    protected boolean[] calc;
    protected boolean[] physCalc;

    protected boolean calcA;

    float[] x, y, z;
    float[] prevX, prevY, prevZ;
    float[] vx, vy, vz;
    float[] ax, ay, az;

    float[] lifetime;
    float[] age;

    float emitterAge;

    public final int count;

    int[] id;

    private Identifier sprite;

    public boolean[] billboardAxis = new boolean[]{true, true, true};

    private int despawnedAmount = 0;

    protected boolean emittingStopped = false;

    public AstParticleEmitter(Vector3f position, Vector2f size, int count, float lifetime, Identifier spriteId) {
        this.count = count;

        id = new int[CAPACITY];

        x = new float[CAPACITY];
        y = new float[CAPACITY];
        z = new float[CAPACITY];

        prevX = new float[CAPACITY];
        prevY = new float[CAPACITY];
        prevZ = new float[CAPACITY];

        vx = new float[CAPACITY];
        vy = new float[CAPACITY];
        vz = new float[CAPACITY];

        ax = new float[CAPACITY];
        ay = new float[CAPACITY];
        az = new float[CAPACITY];

        age = new float[CAPACITY];
        this.lifetime = new float[CAPACITY];

        despawned = new boolean[CAPACITY];
        calc = new boolean[CAPACITY];
        physCalc = new boolean[CAPACITY];

        for(int i = 0; i < this.count; i++) {
            x[i] = position.x;
            y[i] = position.y;
            z[i] = position.z;

            prevX[i] = position.x;
            prevY[i] = position.y;
            prevZ[i] = position.z;

            vx[i] = 0;
            vy[i] = 0;
            vz[i] = 0;

            ax[i] = 0;
            ay[i] = 0;
            az[i] = 0;

            age[i] = 0;
            this.lifetime[i] = lifetime;

            this.id[i] = AstParticleManager.numParticles;
            AstParticleManager.numParticles++;

            despawned[i] = false;
            calc[i] = false;
            physCalc[i] = true;
        }

        this.size = size;

        this.sprite = spriteId != null ? spriteId : Identifier.of(AstroCraft.MOD_ID, "blank");
    }

    public void update(float deltaTime) {
        if(!calcA)
            init();

        for(int i = 0; i < count; i++) {
            if (!calc[i])
                initParticle(i);

            if(isDespawned(i))
                continue;

            if (age[i] > lifetime[i]) {
                despawn(i);
            }

            age[i] += deltaTime;

            if(physCalc[i]) {
                float[] res = VectorMath.add(vx[i], vy[i], vz[i], ax[i], ay[i], az[i]);

                vx[i] = res[0];
                vy[i] = res[1];
                vz[i] = res[2];

                float[] res2 = VectorMath.mul(vx[i], vy[i], vz[i], (float) Math.pow(drag, deltaTime * 20.0f));

                vx[i] = res2[0];
                vy[i] = res2[1];
                vz[i] = res2[2];

                float[] res3 = VectorMath.fma(deltaTime, x[i], y[i], z[i], vx[i], vy[i], vz[i]);

                x[i] = res3[0];
                y[i] = res3[1];
                z[i] = res3[2];

                prevX[i] = x[i];
                prevY[i] = y[i];
                prevZ[i] = z[i];

                ax[i] = 0;
                ay[i] = gravity;
                az[i] = 0;
            }
        }

        emitterAge += deltaTime;
    }

    protected void startPhysicsCalculations(int i) {
        physCalc[i] = true;
    }

    protected void stopPhysicsCalculations(int i) {
        physCalc[i] = false;
    }

    protected void stopped() {}

    public void stopEmitting() {
        this.emittingStopped = true;
        stopped();
    }

    public int getNumDespawned() {
        return this.despawnedAmount;
    }

    public Identifier getSprite() {
        return this.sprite;
    }

    protected void initParticle(int i) {
        calc[i] = true;
    }

    protected void init() {
        calcA = true;
    }

    protected void despawn(int i) {
        despawned[i] = true;
        this.despawnedAmount++;
    }

    public boolean isDespawned(int i) {
        return despawned[i];
    }

    public float[] getPos(int i) {
        return new float[]{x[i], y[i], z[i]};
    }
}
