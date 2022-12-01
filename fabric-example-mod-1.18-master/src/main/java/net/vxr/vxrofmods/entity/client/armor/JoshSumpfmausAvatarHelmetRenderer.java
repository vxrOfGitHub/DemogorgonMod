package net.vxr.vxrofmods.entity.client.armor;

import net.vxr.vxrofmods.item.custom.JoshSumpfmausAvatarHelmetItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class JoshSumpfmausAvatarHelmetRenderer extends GeoArmorRenderer<JoshSumpfmausAvatarHelmetItem> {

    public JoshSumpfmausAvatarHelmetRenderer() {
        super(new JoshSumpfmausAvatarHelmetModel());

        this.headBone = "armorHead";
    }
}
