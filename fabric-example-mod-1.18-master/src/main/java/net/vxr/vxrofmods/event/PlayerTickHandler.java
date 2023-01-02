package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vxr.vxrofmods.item.ModArmorMaterials;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.item.custom.ModHelmetItem;
import net.vxr.vxrofmods.networking.ModMessages;
import net.vxr.vxrofmods.util.DreamBoostCooldownData;
import net.vxr.vxrofmods.util.DreamHelmetData;
import net.vxr.vxrofmods.util.DreamJetpackData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

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
            if(!hasCorrectChestplateOn) {
                player.setNoGravity(false);
                DreamJetpackData.setJetpackOnOff(playerSaver, false);
                //player.speed = DreamJetpackData.getEarlierForwardSpeed(playerSaver);
            } else {
                player.setNoGravity(true);
                ItemStack chestplate = player.getInventory().getArmorStack(2);

                if(!chestplate.hasNbt()) {
                    chestplate.setNbt(new NbtCompound());
                    assert chestplate.getNbt() != null;
                    chestplate.getNbt().putInt("durabilityTick", 0);
                } else {
                    assert chestplate.getNbt() != null;
                    chestplate.getNbt().putInt("durabilityTick", chestplate.getNbt().getInt("durabilityTick") + 1);
                }
                if(chestplate.getNbt().getInt("durabilityTick") >= 40) {
                    chestplate.setDamage(chestplate.getDamage() + 1);
                    chestplate.getNbt().putInt("durabilityTick",0);
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
