package net.vxr.vxrofmods.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

import java.util.Objects;

public class LifeStealEnchantment extends Enchantment {
    public LifeStealEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        {
            return super.canAccept(other) && other != Enchantments.SWEEPING;
        }
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {

        if(!user.world.isClient()) {

            float damage = Objects.requireNonNull(((LivingEntity) target).getDamageTracker().getMostRecentDamage()).getDamage();

            if(level == 1) {

                float healAmount = damage * 0.06F;

                if(user.getHealth() < user.getMaxHealth()) {

                    if(user.getHealth() + healAmount > user.getMaxHealth()) {

                        user.setHealth(user.getMaxHealth());

                    } else {

                        user.setHealth(user.getHealth() + healAmount);

                    }
                }
            } if(level == 2) {

                float healAmount = damage * 0.08F;

                if(user.getHealth() < user.getMaxHealth()) {

                    if(user.getHealth() + healAmount > user.getMaxHealth()) {

                        user.setHealth(user.getMaxHealth());

                    } else {

                        user.setHealth(user.getHealth() + healAmount);

                    }
                }
            } if(level >= 3) {

                float healAmount = damage * 0.1F;

                if(user.getHealth() < user.getMaxHealth()) {

                    if(user.getHealth() + healAmount > user.getMaxHealth()) {

                        user.setHealth(user.getMaxHealth());

                    } else {

                        user.setHealth(user.getHealth() + healAmount);

                    }
                }
            }




        }

        super.onTargetDamaged(user, target, level);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
