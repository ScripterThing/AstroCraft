package com.cubikore.astro.client.cutscene;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.math.AstMath;
import com.cubikore.astro.math.bezier.CubicBezierCurve;
import com.cubikore.astro.math.bezier.Point;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CutsceneManager {
    private List<AstCutscene> cutscenes = new ArrayList<>();

    public void addScene(AstCutscene cutscene) {
        cutscenes.add(cutscene);
    }

    public void playScene(MinecraftClient client, String name) {
        for(AstCutscene cutscene : cutscenes) {
            if(cutscene.getName().equals(name))
                cutscene.play(client);
        }
    }

    public void tick(MinecraftClient client) {

    }

    public void frame(MinecraftClient client) {
        if(client.player != null) {
            for(AstCutscene cutscene : cutscenes) {
                if(cutscene.isPlaying()) {
                    if(cutscene.getProgress() < cutscene.getSceneLength()) {
                        cutscene.frame();
                    }
                    else {
                        cutscene.stop();
                    }
                }
            }
        }
    }
}
