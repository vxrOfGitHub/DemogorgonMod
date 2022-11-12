package net.vxr.vxrofmods.entity.client.armor;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.item.custom.MoritzDragonAvatarHelmetItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MoritzDragonAvatarHelmetModel extends AnimatedGeoModel<MoritzDragonAvatarHelmetItem> {
    @Override
    public Identifier getModelResource(MoritzDragonAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/moritz_dragon_avatar_helmet_item.geo.json");
    }

    @Override
    public Identifier getTextureResource(MoritzDragonAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/models/armor/moritz_dragon_avatar_helmet_item.png");
    }

    @Override
    public Identifier getAnimationResource(MoritzDragonAvatarHelmetItem animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/moritz_dragon_avatar_helmet_item.animation.json");
    }
}
