package com.cubikore.astro.block.entity;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.block.special.BrightLightBlock;
import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.client.light.PositionPointLightData;
import foundry.veil.VeilClient;
import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.VeilRenderer;
import foundry.veil.api.client.render.light.data.LightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import foundry.veil.fabric.VeilFabric;
import foundry.veil.fabric.compat.iris.VeilFabricIrisCompat;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class BrightLightBlockEntity extends BlockEntity {
    private PositionPointLightData pointLight;
    private LightRenderHandle<PositionPointLightData> lightHandle;

    public BrightLightBlockEntity(BlockPos pos, BlockState state) {
        super(AstroCraftBlockEntities.BRIGHT_LIGHT_BLOCK_ENTITY, pos, state);
    }

    public void updateLight(BlockState state) {
        if(pointLight == null || lightHandle == null) {
            pointLight = new PositionPointLightData(new Vector3d(0));
            lightHandle = VeilRenderSystem.renderer().getLightRenderer().addLight(pointLight);
            ClientStorage.BLOCK_LIGHTS.put(this.pos, lightHandle);
        }

        Direction dir = state.get(BrightLightBlock.LIGHT_DIR);
        BlockPos blockTarget = this.pos.offset(dir);

        float yO = 0;

        if(dir.getAxis() == Direction.Axis.Y) {
            yO = dir == Direction.DOWN ? 1f - 0.0625f : 0.0625f;
        }

        Vector3d targetPos = new Vector3d(blockTarget.getX() + 0.5, blockTarget.getY() + yO, blockTarget.getZ() + 0.5);

        pointLight.setPosition(targetPos.x, targetPos.y, targetPos.z);
        pointLight.setTruePosition(targetPos);
        pointLight.setRadius(10.0f);
        pointLight.setColor(1.0f, 1.0f, 1.0f);
        pointLight.setBrightness(1.0f);
        pointLight.setOcclusionEnabled(true);
    }

    @Override
    public void setCachedState(BlockState state) {
        super.setCachedState(state);

        if(this.getWorld() != null && this.getWorld().isClient())
            updateLight(state);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);

        if(world.isClient()) {
            updateLight(this.getCachedState());
        }
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        removeLight();
    }

    private void removeLight() {
        if(this.getWorld() != null && this.getWorld().isClient()) {
            pointLight.setBrightness(0);
            ClientStorage.BLOCK_LIGHTS.remove(this.pos);
            lightHandle.free();
        }
    }
}
