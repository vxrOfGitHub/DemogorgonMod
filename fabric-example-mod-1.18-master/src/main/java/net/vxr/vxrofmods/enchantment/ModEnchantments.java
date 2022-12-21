package net.vxr.vxrofmods.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;

import net.minecraft.util.registry.Registry;

public class ModEnchantments {

    public static Enchantment LIFESTEAL = register("lifesteal",
            new LifeStealEnchantment(Enchantment.Rarity.VERY_RARE,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));

    public static Enchantment ICE_ASPECT = register("ice_aspect",
            new IceAspectEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND));

    public static Enchantment RESEEDING = register("reseeding",
            new ReseedingEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND));
    public static Enchantment LUCK_OF_THE_SEED = register("luck_of_the_seed",
            new LuckOfTheSeedEnchantment(Enchantment.Rarity.UNCOMMON,
                    EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND));


    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registry.ENCHANTMENT, new Identifier(WW2Mod.MOD_ID, name), enchantment);
    }

    public static void registerModEnchantments() {
        System.out.println("Registering Enchantments for " + WW2Mod.MOD_ID);
    }

}
