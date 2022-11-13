package net.vxr.vxrofmods.entity.client.armor;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.item.custom.PokuCapybaraAvatarHelmetItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PokuCapybaraAvatarHelmetModel extends AnimatedGeoModel<PokuCapybaraAvatarHelmetItem> {
    @Override
    public Identifier getModelResource(PokuCapybaraAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/poku_capybara_avatar_helmet_item.geo.json");
    }

    @Override
    public Identifier getTextureResource(PokuCapybaraAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/poku_capybara/poku_capybara.png");
    }

    @Override
    public Identifier getAnimationResource(PokuCapybaraAvatarHelmetItem animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/poku_capybara_avatar_helmet_item.animation.json");
    }
}
