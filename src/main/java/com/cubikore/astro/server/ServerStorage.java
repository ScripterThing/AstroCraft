package com.cubikore.astro.server;

import com.cubikore.astro.universe.Planet;
import com.cubikore.astro.weather.PlanetWeather;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerStorage {
    public static HashMap<BlockPos, DisplayEntity.TextDisplayEntity> seatingEntities = new HashMap<>();

    public static Vector4f directionMoving = new Vector4f(0);

    public static float shipSpeed = 1f;

    public static boolean speedAdjustmentReady = false;

    public static Vector4f worldOffset = new Vector4f(0);
    public static Vector4f prevWorldOffset = new Vector4f(0);
}
