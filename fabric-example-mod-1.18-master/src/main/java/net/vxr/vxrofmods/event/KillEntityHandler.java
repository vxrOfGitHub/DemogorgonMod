package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.vxr.vxrofmods.command.MissionsCommand;
import net.vxr.vxrofmods.util.CustomMoneyData;
import net.vxr.vxrofmods.util.IEntityDataSaver;
import net.vxr.vxrofmods.util.MissionsData;
import net.vxr.vxrofmods.util.MissionsWeeklyData;

public class KillEntityHandler implements ServerEntityCombatEvents.AfterKilledOtherEntity{
    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {

        dailyMissionProgress(world, entity, killedEntity);
        weeklyMissionProgress(world, entity, killedEntity);

    }

    private static void dailyMissionProgress(ServerWorld world, Entity entity, LivingEntity killedEntity) {

        if(!world.isClient() && entity instanceof PlayerEntity) {

            IEntityDataSaver player = ((IEntityDataSaver) entity);


            if(MissionsData.getDailyMissionType1(player) == 1) {

                EntityType<?> killedEntityType = killedEntity.getType();

                if(killedEntityType.equals(ServerTickHandler.mobsForDailyMission.get(MissionsData.getDailyMission1(player)))) {

                    MissionsData.addDailyMissionProgress(player, 1, 1);
                    if(MissionsData.getDailyMissionProgress(player, 1) ==
                            ServerTickHandler.amountOfMobToKillForDailyMission.get(MissionsData.getDailyMission1(player))) {

                        MissionsData.setDailyMissionComplete(((IEntityDataSaver) entity), 1, true);
                        entity.sendMessage(Text.literal("§aCongratulations! You completed Mission " + 1 + " !§r"));
                        entity.sendMessage(Text.literal("§bREWARD:§r §6§l" + MissionsCommand.rewardAmountDailyMission + " Coins§r§r"));
                    }
                }
            } if(MissionsData.getDailyMissionType2(player) == 1) {

                EntityType<?> killedEntityType = killedEntity.getType();

                if(killedEntityType.equals(ServerTickHandler.mobsForDailyMission.get(MissionsData.getDailyMission2(player)))) {

                    MissionsData.addDailyMissionProgress(player, 2, 1);
                    if(MissionsData.getDailyMissionProgress(player, 2) ==
                            ServerTickHandler.amountOfMobToKillForDailyMission.get(MissionsData.getDailyMission2(player))) {

                        MissionsData.setDailyMissionComplete(((IEntityDataSaver) entity), 2, true);
                        entity.sendMessage(Text.literal("§aCongratulations! You completed Mission " + 2 + " !§r"));
                        entity.sendMessage(Text.literal("§bREWARD:§r §6§l" + MissionsCommand.rewardAmountDailyMission + " Coins§r§r"));
                    }
                }
            } if(MissionsData.getDailyMissionType3(player) == 1) {

                EntityType<?> killedEntityType = killedEntity.getType();

                if(killedEntityType.equals(ServerTickHandler.mobsForDailyMission.get(MissionsData.getDailyMission3(player)))) {

                    MissionsData.addDailyMissionProgress(player, 3, 1);
                    if(MissionsData.getDailyMissionProgress(player, 3) ==
                            ServerTickHandler.amountOfMobToKillForDailyMission.get(MissionsData.getDailyMission3(player))) {

                        MissionsData.setDailyMissionComplete(((IEntityDataSaver) entity), 3, true);
                        entity.sendMessage(Text.literal("§aCongratulations! You completed Mission " + 3 + " !§r"));
                        entity.sendMessage(Text.literal("§bREWARD:§r §6§l" + MissionsCommand.rewardAmountDailyMission + " Coins§r§r"));
                    }
                }
            }
        }
    }

    private static void weeklyMissionProgress(ServerWorld world, Entity entity, LivingEntity killedEntity) {

        if(!world.isClient() && entity instanceof PlayerEntity) {

            IEntityDataSaver player = ((IEntityDataSaver) entity);


            if(MissionsWeeklyData.getWeeklyMissionType1(player) == 1) {

                EntityType<?> killedEntityType = killedEntity.getType();

                if(killedEntityType.equals(ServerTickHandler.mobsForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission1(player)))) {

                    MissionsWeeklyData.addWeeklyMissionProgress(player, 1, 1);
                    if(MissionsWeeklyData.getWeeklyMissionProgress(player, 1) ==
                            ServerTickHandler.amountOfMobToKillForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission1(player))) {

                        MissionsWeeklyData.setWeeklyMissionComplete(((IEntityDataSaver) entity), 1, true);
                        entity.sendMessage(Text.literal("§aCongratulations! You completed Mission " + 1 + " !§r"));
                        entity.sendMessage(Text.literal("§3REWARD:§r §6§l" + MissionsCommand.rewardAmountWeeklyMission + " Coins§r§r"));
                    }
                }
            } if(MissionsWeeklyData.getWeeklyMissionType2(player) == 1) {

                EntityType<?> killedEntityType = killedEntity.getType();

                if(killedEntityType.equals(ServerTickHandler.mobsForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission2(player)))) {

                    MissionsWeeklyData.addWeeklyMissionProgress(player, 2, 1);
                    if(MissionsWeeklyData.getWeeklyMissionProgress(player, 2) ==
                            ServerTickHandler.amountOfMobToKillForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission2(player))) {

                        MissionsWeeklyData.setWeeklyMissionComplete(((IEntityDataSaver) entity), 2, true);
                        entity.sendMessage(Text.literal("§aCongratulations! You completed Mission " + 2 + " !§r"));
                        entity.sendMessage(Text.literal("§3REWARD:§r §6§l" + MissionsCommand.rewardAmountWeeklyMission + " Coins§r§r"));
                    }
                }
            } if(MissionsWeeklyData.getWeeklyMissionType3(player) == 1) {

                EntityType<?> killedEntityType = killedEntity.getType();

                if(killedEntityType.equals(ServerTickHandler.mobsForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission3(player)))) {

                    MissionsWeeklyData.addWeeklyMissionProgress(player, 3, 1);
                    if(MissionsWeeklyData.getWeeklyMissionProgress(player, 3) ==
                            ServerTickHandler.amountOfMobToKillForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission3(player))) {

                        MissionsWeeklyData.setWeeklyMissionComplete(((IEntityDataSaver) entity), 3, true);
                        entity.sendMessage(Text.literal("§aCongratulations! You completed Mission " + 3 + " !§r"));
                        entity.sendMessage(Text.literal("§3REWARD:§r §6§l" + MissionsCommand.rewardAmountWeeklyMission + " Coins§r§r"));
                    }
                }
            }
        }
    }

}
