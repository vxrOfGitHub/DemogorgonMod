package net.vxr.vxrofmods.entity.client.armor;

import net.vxr.vxrofmods.item.custom.DomeCapybaraAvatarHelmetItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class DomeCapybaraAvatarHelmetRenderer extends GeoArmorRenderer<DomeCapybaraAvatarHelmetItem> {

    public DomeCapybaraAvatarHelmetRenderer() {
        super(new DomeCapybaraAvatarHelmetModel());

        this.headBone = "armorHead";
    }
}
