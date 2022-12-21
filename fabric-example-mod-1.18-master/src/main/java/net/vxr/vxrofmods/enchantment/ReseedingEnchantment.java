package net.vxr.vxrofmods.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class ReseedingEnchantment extends Enchantment {

    public ReseedingEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != ModEnchantments.LUCK_OF_THE_SEED;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        boolean isASwordItem = stack.getItem() instanceof SwordItem;
        return super.isAcceptableItem(stack) && !isASwordItem;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
