package net.vxr.vxrofmods.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {

    public static final FoodComponent WAFFLE = (new FoodComponent.Builder()).hunger(6).saturationModifier(1.2F)
            .statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 0, 1), 1f).build();

}

