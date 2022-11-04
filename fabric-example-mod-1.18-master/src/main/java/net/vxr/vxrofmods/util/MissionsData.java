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
    public static void setRandomDailyMission1(IEntityDataSaver player, List <ItemStack> itemStackList, List<EntityType> entityTypeList) {
        if(nextInt(0,2) == 0) {
            //Item Mission
            int x = nextInt(0, itemStackList.size());
            System.out.println("----Item Mission");
            System.out.println("----Random Zahl ist: " + x);
            setDailyMissionType1(player, 0);
            setDailyMission1(player, x);
            System.out.println("----GetMedthod1 bekommt: " + getDailyMission1(player));
        } else {
            //Mob Kill Mission
            int x = nextInt(0, entityTypeList.size());
            System.out.println("----Mob Kill Mission");
            System.out.println("----Random Zahl ist: " + x);
            setDailyMissionType1(player, 1);
            setDailyMission1(player, x);
            System.out.println("----GetMedthod1 bekommt: " + getDailyMission1(player));
        }
    }
    public static void setRandomDailyMission2(IEntityDataSaver player, List <ItemStack> itemStackList, List<EntityType> entityTypeList) {
        if(nextInt(0,2) == 0) {
            //Item Mission
            int x = nextInt(0, itemStackList.size());
            System.out.println("----Item Mission");
            System.out.println("----Random Zahl ist: " + x);
            setDailyMissionType2(player, 0);
            setDailyMission2(player, x);
            System.out.println("----GetMedthod2 bekommt: " + getDailyMission2(player));
        } else {
            //Mob Kill Mission
            int x = nextInt(0, entityTypeList.size());
            System.out.println("----Mob Kill Mission");
            System.out.println("----Random Zahl ist: " + x);
            setDailyMissionType2(player, 1);
            setDailyMission2(player, x);
            System.out.println("----GetMedthod2 bekommt: " + getDailyMission2(player));
        }
    }
    public static void setRandomDailyMission3(IEntityDataSaver player, List <ItemStack> itemStackList, List<EntityType> entityTypeList) {
        if(nextInt(0,2) == 0) {
            //Item Mission
            int x = nextInt(0, itemStackList.size());
            System.out.println("----Item Mission");
            System.out.println("----Random Zahl ist: " + x);
            setDailyMissionType3(player, 0);
            setDailyMission3(player, x);
            System.out.println("----GetMedthod3 bekommt: " + getDailyMission3(player));
        } else {
            //Mob Kill Mission
            int x = nextInt(0, entityTypeList.size());
            System.out.println("----Mob Kill Mission");
            System.out.println("----Random Zahl ist: " + x);
            setDailyMissionType3(player, 1);
            setDailyMission3(player, x);
            System.out.println("----GetMedthod3 bekommt: " + getDailyMission3(player));
        }
    }

    public static int getDailyMissionType1(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("daily_mission_1_type");
    }
    public static int getDailyMissionType2(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("daily_mission_2_type");
    }
    public static int getDailyMissionType3(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("daily_mission_3_type");
    }
    public static void setDailyMissionType1(IEntityDataSaver player, int numberAKATypeToPut) {
        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("daily_mission_1_type", numberAKATypeToPut);
    }
    public static void setDailyMissionType2(IEntityDataSaver player, int numberAKATypeToPut) {
        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("daily_mission_2_type", numberAKATypeToPut);
    }
    public static void setDailyMissionType3(IEntityDataSaver player, int numberAKATypeToPut) {
        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("daily_mission_3_type", numberAKATypeToPut);
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

