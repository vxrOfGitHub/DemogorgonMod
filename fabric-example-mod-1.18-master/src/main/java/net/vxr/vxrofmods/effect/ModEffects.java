package net.vxr.vxrofmods.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.vxr.vxrofmods.WW2Mod;

public class ModEffects {
    public static StatusEffect JETPACK;
    public static StatusEffect registerStatusEffect(String name) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(WW2Mod.MOD_ID, name),
                new JetPackEffect(StatusEffectCategory.BENEFICIAL, 3124687));
    }

    public static void registerEffects() {
        JETPACK = registerStatusEffect("jetpack");
    }

}
