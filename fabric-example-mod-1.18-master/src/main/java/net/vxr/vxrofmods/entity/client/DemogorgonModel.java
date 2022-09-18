package net.vxr.vxrofmods.entity.client;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.DemogorgonEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class DemogorgonModel extends AnimatedGeoModel<DemogorgonEntity> {
    @Override
    public Identifier getModelResource(DemogorgonEntity object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/demogorgon.geo.json");
    }

    @Override
    public Identifier getTextureResource(DemogorgonEntity object) {
        return DemogorgonRenderer.LOCATION_BY_VARIANT.get(object.getVariant());
    }

    @Override
    public Identifier getAnimationResource(DemogorgonEntity animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/demogorgon.animation.json");
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setLivingAnimations(DemogorgonEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
