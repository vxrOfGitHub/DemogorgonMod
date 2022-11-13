package net.vxr.vxrofmods.entity.client.armor;

import net.vxr.vxrofmods.item.custom.MoritzDragonAvatarHelmetItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class MoritzDragonAvatarHelmetRenderer extends GeoArmorRenderer<MoritzDragonAvatarHelmetItem> {

    public MoritzDragonAvatarHelmetRenderer() {
        super(new MoritzDragonAvatarHelmetModel());
    }
}
