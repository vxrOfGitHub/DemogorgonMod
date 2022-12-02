package net.vxr.vxrofmods.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.LennartGollumAvatarEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class LennartGollumAvatarRenderer extends GeoEntityRenderer<LennartGollumAvatarEntity> {
    public LennartGollumAvatarRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new LennartGollumAvatarModel());
    }

    @Override
    public Identifier getTextureResource(LennartGollumAvatarEntity instance) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/lennart_gollum/lennart_gollum.png");
    }
}
