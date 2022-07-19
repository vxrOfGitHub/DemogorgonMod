package net.vxr.vxrofmods.entity.client.armor;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.item.custom.PenguinAvatarHelmetItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PenguinAvatarHelmetModel extends AnimatedGeoModel<PenguinAvatarHelmetItem> {
    @Override
    public Identifier getModelLocation(PenguinAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/penguin_avatar_helmet_item.geo.json");
    }

    @Override
    public Identifier getTextureLocation(PenguinAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/models/armor/penguin_avatar_helmet_item.png");
    }

    @Override
    public Identifier getAnimationFileLocation(PenguinAvatarHelmetItem animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/penguin_avatar_helmet_item.animation.json");
    }
}
