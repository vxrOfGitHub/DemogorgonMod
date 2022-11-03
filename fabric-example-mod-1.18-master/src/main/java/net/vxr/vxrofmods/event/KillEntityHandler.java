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

            for (int i = 1; i <= 3; i++) {

                int x = MissionsData.getDailyMissionOfNumber(player, i);

                if(!MissionsData.isDailyMissionOfNumberTypeItem(player, i)) {

                    EntityType<?> killedEntityType = killedEntity.getType();

                    if(killedEntityType.equals(ServerTickHandler.mobsForDailyMission.get(x))) {

                        MissionsData.addDailyMissionProgress(player, i, 1);
                    }
                }
            }
        }
    }
}
