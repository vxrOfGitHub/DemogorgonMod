package net.vxr.vxrofmods.item.custom;

import com.google.common.base.Ticker;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
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

    public ModChestplateItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);

    }


}

