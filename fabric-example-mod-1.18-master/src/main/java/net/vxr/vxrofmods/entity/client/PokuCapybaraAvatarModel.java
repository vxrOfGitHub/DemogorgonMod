package net.vxr.vxrofmods.entity.client;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.PokuCapybaraAvatarEntity;
import net.vxr.vxrofmods.entity.custom.PokuCapybaraAvatarEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class PokuCapybaraAvatarModel extends AnimatedGeoModel<PokuCapybaraAvatarEntity> {
    @Override
    public Identifier getModelResource(PokuCapybaraAvatarEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/poku_capybara.geo.json");
    }

    @Override
    public Identifier getTextureResource(PokuCapybaraAvatarEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/entity/poku_capybara/poku_capybara.png");
    }

    @Override
    public Identifier getAnimationResource(PokuCapybaraAvatarEntity animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/poku_capybara.animation.json");
    }

    @Override
    public void setCustomAnimations(PokuCapybaraAvatarEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * MathHelper.RADIANS_PER_DEGREE);
            head.setRotationY(extraData.netHeadYaw * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
