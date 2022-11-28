package net.vxr.vxrofmods.entity.client.armor;

import net.vxr.vxrofmods.item.custom.MichelWolfAvatarHelmetItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MichelWolfAvatarHelmetRenderer extends GeoArmorRenderer<MichelWolfAvatarHelmetItem> {

    public MichelWolfAvatarHelmetRenderer() {
        super(new MichelWolfAvatarHelmetModel());

        this.headBone = "armorHead";
    }
}
