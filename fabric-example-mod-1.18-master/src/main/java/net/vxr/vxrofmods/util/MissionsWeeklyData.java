package net.vxr.vxrofmods.util;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.vxr.vxrofmods.command.MissionsCommand;
import java.util.List;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public class MissionsWeeklyData {


    public static int getWeeklyMission1(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("weekly_mission_1");
    }
    public static int getWeeklyMission2(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("weekly_mission_2");
    }
    public static int getWeeklyMission3(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("weekly_mission_3");
    }

    public static void setWeeklyMission1(IEntityDataSaver player, int numberToPut) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("weekly_mission_1", numberToPut);
    }
    public static void setWeeklyMission2(IEntityDataSaver player, int numberToPut) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("weekly_mission_2", numberToPut);
    }
    public static void setWeeklyMission3(IEntityDataSaver player, int numberToPut) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("weekly_mission_3", numberToPut);
    }
    public static void setRandomWeeklyMission1(IEntityDataSaver player, List <ItemStack> itemStackList, List<EntityType> entityTypeList) {
        if(nextInt(0,2) == 0) {
            //Item Mission
            int x = nextInt(0, itemStackList.size());
            if(getWeeklyMissionType2(player) == 0 || getWeeklyMissionType3(player) == 0) {
                while(x == getWeeklyMission2(player) || x == getWeeklyMission3(player)) {
                    x = nextInt(0, itemStackList.size());
                }
            }
            /*System.out.println("----Item Mission");
            System.out.println("----Random Zahl ist: " + x);*/
            setWeeklyMissionType1(player, 0);
            setWeeklyMission1(player, x);
            //System.out.println("----GetMedthod1 bekommt: " + getWeeklyMission1(player));
        } else {
            //Mob Kill Mission
            int x = nextInt(0, entityTypeList.size());
            if(getWeeklyMissionType2(player) == 1 || getWeeklyMissionType3(player) == 1) {
                while(x == getWeeklyMission2(player) || x == getWeeklyMission3(player)) {
                    x = nextInt(0, entityTypeList.size());
                }
            }
            //System.out.println("----Mob Kill Mission");
            //System.out.println("----Random Zahl ist: " + x);
            setWeeklyMissionType1(player, 1);
            setWeeklyMission1(player, x);
            //System.out.println("----GetMedthod1 bekommt: " + getWeeklyMission1(player));
        }
        setWeeklyMissionProgress(player, 1, 0);
        setWeeklyMissionComplete(player, 1, false);
    }
    public static void setRandomWeeklyMission2(IEntityDataSaver player, List <ItemStack> itemStackList, List<EntityType> entityTypeList) {
        if(nextInt(0,2) == 0) {
            //Item Mission
            int x = nextInt(0, itemStackList.size());
            if(getWeeklyMissionType1(player) == 0 || getWeeklyMissionType3(player) == 0) {
                while(x == getWeeklyMission1(player) || x == getWeeklyMission3(player)) {
                    x = nextInt(0, itemStackList.size());
                }
            }
            //System.out.println("----Item Mission");
            //System.out.println("----Random Zahl ist: " + x);
            setWeeklyMissionType2(player, 0);
            setWeeklyMission2(player, x);
            //System.out.println("----GetMedthod2 bekommt: " + getWeeklyMission2(player));
        } else {
            //Mob Kill Mission
            int x = nextInt(0, entityTypeList.size());
            if(getWeeklyMissionType1(player) == 1 || getWeeklyMissionType3(player) == 1) {
                while(x == getWeeklyMission1(player) || x == getWeeklyMission3(player)) {
                    x = nextInt(0, entityTypeList.size());
                }
            }
            //System.out.println("----Mob Kill Mission");
            //System.out.println("----Random Zahl ist: " + x);
            setWeeklyMissionType2(player, 1);
            setWeeklyMission2(player, x);
            //System.out.println("----GetMedthod2 bekommt: " + getWeeklyMission2(player));
        }
        setWeeklyMissionProgress(player, 2, 0);
        setWeeklyMissionComplete(player, 2, false);
    }
    public static void setRandomWeeklyMission3(IEntityDataSaver player, List <ItemStack> itemStackList, List<EntityType> entityTypeList) {
        if(nextInt(0,2) == 0) {
            //Item Mission
            int x = nextInt(0, itemStackList.size());
            if(getWeeklyMissionType1(player) == 0 || getWeeklyMissionType2(player) == 0) {
                while(x == getWeeklyMission1(player) || x == getWeeklyMission2(player)) {
                    x = nextInt(0, itemStackList.size());
                }
            }
            //System.out.println("----Item Mission");
            //System.out.println("----Random Zahl ist: " + x);
            setWeeklyMissionType3(player, 0);
            setWeeklyMission3(player, x);
            //System.out.println("----GetMedthod3 bekommt: " + getWeeklyMission3(player));
        } else {
            //Mob Kill Mission
            int x = nextInt(0, entityTypeList.size());
            if(getWeeklyMissionType1(player) == 1 || getWeeklyMissionType2(player) == 1) {
                while(x == getWeeklyMission1(player) || x == getWeeklyMission2(player)) {
                    x = nextInt(0, entityTypeList.size());
                }
            }
            //System.out.println("----Mob Kill Mission");
            //System.out.println("----Random Zahl ist: " + x);
            setWeeklyMissionType3(player, 1);
            setWeeklyMission3(player, x);
            //System.out.println("----GetMedthod3 bekommt: " + getWeeklyMission3(player));
        }
        setWeeklyMissionProgress(player, 3, 0);
        setWeeklyMissionComplete(player, 3, false);
    }

    public static int getWeeklyMissionType1(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("weekly_mission_1_type");
    }
    public static int getWeeklyMissionType2(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("weekly_mission_2_type");
    }
    public static int getWeeklyMissionType3(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("weekly_mission_3_type");
    }
    public static void setWeeklyMissionType1(IEntityDataSaver player, int numberAKATypeToPut) {
        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("weekly_mission_1_type", numberAKATypeToPut);
    }
    public static void setWeeklyMissionType2(IEntityDataSaver player, int numberAKATypeToPut) {
        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("weekly_mission_2_type", numberAKATypeToPut);
    }
    public static void setWeeklyMissionType3(IEntityDataSaver player, int numberAKATypeToPut) {
        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("weekly_mission_3_type", numberAKATypeToPut);
    }

    public static void setWeeklyMissionProgress(IEntityDataSaver player, int missionNumber, int newProgress) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("weekly_mission_" + missionNumber + "_progress", newProgress);
    }

    public static int getWeeklyMissionProgress(IEntityDataSaver player, int missionNumber) {

        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("weekly_mission_" + missionNumber + "_progress");
    }

    public static void addWeeklyMissionProgress(IEntityDataSaver player, int missionNumber, int additionalProgress) {

        NbtCompound nbt = player.getPersistentData();

        int currentProgress = getWeeklyMissionProgress(player, missionNumber);

        int newProgress = currentProgress + additionalProgress;

        nbt.putInt("weekly_mission_" + missionNumber + "_progress", newProgress);
    }

    public static void setWeeklyMissionComplete(IEntityDataSaver player, int missionNumber, boolean isComplete) {
        NbtCompound nbt = player.getPersistentData();

        if(isComplete) {
            CustomMoneyData.addOrSubtractMoney(player, MissionsCommand.rewardAmountWeeklyMission);
        }

        nbt.putBoolean("weekly_mission_" + missionNumber + "_complete", isComplete);
    }

    public static boolean getWeeklyMissionComplete(IEntityDataSaver player, int missionNumber) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getBoolean("weekly_mission_" + missionNumber + "_complete");
    }
}

