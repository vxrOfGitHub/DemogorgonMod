package net.vxr.vxrofmods.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.client.MichelWolfAvatarModel;
import net.vxr.vxrofmods.entity.custom.MichelWolfAvatarEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MichelWolfAvatarRenderer extends GeoEntityRenderer<MichelWolfAvatarEntity> {
    public MichelWolfAvatarRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MichelWolfAvatarModel());
    }

    @Override
    public Identifier getTextureResource(MichelWolfAvatarEntity instance) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/michel_wolf/michel_wolf.png");
    }
}
