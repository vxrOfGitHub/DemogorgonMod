package net.vxr.vxrofmods.entity.client;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.MoritzDragonAvatarEntity;
import net.vxr.vxrofmods.entity.custom.vxrPenguinAvatarEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class MoritzDragonModel extends AnimatedGeoModel<MoritzDragonAvatarEntity> {
    @Override
    public Identifier getModelResource(MoritzDragonAvatarEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/moritz_dragon.geo.json");
    }

    @Override
    public Identifier getTextureResource(MoritzDragonAvatarEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/moritz_dragon/moritz_dragon.png");
    }

    @Override
    public Identifier getAnimationResource(MoritzDragonAvatarEntity animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/moritz_dragon.animation.json");
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(MoritzDragonAvatarEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
