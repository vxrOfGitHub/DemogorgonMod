package net.vxr.vxrofmods.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.JoshSumpfmausAvatarEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class JoshSumpfmausAvatarRenderer extends GeoEntityRenderer<JoshSumpfmausAvatarEntity> {
    public JoshSumpfmausAvatarRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new JoshSumpfmausAvatarModel());
    }

    @Override
    public Identifier getTextureResource(JoshSumpfmausAvatarEntity instance) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/josh_sumpfmaus/josh_sumpfmaus.png");
    }
}
