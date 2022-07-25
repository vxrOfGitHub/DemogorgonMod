package net.vxr.vxrofmods.item.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.vxr.vxrofmods.WW2ClientMod;
import net.vxr.vxrofmods.item.ModArmorMaterials;

import java.util.Map;

public class DreamBootsItem extends ArmorItem {
    private static final Map<ArmorMaterial, StatusEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, StatusEffectInstance>())
                    .put(ModArmorMaterials.Dream,
                            new StatusEffectInstance(StatusEffects.LEVITATION, 15, 40)).build();

    public DreamBootsItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient()) {
            if(entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)entity;

                if(hasBootsOn(player)) {
                    if(dreamJumpCooldown > 0) {
                        int x = dreamJumpCooldown / 20;
                        player.sendMessage(new LiteralText("§3Dream-Jump Cooldown§l: §5" + x + "s§r"), true);
                        dreamJumpCooldown--;
                        if(dreamJumpCooldown == 0) {
                            player.sendMessage(new LiteralText("§2Dream-Jump Cooldown: finished§r"), true);
                        }
                    }
                    evaluateArmorEffects(player, world);
                } else {
                    WW2ClientMod.UseDreamBoots = false;
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void evaluateArmorEffects(PlayerEntity player, World world) {
        for (Map.Entry<ArmorMaterial, StatusEffectInstance> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            StatusEffectInstance mapStatusEffect = entry.getValue();
            if(hasCorrectBootsOn(mapArmorMaterial, player)) {
                addStatusEffectForMaterial(player, mapArmorMaterial, mapStatusEffect, world);
            } else {
                WW2ClientMod.UseDreamBoots = false;
            }
        }
    }

    private void addStatusEffectForMaterial(PlayerEntity player, ArmorMaterial mapArmorMaterial, StatusEffectInstance mapStatusEffect, World world) {
        boolean hasPlayerEffect = player.hasStatusEffect(mapStatusEffect.getEffectType());

        if(hasCorrectBootsOn(mapArmorMaterial, player) && WW2ClientMod.UseDreamBoots && player.isOnGround() && dreamJumpCooldown <= 0) {
            player.addStatusEffect(new StatusEffectInstance(mapStatusEffect.getEffectType(),
                    mapStatusEffect.getDuration(), mapStatusEffect.getAmplifier()));
            if(world.isClient()) {
                world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS,
                        1f, 1f);
            }
            dreamJumpCooldown = maxDreamJumpCooldown;
            WW2ClientMod.UseDreamBoots = false;

            // if(new Random().nextFloat() > 0.6f) { // 40% of damaging the armor! Possibly!
            //     player.getInventory().damageArmor(DamageSource.MAGIC, 1f, new int[]{0, 1, 2, 3});
            // }
        }
    }

    private boolean hasBootsOn(PlayerEntity player) {
        ItemStack boots = player.getInventory().getArmorStack(0);

        return !boots.isEmpty();
    }

    private boolean hasCorrectBootsOn(ArmorMaterial material, PlayerEntity player) {
        ArmorItem boots = ((ArmorItem)player.getInventory().getArmorStack(0).getItem());

        return boots.getMaterial() == material;
    }

    public static int dreamJumpCooldown = 5;
    private int maxDreamJumpCooldown = 400;

}