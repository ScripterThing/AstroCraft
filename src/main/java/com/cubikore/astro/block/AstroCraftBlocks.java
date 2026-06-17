package com.cubikore.astro.block;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.block.special.BrightLightBlock;
import com.cubikore.astro.block.special.CaptainSeatBlock;
import com.cubikore.astro.block.special.ShipControlPanelBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Identifier;

public class AstroCraftBlocks {
    public static Block CAPTAIN_SEAT_BLOCK = registerBlock("captain_seat", new CaptainSeatBlock());

    public static Block VENUSIAN_BASALT_BLOCK = registerBlock("venusian_basalt", new Block(AbstractBlock.Settings.create()
            .strength(4f)
            .requiresTool()
            .sounds(BlockSoundGroup.STONE)
    ));

    public static Block IRON_OXIDE_BLOCK = registerBlock("iron_oxide", new Block(AbstractBlock.Settings.create()
            .strength(4f)
            .requiresTool()
            .sounds(BlockSoundGroup.SAND)
    ));

    public static Block VENUSIAN_SAND_BLOCK = registerBlock("venusian_sand", new ColoredFallingBlock(new ColorCode(0xfccd8f), AbstractBlock.Settings.create()
            .strength(4f)
            .requiresTool()
            .sounds(BlockSoundGroup.SAND)
    ));

    public static Block REINFORCED_GLASS_BLOCK = registerBlock("reinforced_glass", new TransparentBlock(AbstractBlock.Settings.create()
            .strength(4f)
            .dropsNothing()
            .requiresTool()
            .sounds(BlockSoundGroup.GLASS)
            .nonOpaque()
            .allowsSpawning(Blocks::never)
            .solidBlock(Blocks::never)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)
    ));

    public static Block REINFORCED_GLASS_PANE_BLOCK = registerBlock("reinforced_glass_pane", new PaneBlock(AbstractBlock.Settings.create()
            .strength(4f)
            .dropsNothing()
            .requiresTool()
            .sounds(BlockSoundGroup.GLASS)
            .nonOpaque()
            .allowsSpawning(Blocks::never)
            .solidBlock(Blocks::never)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)
    ));

    public static Block METAL_PLATE_BLOCK = registerBlock("metal_plate", new PaneBlock(AbstractBlock.Settings.create()
            .strength(4f)
            .dropsNothing()
            .requiresTool()
            .sounds(BlockSoundGroup.METAL)
            .nonOpaque()
            .allowsSpawning(Blocks::never)
            .solidBlock(Blocks::never)
            .suffocates(Blocks::never)
            .blockVision(Blocks::never)
    ));

    public static Block BRIGHT_LIGHT_BLOCK = registerBlock("bright_light_block", new BrightLightBlock(AbstractBlock.Settings.create()
            .strength(4f)
            .dropsNothing()
            .requiresTool()
            .sounds(BlockSoundGroup.GLASS)
            .nonOpaque()
            .allowsSpawning(Blocks::never)
            .suffocates(Blocks::never)
            //.luminance(state -> 15)
    ));

    public static Block SHIP_CONTROL_PANEL_BLOCK = registerBlock("ship_control_panel_block", new ShipControlPanelBlock(AbstractBlock.Settings.create()
            .strength(4f)
            .dropsNothing()
            .requiresTool()
            .sounds(BlockSoundGroup.METAL)
            .allowsSpawning(Blocks::never)
    ));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(AstroCraft.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(AstroCraft.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerBlocks() {
        AstroCraft.LOGGER.info("Registering blocks for Astro Craft");

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(AstroCraftBlocks.CAPTAIN_SEAT_BLOCK);
        });
    }
}
