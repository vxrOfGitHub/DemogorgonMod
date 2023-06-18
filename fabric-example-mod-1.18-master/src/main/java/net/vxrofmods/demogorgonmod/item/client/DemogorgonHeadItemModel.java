package net.vxrofmods.demogorgonmod.item.client;

import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.item.custom.DemogorgonArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class DemogorgonHeadItemModel extends GeoModel<DemogorgonArmorItem> {
    @Override
    public Identifier getModelResource(DemogorgonArmorItem animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "geo/demogorgon_head_item.geo.json");
    }

    @Override
    public Identifier getTextureResource(DemogorgonArmorItem animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demogorgon/demogorgondark.png");
    }

    @Override
    public Identifier getAnimationResource(DemogorgonArmorItem animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "animations/demogorgon_head_item.animation.json");
    }
}
