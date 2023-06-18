package net.vxrofmods.demogorgonmod.item.client;

import net.vxrofmods.demogorgonmod.item.custom.DemogorgonArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DemogorgonHeadItemRenderer extends GeoItemRenderer<DemogorgonArmorItem> {
    public DemogorgonHeadItemRenderer() {
        super(new DemogorgonHeadItemModel());
    }
}
