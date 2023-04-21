package net.vxrofmods.demogorgonmod.entity.client;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class DemogorgonModel extends GeoModel<DemogorgonEntity> {
    @Override
    public Identifier getModelResource(DemogorgonEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "geo/demogorgon.geo.json");
    }

    @Override
    public Identifier getTextureResource(DemogorgonEntity animatable) {
        return DemogorgonRenderer.LOCATION_BY_VARIANT.get(animatable.getVariant());
    }

    @Override
    public Identifier getAnimationResource(DemogorgonEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "animations/demogorgon.animation.json");
    }

    @Override
    public void setCustomAnimations(DemogorgonEntity animatable, long instanceId, AnimationState<DemogorgonEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if(head !=null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
