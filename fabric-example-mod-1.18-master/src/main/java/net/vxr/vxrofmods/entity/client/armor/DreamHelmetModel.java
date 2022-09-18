package net.vxr.vxrofmods.entity.client.armor;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.item.custom.ModHelmetItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DreamHelmetModel extends AnimatedGeoModel<ModHelmetItem> {
    @Override
    public Identifier getModelResource(ModHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/dream_helmet.geo.json");
    }

    @Override
    public Identifier getTextureResource(ModHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/models/armor/dream_helmet.png");
    }

    @Override
    public Identifier getAnimationResource(ModHelmetItem animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/dream_helmet.animation.json");
    }
}