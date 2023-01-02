package net.vxr.vxrofmods.item.custom;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.networking.ModMessages;
import net.vxr.vxrofmods.util.DreamJetpackData;
import net.vxr.vxrofmods.util.IEntityDataSaver;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class ModChestplateItem extends ArmorItem implements IAnimatable{

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    /* private static final Map<ArmorMaterial, StatusEffectInstance> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, StatusEffectInstance>())
                    .put(ModArmorMaterials.Dream,
                            new StatusEffectInstance(StatusEffects.SLOW_FALLING, 6, 1)).build(); */

    public ModChestplateItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);

    }

    // Predicate runs every frame
    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        
        // This is all the extradata this event carries. The livingentity is the entity
        // that's wearing the armor. The itemstack and equipmentslottype are self
        // explanatory.
        LivingEntity livingEntity = event.getExtraDataOfType(LivingEntity.class).get(0);
        //System.out.println("------Jetpack is " + DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) livingEntity)));
        if(livingEntity instanceof PlayerEntity player) {
            ItemStack chestPlateStack = player.getInventory().getArmorStack(2);
            assert chestPlateStack.getNbt() != null;
            if(!chestPlateStack.getNbt().getBoolean("dream_jetpack_on") && event.getController().getAnimationState().equals(AnimationState.Stopped))
            {

                // Always loop the animation but later on in this method we'll decide whether or
                // not to actually play it
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dream_chestplate.standard", ILoopType.EDefaultLoopTypes.LOOP));


                // If the living entity is an armorstand just play the animation nonstop

                // Get all the equipment, aka the armor, currently held item, and offhand item
                List<Item> equipmentList = new ArrayList<>();
                player.getItemsEquipped().forEach((x) -> equipmentList.add(x.getItem()));

                // elements 2 to 6 are the armor so we take the sublist. Armorlist now only
                // contains the 4 armor slots
                List<Item> armorList = equipmentList.subList(2, 6);

                // Make sure the player is wearing all the armor. If they are, continue playing
                // the animation, otherwise stop
                boolean isWearingAll = new HashSet<>(armorList).contains(ModItems.Dream_Chestplate);
                return isWearingAll ? PlayState.CONTINUE : PlayState.STOP;
            }
        }
        return PlayState.STOP;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    // Predicate runs every frame
    private <P extends IAnimatable> PlayState flyPredicate(AnimationEvent<P> event) {
        // This is all the extradata this event carries. The livingentity is the entity
        // that's wearing the armor. The itemstack and equipmentslottype are self
        // explanatory.
        LivingEntity livingEntity = event.getExtraDataOfType(LivingEntity.class).get(0);

        if(livingEntity instanceof PlayerEntity player) {
            ItemStack chestPlateStack = player.getInventory().getArmorStack(2);
            assert chestPlateStack.getNbt() != null;
            if (chestPlateStack.getNbt().getBoolean("dream_jetpack_on")) {
                // Always loop the animation but later on in this method we'll decide whether or
                // not to actually play it
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dream_chestplate.flying", ILoopType.EDefaultLoopTypes.LOOP));


                // If the living entity is an armorstand just play the animation nonstop

                // Get all the equipment, aka the armor, currently held item, and offhand item
                List<Item> equipmentList = new ArrayList<>();
                player.getItemsEquipped().forEach((x) -> equipmentList.add(x.getItem()));

                // elements 2 to 6 are the armor so we take the sublist. Armorlist now only
                // contains the 4 armor slots
                List<Item> armorList = equipmentList.subList(2, 6);

                // Make sure the player is wearing all the armor. If they are, continue playing
                // the animation, otherwise stop
                boolean isWearingAll = armorList.containsAll(Arrays.asList(ModItems.Dream_Chestplate));
                return isWearingAll ? PlayState.CONTINUE : PlayState.STOP;
            }
        }
        return PlayState.CONTINUE;
    }

    // All you need to do here is add your animation controllers to the
    // AnimationData
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 10, this::predicate));
        data.addAnimationController(new AnimationController(this, "flyController", 10, this::flyPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    /*
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
            }
        }
    }

    private void addStatusEffectForMaterial(PlayerEntity player, ArmorMaterial mapArmorMaterial, StatusEffectInstance mapStatusEffect) {
        boolean hasPlayerEffect = player.hasStatusEffect(mapStatusEffect.getEffectType());
        boolean hasPlayerEffectLevitation = player.hasStatusEffect(StatusEffects.LEVITATION);
        if(hasCorrectChestplateOn(mapArmorMaterial, player) && WW2ClientMod.isUseJetpack()) {

            if(hasCorrectChestplateOn(mapArmorMaterial, player) && WW2ClientMod.isUseJetpack()  && !hasPlayerEffect && player.isInSneakingPose()) {
                player.setNoGravity(false);
                player.addStatusEffect(new StatusEffectInstance(mapStatusEffect.getEffectType(),
                        mapStatusEffect.getDuration(), mapStatusEffect.getAmplifier()));
            } else if (hasCorrectChestplateOn(mapArmorMaterial, player) && WW2ClientMod.isUseJetpack() && WW2ClientMod.isUseJetpackLifter() && !hasPlayerEffectLevitation) {
                player.setNoGravity(false);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 3, 5));
                WW2ClientMod.setUseJetpackLifter(false);
            } else if(hasCorrectChestplateOn(mapArmorMaterial, player) && WW2ClientMod.isUseJetpack() && !player.isInSneakingPose() && !hasPlayerEffectLevitation) {
                player.setNoGravity(true);
                player.addStatusEffect(new StatusEffectInstance(mapStatusEffect.getEffectType(),
                        mapStatusEffect.getDuration(), mapStatusEffect.getAmplifier()));
            } else {
                player.setNoGravity(false);
            }
        } else {
            player.setNoGravity(false);
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
    } */

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        /*if(!world.isClient() && entity instanceof PlayerEntity player) {
            if(!hasChestplateOn(player) && DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)) || !hasCorrectChestplateOn(this.getMaterial(), player)) {
                player.setNoGravity(false);
                DreamJetpackData.setJetpackOnOff(((IEntityDataSaver) player), false);
            }
        } */
        if(entity instanceof PlayerEntity player && hasCorrectChestplateOn(player, stack.getItem())
                //&& DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player))
        ) {
            if(!world.isClient() && DreamJetpackData.getJetpackOnOff(((IEntityDataSaver)player))) {
                System.out.println("Jetpack Is On!");
                ServerPlayNetworking.send(((ServerPlayerEntity) player), ModMessages.DREAM_JETPACK_PARTICLE_SPAWN, PacketByteBufs.create());
            }
        }



        super.inventoryTick(stack, world, entity, slot, selected);
    }
    private boolean hasChestplateOn(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(2);

        return !chestplate.isEmpty();
    }

    private boolean hasCorrectChestplateOn(PlayerEntity player, Item thisItem) {
        return player.getInventory().getArmorStack(2).isOf(thisItem);
    }

}

