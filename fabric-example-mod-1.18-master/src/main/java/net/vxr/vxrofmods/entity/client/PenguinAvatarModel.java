package net.vxr.vxrofmods.entity.client;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.DemogorgonEntity;
import net.vxr.vxrofmods.entity.custom.vxrPenguinAvatarEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class PenguinAvatarModel extends AnimatedGeoModel<vxrPenguinAvatarEntity> {
    @Override
    public Identifier getModelResource(vxrPenguinAvatarEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/vxr_penguin.geo.json");
    }

    @Override
    public Identifier getTextureResource(vxrPenguinAvatarEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/vxr_penguin/vxr_penguin.png");
    }

    @Override
    public Identifier getAnimationResource(vxrPenguinAvatarEntity animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/vxr_penguin.animation.json");
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(vxrPenguinAvatarEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
