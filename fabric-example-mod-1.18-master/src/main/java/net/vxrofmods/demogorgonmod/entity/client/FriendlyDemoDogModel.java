package net.vxrofmods.demogorgonmod.entity.client;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.custom.FriendlyDemoDogEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FriendlyDemoDogModel extends GeoModel<FriendlyDemoDogEntity> {
    @Override
    public Identifier getModelResource(FriendlyDemoDogEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "geo/demodog.geo.json");
    }

    @Override
    public Identifier getTextureResource(FriendlyDemoDogEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demodog.png");
    }

    @Override
    public Identifier getAnimationResource(FriendlyDemoDogEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "animations/demodog.animation.json");
    }

    @Override
    public void setCustomAnimations(FriendlyDemoDogEntity animatable, long instanceId, AnimationState<FriendlyDemoDogEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if(head !=null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
