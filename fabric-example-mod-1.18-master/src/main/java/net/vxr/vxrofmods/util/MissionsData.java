package net.vxr.vxrofmods.util;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.DistancePredicate;
import net.vxr.vxrofmods.event.ServerTickHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public class MissionsData {


    public static String getDailyMissionTime(IEntityDataSaver player) {

        NbtCompound nbt = player.getPersistentData();

        int ticks2Hour = nbt.getInt("daily_mission_time") / 72000;
        int ticks2Min = nbt.getInt("daily_mission_time") % 72000 / 1200;
        int ticks2Sec = nbt.getInt("daily_mission_time") % 72000 % 1200 / 20;

        String ticks2Time = ticks2Hour + "h " + ticks2Min + "min " + ticks2Sec + "sec";

        return ticks2Time;

    }

    public static void setDailyMissionTime(IEntityDataSaver player, int dailyMissionTime) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("daily_mission_time", dailyMissionTime);

    }

    public static String getWeeklyMissionTime(IEntityDataSaver player) {

        NbtCompound nbt = player.getPersistentData();

        int ticks2Day = nbt.getInt("weekly_mission_time") / 1728000;
        int ticks2Hour = nbt.getInt("weekly_mission_time") % 1728000 / 72000;
        int ticks2Min = nbt.getInt("weekly_mission_time") % 1728000 % 72000 / 1200;


        String ticks2Time = ticks2Day + "day(s)" + ticks2Hour + "h " + ticks2Min + "min ";

        return ticks2Time;

    }

    public static void setWeeklyMissionTime(IEntityDataSaver player, int weeklyMissionTime) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("weekly_mission_time", weeklyMissionTime);

    }

    public static void setRandomDailyMissions(IEntityDataSaver player, List<ItemStack> dailyMissionItems, List<EntityType> dailyMissionMobs) {

        NbtCompound nbt = player.getPersistentData();

        int y1 = nextInt(0, dailyMissionItems.size());
        int y2 = nextInt(0, dailyMissionItems.size());
        int y3 = nextInt(0, dailyMissionItems.size());

        int z1 = nextInt(0, dailyMissionMobs.size());
        int z2 = nextInt(0, dailyMissionMobs.size());
        int z3 = nextInt(0, dailyMissionMobs.size());

        for(int i = 1; i <= 3; i++) {
            int x = nextInt(0, 2);
            if(x < 1) {
                int x1 = alwaysDifferentMissions(i,dailyMissionItems.size(), y1, y2, y3);
                setAllDailyMissions(player, i, x1);
                System.out.println("--------Random: war ein Item: " + x1);
                System.out.println("--------Item welches Item: " +
                        ServerTickHandler.itemsForDailyMission.get(x1));
                setDailyMissionOfNumberTypeItem(player, i, true);

                System.out.println("-------GetMethod bekommt die Zahl: " + getAllDailyMissions(player, i));

            } else {
                int x1 = alwaysDifferentMissions(i,dailyMissionMobs.size(), z1, z2, z3);
                setAllDailyMissions(player, i, x1);
                System.out.println("--------Random: war ein Mob: " + x1);
                System.out.println("--------Mob war " + ServerTickHandler.mobsForDailyMission.get(x1));
                setDailyMissionOfNumberTypeItem(player, i, false);



                System.out.println("-------GetMethod bekommt die Zahl: " + getAllDailyMissions(player, i));

            }
            setDailyMissionProgress(player, 1, 0);
        }
    }

    private static void setAllDailyMissions(IEntityDataSaver player, int i, int numberToPut) {
        if(i <= 1) {
            System.out.println("-------SetMethod1 NumberToPut: " + numberToPut);
            setDailyMission1(player, numberToPut);
        } else if (i == 2) {
            System.out.println("-------SetMethod2 NumberToPut: " + numberToPut);
            setDailyMission2(player, numberToPut);
        } else {
            System.out.println("-------SetMethod3 NumberToPut: " + numberToPut);
            setDailyMission3(player, numberToPut);
        }
    }

    private static int getAllDailyMissions(IEntityDataSaver player, int i) {
        if( i <= 1) {
            System.out.println("-------GetMethod1 Number: " + getDailyMission1(player));
            return getDailyMission1(player);
        } else if (i == 2) {
            System.out.println("-------GetMethod2 Number: " + getDailyMission2(player));
            return getDailyMission2(player);
        } else {
            System.out.println("-------GetMethod3 Number: " + getDailyMission3(player));
            return getDailyMission3(player);
        }
    }

    private static int alwaysDifferentMissions(int i ,int exclusiveMaxNumber,int y1,int y2,int y3) {
        if(i <= 1) {
            return y1;
        }
        if(i == 2) {
            while (y1 == y2) {
                y2 = nextInt(0, exclusiveMaxNumber);
            }
            return y2;
        } else {
            while (y3 == y1 || y3 == y2) {
                y3 = nextInt(0, exclusiveMaxNumber);
            }
            return y3;
        }
    }

    public static int getDailyMission1(IEntityDataSaver player) {

        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("daily_mission_1");
    }
    public static int getDailyMission2(IEntityDataSaver player) {

        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("daily_mission_2");
    }
    public static int getDailyMission3(IEntityDataSaver player) {

        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("daily_mission_3");
    }

    public static void setDailyMission1(IEntityDataSaver player, int numberToPut) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("daily_mission_1", numberToPut);
    }
    public static void setDailyMission2(IEntityDataSaver player, int numberToPut) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("daily_mission_2", numberToPut);
    }
    public static void setDailyMission3(IEntityDataSaver player, int numberToPut) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("daily_mission_3", numberToPut);
    }

    public static boolean isDailyMissionOfNumberTypeItem(IEntityDataSaver player, int missionNumber) {

        NbtCompound nbt = player.getPersistentData();

        return nbt.getBoolean("daily_mission_" + missionNumber);
    }
    public static void setDailyMissionOfNumberTypeItem(IEntityDataSaver player, int missionNumber, boolean isItemMission) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putBoolean("daily_mission_" + missionNumber, isItemMission);
    }

    public static int getDailyMissionOfNumber(IEntityDataSaver player, int missionNumber) {

        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("daily_mission_" + missionNumber);
    }

    public static void setDailyMissionNumber(IEntityDataSaver player, int missionNumber, int numberToPut) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("daily_mission_" + missionNumber, numberToPut);
    }

    public static void setDailyMissionProgress(IEntityDataSaver player, int missionNumber, int newProgress) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("daily_mission_" + missionNumber + "_progress", newProgress);
    }

    public static int getDailyMissionProgress(IEntityDataSaver player, int missionNumber) {

        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("daily_mission_" + missionNumber + "_progress");
    }

    public static void addDailyMissionProgress(IEntityDataSaver player, int missionNumber, int additionalProgress) {

        NbtCompound nbt = player.getPersistentData();

        int currentProgress = getDailyMissionProgress(player, missionNumber);

        int newProgress = currentProgress + additionalProgress;

        nbt.putInt("daily_mission_" + missionNumber + "_progress", newProgress);
    }
}

