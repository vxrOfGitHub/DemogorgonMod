package net.vxrofmods.demogorgonmod.entity.client;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.custom.DemoDogEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class DemoDogModel extends GeoModel<DemoDogEntity> {
    @Override
    public Identifier getModelResource(DemoDogEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "geo/demodog.geo.json");
    }

    @Override
    public Identifier getTextureResource(DemoDogEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demodog.png");
    }

    @Override
    public Identifier getAnimationResource(DemoDogEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "animations/demodog.animation.json");
    }

    @Override
    public void setCustomAnimations(DemoDogEntity animatable, long instanceId, AnimationState<DemoDogEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if(head !=null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
