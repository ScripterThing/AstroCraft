package com.cubikore.astro.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.GuiAtlasManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DrawContext.class)
public interface DrawContextAccessor {
    @Accessor("guiAtlasManager")
    GuiAtlasManager getGuiAtlasManager();
}
