package net.vxr.vxrofmods.item.custom;

import com.google.common.base.Ticker;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vxr.vxrofmods.WW2ClientMod;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.block.entity.DiamondMinerBlockEntity;
import net.vxr.vxrofmods.block.entity.ModBlockEntities;
import net.vxr.vxrofmods.effect.JetPackEffect;
import net.vxr.vxrofmods.effect.ModEffects;
import net.vxr.vxrofmods.item.ModArmorMaterials;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Scanner;


public class ModChestplateItem extends ArmorItem {

    private static final Map<ArmorMaterial, StatusEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, StatusEffectInstance>())
                    .put(ModArmorMaterials.Dream,
                            new StatusEffectInstance(StatusEffects.SLOW_FALLING, 6, 1)).build();

    public ModChestplateItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);

    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient()) {
            if(entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)entity;

                if(hasChestplateOn(player)) {
                    evaluateArmorEffects(player);
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void evaluateArmorEffects(PlayerEntity player) {
        for (Map.Entry<ArmorMaterial, StatusEffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            StatusEffectInstance mapStatusEffect = entry.getValue();

            if(hasCorrectChestplateOn(mapArmorMaterial, player)) {
                addStatusEffectForMaterial(player, mapArmorMaterial, mapStatusEffect);
            } else if(!hasCorrectChestplateOn(mapArmorMaterial, player)) {
                removeStatusEffect(player, mapArmorMaterial, mapStatusEffect);
            }
        }
    }

    private void addStatusEffectForMaterial(PlayerEntity player, ArmorMaterial mapArmorMaterial, StatusEffectInstance mapStatusEffect) {
        boolean hasPlayerEffect = player.hasStatusEffect(mapStatusEffect.getEffectType());

        if(hasCorrectChestplateOn(mapArmorMaterial, player) && WW2ClientMod.isUseJetpack()  && !hasPlayerEffect) {
            player.addStatusEffect(new StatusEffectInstance(mapStatusEffect.getEffectType(),
                    mapStatusEffect.getDuration(), mapStatusEffect.getAmplifier()));

            // if(new Random().nextFloat() > 0.6f) { // 40% of damaging the armor! Possibly!
            //     player.getInventory().damageArmor(DamageSource.MAGIC, 1f, new int[]{0, 1, 2, 3});
            // }
        }
    }

    private void removeStatusEffect(PlayerEntity player, ArmorMaterial mapArmorMaterial, StatusEffectInstance mapStatusEffect) {
        boolean hasPlayerEffect = player.hasStatusEffect(mapStatusEffect.getEffectType());

        if(!hasCorrectChestplateOn(mapArmorMaterial, player) && hasPlayerEffect) {
            player.removeStatusEffect(mapStatusEffect.getEffectType());
        }
    }


    private boolean hasChestplateOn(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(2);

        return !chestplate.isEmpty();
    }

    private boolean hasCorrectChestplateOn(ArmorMaterial material, PlayerEntity player) {
        ArmorItem chestplate = ((ArmorItem)player.getInventory().getArmorStack(2).getItem());

        return chestplate.getMaterial() == material;
    }

}

