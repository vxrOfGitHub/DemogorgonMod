package net.vxrofmods.demogorgonmod.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.custom.DemoDogEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DemoDogRenderer extends GeoEntityRenderer<DemoDogEntity> {

    public DemoDogRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DemoDogModel());
    }

    @Override
    public Identifier getTextureLocation(DemoDogEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demodog.png");
    }
}
