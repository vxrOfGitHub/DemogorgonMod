package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vxr.vxrofmods.item.custom.ModHelmetItem;
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
        if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)) && player.isSneaking() &&
                !DreamJetpackData.getJetpackUp(((IEntityDataSaver) player))) {
            player.setNoGravity(false);
            DreamJetpackData.setJetpackUp(((IEntityDataSaver) player), false);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 6, 1));
            DreamJetpackData.setJetpackUp(((IEntityDataSaver) player), false);
        } if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)) && !player.isSneaking() &&
                !DreamJetpackData.getJetpackUp(((IEntityDataSaver) player))) {
            DreamJetpackData.setJetpackUp(((IEntityDataSaver) player), false);
            player.setNoGravity(true);
        } if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)) && !player.isSneaking() &&
                DreamJetpackData.getJetpackUp(((IEntityDataSaver) player))) {
            player.setNoGravity(false);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 3, 5));
        } if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player))) {
            spawnJetpackParticles(player.getWorld(), player.getBlockPos());
        }
    }

    private void spawnJetpackParticles(World world, BlockPos playerPos) {
        for(int i = 0; i < 360; i++) {
            if(i % 20 == 0) {
                world.addParticle(ParticleTypes.FLAME,
                        playerPos.getX() + 0.5d, playerPos.getY() + 1, playerPos.getZ() + 0.5d,
                        Math.cos(i) * 0.25d, 0.15d, Math.sin(i) * 0.25d);
            }
        }
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
