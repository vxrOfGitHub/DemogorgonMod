package net.vxr.vxrofmods.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.DomeCapybaraAvatarEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DomeCapybaraAvatarRenderer extends GeoEntityRenderer<DomeCapybaraAvatarEntity> {
    public DomeCapybaraAvatarRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DomeCapybaraAvatarModel());
    }

    @Override
    public Identifier getTextureResource(DomeCapybaraAvatarEntity instance) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/dome_capybara/dome_capybara.png");
    }
}
