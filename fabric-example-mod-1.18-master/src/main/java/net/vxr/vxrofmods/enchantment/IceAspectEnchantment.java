package net.vxr.vxrofmods.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.Objects;

public class IceAspectEnchantment extends Enchantment {
    public IceAspectEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        {
            return super.canAccept(other) && other != Enchantments.FIRE_ASPECT;
        }
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {

        if(!user.world.isClient()) {
            if (target instanceof LivingEntity) {
                if(level == 1) {
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 35, 1));
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 35));
                }
                if(level == 2) {
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 50, 2));
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 70));
                }
                if(level == 3) {
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 65, 3));
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 95));
                }
            }
        }

        if (target.world.isClient()) {
            Random random = target.world.getRandom();
            boolean bl = target.lastRenderX != target.getX() || target.lastRenderZ != target.getZ();
            if (bl && random.nextBoolean()) {
                target.world.addParticle(ParticleTypes.SNOWFLAKE, target.getX(), (double)(target.getBlockPos().getY() + 1), target.getBlockPos().getZ(), (double)(MathHelper.nextBetween(random, -1.0F, 1.0F) * 0.083333336F), 0.05000000074505806, (double)(MathHelper.nextBetween(random, -1.0F, 1.0F) * 0.083333336F));
            }
        }

        super.onTargetDamaged(user, target, level);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
