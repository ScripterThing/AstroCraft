package com.cubikore.astro.client.renderer.gecko.armor;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.client.model.SpaceSuitArmorModel;
import com.cubikore.astro.components.AstComponents;
import com.cubikore.astro.item.special.SpaceSuitArmorItem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.Color;

public class SpaceSuitArmorRenderer extends GeoArmorRenderer<SpaceSuitArmorItem>  {
    private ItemStack currentStack = ItemStack.EMPTY;

    public SpaceSuitArmorRenderer() {
        super(new SpaceSuitArmorModel());
    }

    @Override
    public Color getRenderColor(SpaceSuitArmorItem animatable, float partialTick, int packedLight) {
        String color = currentStack.get(AstComponents.SUIT_COLOR_COMPONENT);
        Formatting tint = Formatting.byName(color);

        if(tint != null)
            return Color.ofOpaque(tint.getColorValue());

        return Color.WHITE;
    }

    public void setCurrentStack(ItemStack stack) {
        currentStack = stack;
    }
}
