package net.vxr.vxrofmods.entity.client;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.DomeCapybaraAvatarEntity;
import net.vxr.vxrofmods.entity.custom.DomeCapybaraAvatarEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DomeCapybaraAvatarModel extends AnimatedGeoModel<DomeCapybaraAvatarEntity> {
    @Override
    public Identifier getModelResource(DomeCapybaraAvatarEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/dome_capybara.geo.json");
    }

    @Override
    public Identifier getTextureResource(DomeCapybaraAvatarEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/dome_capybara/dome_capybara.png");
    }

    @Override
    public Identifier getAnimationResource(DomeCapybaraAvatarEntity animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/dome_capybara.animation.json");
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(DomeCapybaraAvatarEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
