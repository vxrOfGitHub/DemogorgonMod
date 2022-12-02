package net.vxr.vxrofmods.entity.client.armor;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.item.custom.LennartGollumAvatarHelmetItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LennartGollumAvatarHelmetModel extends AnimatedGeoModel<LennartGollumAvatarHelmetItem> {
    @Override
    public Identifier getModelResource(LennartGollumAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/lennart_gollum_helmet.geo.json");
    }

    @Override
    public Identifier getTextureResource(LennartGollumAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/lennart_gollum/lennart_gollum.png");
    }

    @Override
    public Identifier getAnimationResource(LennartGollumAvatarHelmetItem animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/lennart_gollum_helmet.animation.json");
    }
}
