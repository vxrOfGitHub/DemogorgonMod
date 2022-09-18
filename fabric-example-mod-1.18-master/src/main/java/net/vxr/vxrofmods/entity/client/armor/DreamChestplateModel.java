package net.vxr.vxrofmods.entity.client.armor;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.item.custom.ModChestplateItem;
import net.vxr.vxrofmods.item.custom.ModHelmetItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DreamChestplateModel extends AnimatedGeoModel<ModChestplateItem> {
    @Override
    public Identifier getModelResource(ModChestplateItem object) {
        return new Identifier(WW2Mod.MOD_ID, "geo/dream_chestplate.geo.json");
    }

    @Override
    public Identifier getTextureResource(ModChestplateItem object) {
        return new Identifier(WW2Mod.MOD_ID, "textures/models/armor/dream_chestplate.png");
    }

    @Override
    public Identifier getAnimationResource(ModChestplateItem animatable) {
        return new Identifier(WW2Mod.MOD_ID, "animations/dream_chestplate.animation.json");
    }
}
