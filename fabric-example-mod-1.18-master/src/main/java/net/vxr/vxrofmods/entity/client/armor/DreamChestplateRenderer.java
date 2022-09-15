package net.vxr.vxrofmods.entity.client.armor;

import net.vxr.vxrofmods.item.custom.ModChestplateItem;
import net.vxr.vxrofmods.item.custom.ModHelmetItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class DreamChestplateRenderer extends GeoArmorRenderer<ModChestplateItem> {

    public DreamChestplateRenderer() {
        super(new DreamChestplateModel());

        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
    }
}
