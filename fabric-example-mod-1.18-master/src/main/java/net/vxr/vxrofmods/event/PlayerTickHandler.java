package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.vxr.vxrofmods.util.DreamBoostCooldownData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

public class PlayerTickHandler implements ServerTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if(DreamBoostCooldownData.getCooldown(((IEntityDataSaver) player)) > 0) {
                DreamBoostCooldownData.removeCooldownTick(((IEntityDataSaver) player));
                int tick2Secconds = DreamBoostCooldownData.getCooldown(((IEntityDataSaver) player)) / 20;
                int x = DreamBoostCooldownData.getCooldown(((IEntityDataSaver) player)) % 20;
                if(x <= 0) {
                    player.sendMessage(new LiteralText("§3Dream-Jump Cooldown§l: §5" +
                            tick2Secconds + "s§r"), true);
                }
            }
        }
    }
}
