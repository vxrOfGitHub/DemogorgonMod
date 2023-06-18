package net.vxrofmods.demogorgonmod.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.vxrofmods.demogorgonmod.item.ModItems;
import net.vxrofmods.demogorgonmod.util.DemogorgonData;
import net.vxrofmods.demogorgonmod.util.IEntityDataSaver;

import java.util.UUID;

public class ServerPlayerTickHandler implements ServerTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

            checkDemogorgonHeadAbility(player);

        }
    }

    private void checkDemogorgonHeadAbility(ServerPlayerEntity player) {

        IEntityDataSaver playerSaver = ((IEntityDataSaver) player);
        ServerWorld world = player.getServerWorld();



        // Despawn Dog if Player doesn't have Demogorgon Head on
        // Switch Player Stages if dog dies
        // Cooldown when in Stage 2

        if(DemogorgonData.getDemogorgonHeadUserState(playerSaver) == 1) {
            Entity entity = null;
            UUID uuid = DemogorgonData.readDogsUUIDFromOwner(playerSaver);
            if(uuid != null && !DemogorgonData.isDogsUUIDFromOwnerNull(playerSaver)) {
                entity = world.getEntity(uuid);
            }
            if(entity == null) {
                DemogorgonData.setDemogorgonHeadUserState(playerSaver, 2);
            } else {
                if(entity.isAlive() && !player.getInventory().getArmorStack(3).isOf(ModItems.DEMOGORGON_HEAD_HELMET)
                || !entity.isAlive()) {
                    entity.kill();
                    DemogorgonData.setDemogorgonHeadUserState(playerSaver, 2);
                }
            }
        } else if (DemogorgonData.getDemogorgonHeadUserState(playerSaver) == 2) {
            int cooldown = DemogorgonData.getDogSpawnCooldownForHelmet(playerSaver);
            if(cooldown >= DemogorgonData.maxDogSpawnCooldownForHelmet) {
                DemogorgonData.setDemogorgonHeadUserState(playerSaver, 0);
                DemogorgonData.setDogSpawnCooldownForHelmet(playerSaver, 0);
            } else {
                cooldown++;
                DemogorgonData.setDogSpawnCooldownForHelmet(playerSaver, cooldown);
            }
        }
    }
}
