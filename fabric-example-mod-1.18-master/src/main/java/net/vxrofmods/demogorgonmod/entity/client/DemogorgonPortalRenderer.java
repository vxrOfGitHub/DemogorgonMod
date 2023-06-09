package net.vxrofmods.demogorgonmod.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonPortalEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DemogorgonPortalRenderer extends GeoEntityRenderer<DemogorgonPortalEntity> {

    public DemogorgonPortalRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DemogorgonPortalModel());
    }

    @Override
    public Identifier getTextureLocation(DemogorgonPortalEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demogorgon_portal.png");
    }
}
