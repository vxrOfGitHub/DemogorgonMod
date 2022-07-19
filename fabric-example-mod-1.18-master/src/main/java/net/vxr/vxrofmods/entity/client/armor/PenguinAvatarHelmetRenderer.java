package net.vxr.vxrofmods.entity.client.armor;

import net.vxr.vxrofmods.item.custom.PenguinAvatarHelmetItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class PenguinAvatarHelmetRenderer extends GeoArmorRenderer<PenguinAvatarHelmetItem> {

    public PenguinAvatarHelmetRenderer() {
        super(new PenguinAvatarHelmetModel());

        this.headBone = "armorHead";
    }
}
