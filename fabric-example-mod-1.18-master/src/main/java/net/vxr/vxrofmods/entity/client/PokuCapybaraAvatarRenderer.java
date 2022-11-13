package net.vxr.vxrofmods.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.DomeCapybaraAvatarEntity;
import net.vxr.vxrofmods.entity.custom.PokuCapybaraAvatarEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PokuCapybaraAvatarRenderer extends GeoEntityRenderer<PokuCapybaraAvatarEntity> {
    public PokuCapybaraAvatarRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PokuCapybaraAvatarModel());
    }

    @Override
    public Identifier getTextureResource(PokuCapybaraAvatarEntity instance) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/poku_capybara/poku_capybara.png");
    }
}
