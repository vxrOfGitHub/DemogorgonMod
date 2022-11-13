package net.vxr.vxrofmods.entity.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.MoritzDragonAvatarEntity;
import net.vxr.vxrofmods.entity.custom.MoritzDragonAvatarEntity2;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MoritzDragonAvatarRenderer extends GeoEntityRenderer<MoritzDragonAvatarEntity> {
    public MoritzDragonAvatarRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MoritzDragonModel());
    }

    @Override
    public Identifier getTextureResource(MoritzDragonAvatarEntity instance) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/moritz_dragon/moritz_dragon.png");
    }
}
