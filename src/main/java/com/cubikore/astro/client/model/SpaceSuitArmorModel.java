package com.cubikore.astro.client.model;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.item.special.SpaceSuitArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class SpaceSuitArmorModel extends GeoModel<SpaceSuitArmorItem> {
    @Override
    public Identifier getModelResource(SpaceSuitArmorItem animatable) {
        return Identifier.of(AstroCraft.MOD_ID, "geo/space_suit.geo.json");
    }

    @Override
    public Identifier getTextureResource(SpaceSuitArmorItem animatable) {
        return Identifier.of(AstroCraft.MOD_ID, "textures/armor/space_suit.png");
    }

    @Override
    public Identifier getAnimationResource(SpaceSuitArmorItem animatable) {
        return Identifier.of(AstroCraft.MOD_ID, "animations/space_suit.animation.json");
    }
}
