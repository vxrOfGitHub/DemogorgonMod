package net.vxr.vxrofmods.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

public class MissionsData {


    public static String getDailyMissionTime(IEntityDataSaver player) {

        NbtCompound nbt = player.getPersistentData();

        int ticks2Hour = nbt.getInt("daily_mission_time") / 72000;
        int ticks2Min = nbt.getInt("daily_mission_time") % 72000 / 1200;
        int ticks2Sec = nbt.getInt("daily_mission_time") % 72000 % 1200 / 20;

        String ticks2Time = ticks2Hour + "h " + ticks2Min + "min " + ticks2Sec + "sec";

        return ticks2Time;

    }

    public static int setDailyMissionTime(IEntityDataSaver player, int dailyMissionTime) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("daily_mission_time", dailyMissionTime);

        return dailyMissionTime;

    }

    public static String getWeeklyMissionTime(IEntityDataSaver player) {

        NbtCompound nbt = player.getPersistentData();

        int ticks2Day = nbt.getInt("weekly_mission_time") / 1728000;
        int ticks2Hour = nbt.getInt("weekly_mission_time") % 1728000 / 72000;
        int ticks2Min = nbt.getInt("weekly_mission_time") % 1728000 % 72000 / 1200;


        String ticks2Time = ticks2Day + "day(s)" + ticks2Hour + "h " + ticks2Min + "min ";

        return ticks2Time;

    }

    public static int setWeeklyMissionTime(IEntityDataSaver player, int weeklyMissionTime) {

        NbtCompound nbt = player.getPersistentData();

        nbt.putInt("weekly_mission_time", weeklyMissionTime);

        return weeklyMissionTime;

    }

    public static List<ItemStack> setDailyMissionItems(IEntityDataSaver player, List<ItemStack> dailyMissionItems) {
        NbtCompound nbt = player.getPersistentData();

        //nbt.put("daily_mission_item", dailyMissionItems.);
        return null;
    }

}
