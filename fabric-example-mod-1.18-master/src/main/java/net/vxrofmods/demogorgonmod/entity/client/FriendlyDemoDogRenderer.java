package net.vxrofmods.demogorgonmod.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.custom.FriendlyDemoDogEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FriendlyDemoDogRenderer extends GeoEntityRenderer<FriendlyDemoDogEntity> {

    public FriendlyDemoDogRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new FriendlyDemoDogModel());
    }

    @Override
    public Identifier getTextureLocation(FriendlyDemoDogEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demodog.png");
    }
}
