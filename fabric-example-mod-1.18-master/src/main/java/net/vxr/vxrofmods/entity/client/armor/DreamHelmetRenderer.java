package net.vxr.vxrofmods.entity.client.armor;

import net.vxr.vxrofmods.item.custom.ModHelmetItem;
import net.vxr.vxrofmods.item.custom.PenguinAvatarHelmetItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class DreamHelmetRenderer extends GeoArmorRenderer<ModHelmetItem> {

    public DreamHelmetRenderer() {
        super(new DreamHelmetModel());

        this.headBone = "armorHead";
    }
}
