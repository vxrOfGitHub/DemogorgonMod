package net.vxrofmods.demogorgonmod.entity.client;

import com.google.common.collect.Maps;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonEntity;
import net.vxrofmods.demogorgonmod.entity.variant.DemogorgonVariant;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Map;

public class DemogorgonRenderer extends GeoEntityRenderer<DemogorgonEntity> {


    public static final Map<DemogorgonVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(DemogorgonVariant.class), (map) -> {
                map.put(DemogorgonVariant.DEFAULT,
                        new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demogorgon/demogorgon.png"));
                map.put(DemogorgonVariant.DARK,
                        new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demogorgon/demogorgondark.png"));
                map.put(DemogorgonVariant.GREY,
                        new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demogorgon/demogorgongrey.png"));
                map.put(DemogorgonVariant.RED,
                        new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demogorgon/demogorgonred.png"));
            });

    public DemogorgonRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DemogorgonModel());
    }

    @Override
    public Identifier getTextureLocation(DemogorgonEntity animatable) {
        return DemogorgonRenderer.LOCATION_BY_VARIANT.get(animatable.getVariant());
    }
}
