package net.vxr.vxrofmods.item.custom;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DreamSword extends SwordItem {
    public DreamSword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    private static final int strongHitRarity = 3;

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasNbt()
                && stack.getNbt().getInt("dream_sword_strong_hit") >= strongHitRarity;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.vxrofmods.dream_sword.tooltip.shift"));
        } else {
            tooltip.add(Text.translatable("item.vxrofmods.dream_sword.tooltip"));
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        //target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 2), attacker);
        NbtCompound nbt = new NbtCompound();
        if(stack.hasNbt()) {
            nbt = stack.getNbt();
        }
        String strongHitKey = "dream_sword_strong_hit";
        /*int[] damageArray = nbt.getIntArray("dream_sword_recent_damage_made");
        List<Integer> damageList = new ArrayList<>();
        for(int i = 0; i < damageArray.length; i++) {
            damageList.add(damageArray[i]);
        }*/
        int mostRecentDamage = 0;
        if(target.getRecentDamageSource() != null
                && target.getRecentDamageSource().getAttacker() == attacker
                && target.getDamageTracker() != null
                && target.getDamageTracker().getMostRecentDamage() != null) {
            mostRecentDamage = ((int) (target.getDamageTracker().getMostRecentDamage().getDamage() * 1000));
            //damageList.add(mostRecentDamage);
        }
        float damage = mostRecentDamage;
        damage /= 1000;
        if(nbt.getInt(strongHitKey) >= strongHitRarity) {
            damage *= 2;
            target.damage(DamageSource.mob(attacker), damage);
            nbt.putInt(strongHitKey, 0);
        }
        /*
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
        nbt.putIntArray("dream_sword_recent_damage_made", damageList);*/
        nbt.putInt(strongHitKey, nbt.getInt(strongHitKey) + 1);
        /*System.out.println("Dream Sword Bleeding Stage = " + nbt.getInt(strongHitKey));
        System.out.println("Damage: " + damage);
        System.out.println("Target Health = " + target.getHealth());*/
        return super.postHit(stack, target, attacker);
    }
}
