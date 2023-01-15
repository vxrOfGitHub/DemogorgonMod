package net.vxr.vxrofmods.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

public class DreamSword extends SwordItem {
    public DreamSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }


    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasNbt()
                && stack.getNbt().getInt("dream_sword_bleeding") >= 5;
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        //target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 2), attacker);
        NbtCompound nbt = new NbtCompound();
        if(stack.hasNbt()) {
            nbt = stack.getNbt();
        }
        int[] damageArray = nbt.getIntArray("dream_sword_recent_damage_made");
        List<Integer> damageList = new ArrayList<>();
        for(int i = 0; i < damageArray.length; i++) {
            damageList.add(damageArray[i]);
        }
        if(target.getRecentDamageSource() != null
                && target.getRecentDamageSource().getAttacker() == attacker) {
            int mostRecentDamage = ((int) (target.getDamageTracker().getMostRecentDamage().getDamage() * 1000));
            damageList.add(mostRecentDamage);
        }
        if(nbt.getInt("dream_sword_bleeding") >= 5 && target.canTakeDamage()) {
            float damage = 0;
            for (int i = 0; i < damageList.size(); i++) {
                damage += damageList.get(i);
            }
            damage /= 1000;
            System.out.println("Damage = " + damage);
            target.damage(DamageSource.mob(attacker), damage);
            damageList.clear();
            nbt.putInt("dream_sword_bleeding", 0);
        }
        nbt.putIntArray("dream_sword_recent_damage_made", damageList);
        nbt.putInt("dream_sword_bleeding", nbt.getInt("dream_sword_bleeding") + 1);
        System.out.println("Dream Sword Bleeding Stage = " + nbt.getInt("dream_sword_bleeding"));
        System.out.println("Target Health = " + target.getHealth());
        return super.postHit(stack, target, attacker);
    }
}
