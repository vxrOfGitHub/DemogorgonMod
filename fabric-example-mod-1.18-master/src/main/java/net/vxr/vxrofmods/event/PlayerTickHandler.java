package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.networking.ModMessages;
import net.vxr.vxrofmods.util.*;

import static org.apache.commons.lang3.RandomUtils.nextFloat;

public class PlayerTickHandler implements ServerTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            DreamBoostTick(player);
            DreamJetpackTick(player);
            DreamHelmetTick(player);
        }
    }

    private void DreamHelmetTick(ServerPlayerEntity player) {
        boolean hasHelmetOn = !player.getInventory().getArmorStack(3).isEmpty();
        boolean hasEffect = player.hasStatusEffect(StatusEffects.NIGHT_VISION);
           if(!hasHelmetOn && DreamHelmetData.hadHelmetOn(((IEntityDataSaver) player)) && hasEffect) {
               player.removeStatusEffect(StatusEffects.NIGHT_VISION);
               DreamHelmetData.setHadHelmetOn(((IEntityDataSaver) player), false);
        }
    }

    private void DreamJetpackTick(ServerPlayerEntity player) {
        //boolean hasChestplateOn = !player.getInventory().getArmorStack(2).isEmpty();
        boolean hasCorrectChestplateOn = player.getInventory().getArmorStack(2).getItem().equals(ModItems.Dream_Chestplate);
        IEntityDataSaver playerSaver = ((IEntityDataSaver) player);
        if(DreamJetpackData.getJetpackOnOff(playerSaver)) {
            if(!hasCorrectChestplateOn || !InventoryUtil.hasPlayerStackInInventory(player, Items.FIREWORK_ROCKET)) {
                player.setNoGravity(false);
                DreamJetpackData.setJetpackOnOff(playerSaver, false);
                //player.speed = DreamJetpackData.getEarlierForwardSpeed(playerSaver);
            } else if(InventoryUtil.hasPlayerStackInInventory(player, Items.FIREWORK_ROCKET)) {
                player.setNoGravity(true);
                /*ItemStack chestplate = player.getInventory().getArmorStack(2);
                NbtCompound nbt = new NbtCompound();
                if(chestplate.hasNbt()) {
                    nbt = chestplate.getNbt();
                } else {
                    nbt.putInt("durabilityTick", 0);
                }
                assert nbt != null;
                nbt.putInt("durabilityTick", nbt.getInt("durabilityTick") + 1);
                chestplate.setNbt(nbt);
                assert chestplate.getNbt() != null;
                if(chestplate.getNbt().getInt("durabilityTick") % 160 == 0) {
                    if(InventoryUtil.hasPlayerStackInInventory(player, Items.FIREWORK_ROCKET)) {
                        player.getInventory().getStack(InventoryUtil.getFirstInventoryIndex(player, Items.FIREWORK_ROCKET)).decrement(1);
                        if(InventoryUtil.getAmountOfItemInInventory(player, Items.FIREWORK_ROCKET) <= 10) {
                            player.sendMessage(Text.literal("§c§lLOW ON ROCKETS: " + InventoryUtil.getAmountOfItemInInventory(player, Items.FIREWORK_ROCKET) + "!§r"), true);
                        }
                        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS   ,
                                0.125F, nextFloat(0F, 1F));
                    }
                }*/
                DreamJetpackData.setFuelTick(playerSaver, DreamJetpackData.getFuelTick(playerSaver) + 1);
                System.out.println("FuelTick: " + DreamJetpackData.getFuelTick(playerSaver));
                int remainder = 160;
                if(DreamJetpackData.getFuelTick(playerSaver) % remainder == 0) {
                    if(InventoryUtil.hasPlayerStackInInventory(player, Items.FIREWORK_ROCKET)) {
                        player.getInventory().getStack(InventoryUtil.getFirstInventoryIndex(player, Items.FIREWORK_ROCKET)).decrement(1);
                        if(InventoryUtil.getAmountOfItemInInventory(player, Items.FIREWORK_ROCKET) <= 10) {
                            player.sendMessage(Text.literal("§c§lLOW ON ROCKETS: " + InventoryUtil.getAmountOfItemInInventory(player, Items.FIREWORK_ROCKET) + "!§r"), true);
                        }
                        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS,
                                0.175F, nextFloat(0F, 1F));
                    }
                    DreamJetpackData.setFuelTick(playerSaver, 0);
                }
            }
        }

        // Jetpack Down
        /* if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)) && player.isSneaking() &&
                !DreamJetpackData.getJetpackUp(((IEntityDataSaver) player))) {
            player.setNoGravity(false);
            DreamJetpackData.setJetpackUp(((IEntityDataSaver) player), false);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 6, 1));
            DreamJetpackData.setJetpackUp(((IEntityDataSaver) player), false);
        //Jetpack Stay
        } if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)) && !player.isSneaking() &&
                !DreamJetpackData.getJetpackUp(((IEntityDataSaver) player))) {
            DreamJetpackData.setJetpackUp(((IEntityDataSaver) player), false);
            player.setNoGravity(true);
        // Jetpack up
        } if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)) && !player.isSneaking() &&
                DreamJetpackData.getJetpackUp(((IEntityDataSaver) player))) {
            player.setNoGravity(false);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 3, 5));
        // Spawn Jetpack Particles
        } if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player))) {
            spawnJetpackParticles(player);
        // Take effects away when putting off chestplate
        } if(!hasChestplateOn || !hasCorrectChestplateOn) {
            if(DreamJetpackData.hadJetpackOn(((IEntityDataSaver) player))) {
                player.setNoGravity(false);
                player.removeStatusEffect(StatusEffects.LEVITATION);
                DreamJetpackData.setJetpackUp(((IEntityDataSaver) player), false);
                DreamJetpackData.setJetpackOnOff(((IEntityDataSaver) player), false);
                DreamJetpackData.setHadJetpackOn(((IEntityDataSaver) player), false);
                System.out.println("Jetpack took effects from " + player.getName().getString());
            }

        }*/
    }

    private void spawnJetpackParticles(PlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(player.getBlockPos());
        ServerPlayNetworking.send(((ServerPlayerEntity) player), ModMessages.DREAM_JETPACK_PARTICLE_SPAWN, buf);
    }

    private void DreamBoostTick(ServerPlayerEntity player) {
        if(DreamBoostCooldownData.getCooldown(((IEntityDataSaver) player)) > 0) {
            DreamBoostCooldownData.removeCooldownTick(((IEntityDataSaver) player));
            int tick2Secconds = DreamBoostCooldownData.getCooldown(((IEntityDataSaver) player)) / 20;
            int x = DreamBoostCooldownData.getCooldown(((IEntityDataSaver) player)) % 20;
            if(x <= 0) {
                player.sendMessage(Text.literal("§3Dream-Boost Cooldown§l: §5" +
                        tick2Secconds + "s§r"), true);
            }
        }
    }

}
