package net.vxr.vxrofmods.entity.client.armor;

import net.vxr.vxrofmods.item.custom.LennartGollumAvatarHelmetItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class LennartGollumAvatarHelmetRenderer extends GeoArmorRenderer<LennartGollumAvatarHelmetItem> {

    public LennartGollumAvatarHelmetRenderer() {
        super(new LennartGollumAvatarHelmetModel());

        this.headBone = "armorHead";
    }
}
