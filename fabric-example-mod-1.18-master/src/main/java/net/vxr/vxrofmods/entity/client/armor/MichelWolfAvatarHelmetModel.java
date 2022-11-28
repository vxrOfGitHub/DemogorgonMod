package net.vxr.vxrofmods.entity.client.armor;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.item.custom.MichelWolfAvatarHelmetItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MichelWolfAvatarHelmetModel extends AnimatedGeoModel<MichelWolfAvatarHelmetItem> {
    @Override
    public Identifier getModelResource(MichelWolfAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/michel_wolf_avatar_helmet_item.geo.json");
    }

    @Override
    public Identifier getTextureResource(MichelWolfAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/models/armor/michel_wolf.png");
    }

    @Override
    public Identifier getAnimationResource(MichelWolfAvatarHelmetItem animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/michel_wolf_avatar_helmet_item.animation.json");
    }
}
