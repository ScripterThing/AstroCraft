package com.cubikore.astro.client;

import com.cubikore.astro.math.AstMath;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;

import static com.cubikore.astro.AstroCraftClient.getGameTime;

public class PlayerBobbing {
    private static long startAnimTime = System.currentTimeMillis();
    private static boolean startedMoving = false;
    private static boolean stoppedMoving = false;
    private static boolean leftGround = false;
    private static boolean landedOnGround = false;

    private static float prevPitch = 0;
    private static float renderedPitch = 0;

    private static float prevRoll = 0;
    private static float renderedRoll = 0;

    private static long lastTime;

    public static void doBobbing(MatrixStack matrices, MinecraftClient client, PlayerEntity playerEntity, float tickDelta) {
        float targetPitch;
        float targetRoll;

        float psm = getDeltaTime();

        if(playerEntity.strideDistance > 0.01f) {
            stoppedMoving = false;

            if(!startedMoving) {
                startedMoving = true;
                captureAnimTime();
            }

            float sine = (float) Math.sin(getGameTime() * (playerEntity.isSprinting() ? 10f : 6f));
            float sine2 = (float) Math.sin(getGameTime() * (playerEntity.isSprinting() ? 15f : 6f));

            targetPitch = sine * 0.5f;
            targetRoll = sine2;
        }
        else {
            startedMoving = false;

            if(!stoppedMoving) {
                stoppedMoving = true;
                captureAnimTime();
            }

            targetPitch = 0;
            targetRoll = 0;
        }

        if(!playerEntity.isOnGround()) {
            landedOnGround = false;

            if(!leftGround) {
                leftGround = true;
                captureAnimTime();
            }

            targetPitch += 3f;
        }
        else {
            leftGround = false;

            if(!landedOnGround) {
                landedOnGround = true;
                captureAnimTime();
            }
        }

        float tp = ((System.currentTimeMillis() - startAnimTime) / 1000f) * psm;

        renderedPitch = AstMath.lerp(prevPitch, targetPitch, tp);
        renderedRoll = AstMath.lerp(prevRoll, targetRoll, tp);

        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(renderedPitch));
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(renderedRoll));

        prevPitch = renderedPitch;
        prevRoll = renderedRoll;
    }

    private static void captureAnimTime() {
        startAnimTime = System.currentTimeMillis();
    }

    private static float getDeltaTime() {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client.isPaused())
            return 0;

        return 1;
    }
}
