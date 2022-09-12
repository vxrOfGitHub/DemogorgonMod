package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.vxr.vxrofmods.util.DreamBoostCooldownData;
import net.vxr.vxrofmods.util.DreamJetpackData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

public class PlayerTickHandler implements ServerTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            DreamBoostTick(player);
            DreamJetpackTick(player);
        }
    }

    private void DreamJetpackTick(ServerPlayerEntity player) {
        if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)) && player.isSneaking() &&
                !DreamJetpackData.getJetpackUp(((IEntityDataSaver) player))) {
            player.setNoGravity(false);
            DreamJetpackData.setJetpackUp(((IEntityDataSaver) player), false);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 6, 1));
        } if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)) && !player.isSneaking() &&
                !DreamJetpackData.getJetpackUp(((IEntityDataSaver) player))) {
            DreamJetpackData.setJetpackUp(((IEntityDataSaver) player), false);
            player.setNoGravity(true);
        } if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)) && !player.isSneaking() &&
                DreamJetpackData.getJetpackUp(((IEntityDataSaver) player))) {
            player.setNoGravity(false);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 3, 5));
        }
    }

    private void DreamBoostTick(ServerPlayerEntity player) {
        if(DreamBoostCooldownData.getCooldown(((IEntityDataSaver) player)) > 0) {
            DreamBoostCooldownData.removeCooldownTick(((IEntityDataSaver) player));
            int tick2Secconds = DreamBoostCooldownData.getCooldown(((IEntityDataSaver) player)) / 20;
            int x = DreamBoostCooldownData.getCooldown(((IEntityDataSaver) player)) % 20;
            if(x <= 0) {
                player.sendMessage(new LiteralText("§3Dream-Boost Cooldown§l: §5" +
                        tick2Secconds + "s§r"), true);
            }
        }
    }

}
