package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.vxr.vxrofmods.util.IEntityDataSaver;
import net.vxr.vxrofmods.util.MissionsData;

public class KillEntityHandler implements ServerEntityCombatEvents.AfterKilledOtherEntity{
    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        if(!world.isClient() && entity instanceof PlayerEntity) {

            IEntityDataSaver player = ((IEntityDataSaver) entity);


            if(MissionsData.getDailyMissionType1(player) == 1) {

                EntityType<?> killedEntityType = killedEntity.getType();

                if(killedEntityType.equals(ServerTickHandler.mobsForDailyMission.get(MissionsData.getDailyMission1(player)))) {

                    MissionsData.addDailyMissionProgress(player, 1, 1);
                }
            } if(MissionsData.getDailyMissionType2(player) == 1) {

                EntityType<?> killedEntityType = killedEntity.getType();

                if(killedEntityType.equals(ServerTickHandler.mobsForDailyMission.get(MissionsData.getDailyMission2(player)))) {

                    MissionsData.addDailyMissionProgress(player, 2, 1);
                }
            } if(MissionsData.getDailyMissionType3(player) == 1) {

                EntityType<?> killedEntityType = killedEntity.getType();

                if(killedEntityType.equals(ServerTickHandler.mobsForDailyMission.get(MissionsData.getDailyMission3(player)))) {

                    MissionsData.addDailyMissionProgress(player, 3, 1);
                }
            }
        }
    }
}
