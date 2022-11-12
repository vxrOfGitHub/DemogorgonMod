package net.vxr.vxrofmods.entity.client.armor;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.item.custom.DomeCapybaraAvatarHelmetItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DomeCapybaraAvatarHelmetModel extends AnimatedGeoModel<DomeCapybaraAvatarHelmetItem> {
    @Override
    public Identifier getModelResource(DomeCapybaraAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/dome_capybara_avatar_helmet_item.geo.json");
    }

    @Override
    public Identifier getTextureResource(DomeCapybaraAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/models/armor/dome_capybara.png");
    }

    @Override
    public Identifier getAnimationResource(DomeCapybaraAvatarHelmetItem animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/dome_capybara_avatar_helmet_item.animation.json");
    }
}
