package net.vxr.vxrofmods.entity.client.armor;

import net.vxr.vxrofmods.entity.client.PokuCapybaraAvatarModel;
import net.vxr.vxrofmods.item.custom.DomeCapybaraAvatarHelmetItem;
import net.vxr.vxrofmods.item.custom.PokuCapybaraAvatarHelmetItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class PokuCapybaraAvatarHelmetRenderer extends GeoArmorRenderer<PokuCapybaraAvatarHelmetItem> {

    public PokuCapybaraAvatarHelmetRenderer() {
        super(new PokuCapybaraAvatarHelmetModel());

        this.headBone = "armorBody";
    }
}
