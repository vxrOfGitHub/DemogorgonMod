package net.vxr.vxrofmods.entity.client.armor;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.item.custom.JoshSumpfmausAvatarHelmetItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class JoshSumpfmausAvatarHelmetModel extends AnimatedGeoModel<JoshSumpfmausAvatarHelmetItem> {
    @Override
    public Identifier getModelResource(JoshSumpfmausAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/josh_sumpfmaus_helmet.geo.json");
    }

    @Override
    public Identifier getTextureResource(JoshSumpfmausAvatarHelmetItem object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/josh_sumpfmaus/josh_sumpfmaus.png");
    }

    @Override
    public Identifier getAnimationResource(JoshSumpfmausAvatarHelmetItem animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/josh_sumpfmaus_helmet.animation.json");
    }
}
