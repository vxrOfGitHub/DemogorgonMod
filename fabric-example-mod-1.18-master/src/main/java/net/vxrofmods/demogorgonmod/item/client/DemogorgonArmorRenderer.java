package net.vxrofmods.demogorgonmod.item.client;

import net.vxrofmods.demogorgonmod.item.custom.DemogorgonArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class DemogorgonArmorRenderer extends GeoArmorRenderer<DemogorgonArmorItem> {
    public DemogorgonArmorRenderer() {
        super(new DemogorgonArmorModel());
    }
}
