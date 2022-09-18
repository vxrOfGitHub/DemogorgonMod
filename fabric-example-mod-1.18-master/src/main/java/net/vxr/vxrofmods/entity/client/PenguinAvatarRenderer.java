package net.vxr.vxrofmods.entity.client;

import com.google.common.collect.Maps;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.DemogorgonEntity;
import net.vxr.vxrofmods.entity.custom.vxrPenguinAvatarEntity;
import net.vxr.vxrofmods.entity.variant.DemogorgonVariant;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class PenguinAvatarRenderer extends GeoEntityRenderer<vxrPenguinAvatarEntity> {
    public PenguinAvatarRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PenguinAvatarModel());
    }

    @Override
    public Identifier getTextureResource(vxrPenguinAvatarEntity instance) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/vxr_penguin/vxr_penguin.png");
    }
}
