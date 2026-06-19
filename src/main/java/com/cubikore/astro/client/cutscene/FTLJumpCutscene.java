package com.cubikore.astro.client.cutscene;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.dimension.DimensionKeys;
import com.cubikore.astro.math.AstMath;
import com.cubikore.astro.math.bezier.CubicBezierCurve;
import com.cubikore.astro.math.bezier.Point;
import com.cubikore.astro.networking.payload.FTLJumpPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.joml.Vector3f;

public class FTLJumpCutscene extends AstCutscene{
    private boolean firstAngle = false;
    private boolean secondAngle = false;

    private long firstAngleStart = System.currentTimeMillis();
    private long secondAngleStart = System.currentTimeMillis();

    public FTLJumpCutscene() {
        super("FTLJump", 7f);
    }

    @Override
    public void play(MinecraftClient client) {
        super.play(client);

        ClientStorage.doingFTLJump = true;
    }

    private float calculateArrivingEasing(float x) {
        Point start = new Point(0, 0);
        Point end = new Point(1, 1);

        Point c1 = new Point(0.96f, 1.02f);
        Point c2 = new Point(0.06f, 0.98f);

        CubicBezierCurve cubicBezierCurve = new CubicBezierCurve(start, c1, c2, end);
        return cubicBezierCurve.getPointAtCurve(x).getY();
    }

    @Override
    public void frame() {
        super.frame();

        Entity cameraEntity = client.cameraEntity;
        ClientPlayerEntity player = client.player;

        float sceneTime = getProgress();

        if(cameraEntity != null && player != null) {
            if(DimensionKeys.inSpace(player)) {
                client.options.hudHidden = true;

                if(firstAngle && !secondAngle) {
                    float tp = getDelta(firstAngleStart);
                    float fraction = tp / 2.0f;
                    ClientStorage.terrainOffset[0] = AstMath.lerpExp(0, 300, 50, fraction);
                }
                else if(secondAngle) {
                    float tp = getDelta(secondAngleStart);
                    float fraction = calculateArrivingEasing(Math.min(tp / 2.0f, 1.0f));
                    ClientStorage.terrainOffset[0] = AstMath.lerp(-2000, 0, fraction);
                }

                if(!firstAngle) {
                    firstAngle = true;
                    firstAngleStart = System.currentTimeMillis();

                    cameraEntity.refreshPositionAndAngles(115, 68, -73, 30.6f, 41.3f);
                }
                else if(!secondAngle && sceneTime >= 3f) {
                    secondAngle = true;
                    secondAngleStart = System.currentTimeMillis();

                    cameraEntity.refreshPositionAndAngles(134, 16, 80, 142.2f, 15f);

                    ClientStorage.renderedWorldOffset.set(ClientStorage.FTLJumpDestination);
                    AstroCraftClient.clientGameManager.prevWorldOffset.set(ClientStorage.renderedWorldOffset);
                    AstroCraftClient.clientGameManager.targetOffsetPos.set(ClientStorage.FTLJumpDestination);

                    sendDonePacket();
                }
            }
        }
    }

    private void sendDonePacket() {
        Vector3f dest = new Vector3f(ClientStorage.FTLJumpDestination.x, ClientStorage.FTLJumpDestination.y, ClientStorage.FTLJumpDestination.z);
        ClientPlayNetworking.send(new FTLJumpPayload(dest, ClientStorage.FTLJumpDestination.w));
    }

    @Override
    protected void setUpCamera() {
        ClientPlayerEntity player = client.player;
        if(player != null && DimensionKeys.inSpace(player)) {
            createCamera(player);
            cameraSetUp = true;
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.client.options.hudHidden = false;
        client.cameraEntity = client.player;

        firstAngle = false;
        secondAngle = false;

        ClientStorage.terrainOffset[0] = 0;
        ClientStorage.terrainOffset[1] = 0;
        ClientStorage.terrainOffset[2] = 0;

        ClientStorage.renderedWorldOffset.set(ClientStorage.FTLJumpDestination);
        AstroCraftClient.clientGameManager.prevWorldOffset.set(ClientStorage.renderedWorldOffset);
        AstroCraftClient.clientGameManager.targetOffsetPos.set(ClientStorage.FTLJumpDestination);

        sendDonePacket();

        ClientStorage.doingFTLJump = false;
    }
}
