package net.vxrofmods.demogorgonmod.entity.client;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonPortalEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class DemogorgonPortalModel extends GeoModel<DemogorgonPortalEntity> {
    @Override
    public Identifier getModelResource(DemogorgonPortalEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "geo/demogorgon_portal.geo.json");
    }

    @Override
    public Identifier getTextureResource(DemogorgonPortalEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "textures/entity/demogorgon_portal.png");
    }

    @Override
    public Identifier getAnimationResource(DemogorgonPortalEntity animatable) {
        return new Identifier(DemogorgonMod.MOD_ID, "animations/demogorgon_portal.animation.json");
    }

    @Override
    public void setCustomAnimations(DemogorgonPortalEntity animatable, long instanceId, AnimationState<DemogorgonPortalEntity> animationState) {

        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
