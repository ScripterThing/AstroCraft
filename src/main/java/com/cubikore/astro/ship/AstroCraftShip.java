package com.cubikore.astro.ship;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.networking.payload.FTLJumpPayload;
import com.cubikore.astro.server.ServerStorage;
import com.cubikore.astro.universe.Planet;
import com.cubikore.astro.universe.Universe;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class AstroCraftShip {
    public static boolean autoThrust = false;

    public static boolean doingFTLJump = false;
    public static int clientConf = 0;

    public static Vector4f ftlJumpDestination = new Vector4f();

    public static void doFTLJump(MinecraftServer server, Identifier destination) {
        Planet targetPlanet = Universe.getPlanet(destination);
        Planet sun = Universe.getPlanet(Universe.SUN_ID);

        autoThrust = false;

        if(targetPlanet != null && sun != null) {
            Vector3f sunPos = new Vector3f(sun.position[0], sun.position[1], sun.position[2]);
            Vector3f planetPos = new Vector3f(targetPlanet.position[0], targetPlanet.position[1], targetPlanet.position[2]);

            Vector3f direction = new Vector3f(sunPos).sub(planetPos).normalize();
            Vector3f targetPosition = direction.mul(targetPlanet.radius[0] / 2.0f).add(planetPos);

            Vector3f targetDir = new Vector3f(planetPos).sub(targetPosition).normalize();

            float yaw = (float)Math.toDegrees(Math.atan2(targetDir.x, -targetDir.z));

            targetPosition.y = (targetPlanet.radius[0] + planetPos.y) + 1f;

            ftlJumpDestination.set(new Vector4f(targetPosition.x, targetPosition.y, targetPosition.z, yaw));

            doingFTLJump = true;

            for(ServerPlayerEntity player : PlayerLookup.all(server)) {
                ServerPlayNetworking.send(player, new FTLJumpPayload(targetPosition, yaw));
            }
        }
    }

    public static void moveShip(Vector4f directionVec) {
        float yawRad = (float) Math.toRadians(ServerStorage.worldOffset.w);
        float cos = (float) Math.cos(yawRad);
        float sin = (float) Math.sin(yawRad);

        float lx = directionVec.x();
        float ly = directionVec.y();
        float lz = directionVec.z();

        // Rotate input → world direction
        float worldX = lx * cos - lz * sin;
        float worldZ = lx * sin + lz * cos;
        float worldY = ly;

        Vector4f direction = new Vector4f(worldX, worldY, worldZ, directionVec.w);

        ServerStorage.directionMoving.set(direction);
    }

    public static void toggleAutoThrust() {
        System.out.println(autoThrust);
        autoThrust = !autoThrust;
        System.out.println(autoThrust);
    }
}
