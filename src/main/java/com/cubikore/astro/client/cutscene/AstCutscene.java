package com.cubikore.astro.client.cutscene;

import com.cubikore.astro.dimension.DimensionKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

public class AstCutscene {
    private final String name;
    private boolean playing = false;
    private float sceneLength;

    private long sceneStart;

    protected MinecraftClient client;

    protected boolean cameraSetUp = false;

    public AstCutscene(String name, float sceneLength) {
        this.name = name;
        this.sceneLength = sceneLength;
    }

    public void play(MinecraftClient client) {
        this.client = client;
        this.playing = true;
        this.sceneStart = System.currentTimeMillis();
        setUpCamera();
    }

    protected void setUpCamera() {
        ClientPlayerEntity player = client.player;
        if(player != null) {
            createCamera(player);
            cameraSetUp = true;
        }
    }

    protected void createCamera(ClientPlayerEntity player) {
        client.cameraEntity = new ItemEntity(client.player.getWorld(), 0, 0, 0, ItemStack.EMPTY);
    }

    public void stop() {
        this.playing = false;
        this.cameraSetUp = false;
    }

    public void frame() {
        if(!cameraSetUp) {
            setUpCamera();
        }
    }

    protected float getProgress() {
        return (System.currentTimeMillis() - this.sceneStart) / 1000f;
    }

    protected float getDelta(long start) {
        return (System.currentTimeMillis() - start) / 1000f;
    }

    public float getSceneLength() {
        return this.sceneLength;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public String getName() {
        return this.name;
    }
}
