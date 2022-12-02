package net.vxr.vxrofmods.entity.client;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.LennartGollumAvatarEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class LennartGollumAvatarModel extends AnimatedGeoModel<LennartGollumAvatarEntity> {
    @Override
    public Identifier getModelResource(LennartGollumAvatarEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/lennart_gollum.geo.json");
    }

    @Override
    public Identifier getTextureResource(LennartGollumAvatarEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/lennart_gollum/lennart_gollum.png");
    }

    @Override
    public Identifier getAnimationResource(LennartGollumAvatarEntity animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/lennart_gollum.animation.json");
    }
    /* @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(LennartGollumAvatarEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    } */

    @Override
    public void setCustomAnimations(LennartGollumAvatarEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * MathHelper.RADIANS_PER_DEGREE);
            head.setRotationY(extraData.netHeadYaw * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
