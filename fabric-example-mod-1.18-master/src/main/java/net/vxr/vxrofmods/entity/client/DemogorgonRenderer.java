package net.vxr.vxrofmods.entity.client;

import com.google.common.collect.Maps;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.DemogorgonEntity;
import net.vxr.vxrofmods.entity.variant.DemogorgonVariant;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

public class DemogorgonRenderer extends GeoEntityRenderer<DemogorgonEntity> {

    public static final Map<DemogorgonVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(DemogorgonVariant.class), (map) -> {
                map.put(DemogorgonVariant.DEFAULT,
                        new Identifier(WW2Mod.MOD_ID, "textures/entity/demogorgon/demogorgon.png"));
                map.put(DemogorgonVariant.DARK,
                        new Identifier(WW2Mod.MOD_ID, "textures/entity/demogorgon/demogorgondark.png"));
                map.put(DemogorgonVariant.GREY,
                        new Identifier(WW2Mod.MOD_ID, "textures/entity/demogorgon/demogorgongrey.png"));
                map.put(DemogorgonVariant.RED,
                        new Identifier(WW2Mod.MOD_ID, "textures/entity/demogorgon/demogorgonred.png"));
            });
    public DemogorgonRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DemogorgonModel());
    }

    @Override
    public Identifier getTextureLocation(DemogorgonEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getVariant());
    }
}
