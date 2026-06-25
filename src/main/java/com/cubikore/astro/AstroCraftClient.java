package com.cubikore.astro;

import com.cubikore.astro.block.AstroCraftBlocks;
import com.cubikore.astro.block.entity.AstroCraftBlockEntities;
import com.cubikore.astro.block.entity.renderer.BrightLightBlockEntityRenderer;
import com.cubikore.astro.block.fluid.AstroCraftFluids;
import com.cubikore.astro.client.renderer.AstroCraftRenderer;
import com.cubikore.astro.game.client.AstroCraftClientGameManager;
import com.cubikore.astro.networking.AstroCraftNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;

public class AstroCraftClient implements ClientModInitializer {
    public static AstroCraftRenderer renderer = new AstroCraftRenderer();
    public static AstroCraftClientGameManager clientGameManager = new AstroCraftClientGameManager();

    @Override
    public void onInitializeClient() {
        AstroCraftNetworking.registerS2CPayloads();

        registerClientEvents();

        clientGameManager.init();
        renderer.init();

        MinecraftClient client = MinecraftClient.getInstance();
    }

    private void registerClientEvents() {
        registerBlockRenderEvents();

        FluidRenderHandlerRegistry.INSTANCE.register(
                AstroCraftFluids.STILL_SULFURIC_ACID, AstroCraftFluids.FLOWING_SULFURIC_ACID,
                new SimpleFluidRenderHandler(Identifier.ofVanilla("block/water_still"), Identifier.ofVanilla("block/water_flow"), 0xffd573)
        );
    }

    private void registerBlockRenderEvents() {
        BlockRenderLayerMap.INSTANCE.putBlock(
                AstroCraftBlocks.REINFORCED_GLASS_BLOCK,
                RenderLayer.getCutout()
        );
        BlockRenderLayerMap.INSTANCE.putBlock(
                AstroCraftBlocks.REINFORCED_GLASS_PANE_BLOCK,
                RenderLayer.getCutout()
        );

        BlockEntityRendererFactories.register(
                AstroCraftBlockEntities.BRIGHT_LIGHT_BLOCK_ENTITY,
                BrightLightBlockEntityRenderer::new
        );
    }
}
