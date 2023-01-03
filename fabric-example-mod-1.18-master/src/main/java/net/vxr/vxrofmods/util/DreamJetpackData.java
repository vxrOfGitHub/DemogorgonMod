package net.vxr.vxrofmods.util;

import net.minecraft.nbt.NbtCompound;

public class DreamJetpackData {
    public static void switchJetpackOnOff(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        boolean isJetpackOn = nbt.getBoolean("dream_jetpack_on");
        isJetpackOn = !isJetpackOn;
        nbt.putBoolean("dream_jetpack_on", isJetpackOn);
    }

    public static void setEarlierForwardSpeed(IEntityDataSaver player, float earlierForwardSpeed) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putFloat("dream_jetpack_airstrafing_speed",earlierForwardSpeed);
    }

    public static float getEarlierForwardSpeed(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getFloat("dream_jetpack_airstrafing_speed");
    }

    public static void setJetpackOnOff(IEntityDataSaver player, boolean isJetpackOn) {
        NbtCompound nbt = player.getPersistentData();

        nbt.putBoolean("dream_jetpack_on", isJetpackOn);

    }

    public static void setFuelTick(IEntityDataSaver player, int ticks) {
        NbtCompound nbtCompound = player.getPersistentData();

        nbtCompound.putInt("dream_jetpack_fuel_tick", ticks);
    }

    public static int getFuelTick(IEntityDataSaver player) {
        NbtCompound nbtCompound = player.getPersistentData();

        return nbtCompound.getInt("dream_jetpack_fuel_tick");
    }

    public static void setJetpackUp(IEntityDataSaver player, boolean newIsJetpackUp) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putBoolean("dream_jetpack_up", newIsJetpackUp);

    }

    public static boolean getJetpackOnOff(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getBoolean("dream_jetpack_on");
    }

    public static boolean getJetpackUp(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getBoolean("dream_jetpack_up");
    }

    public static void switchJetpackUp(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        boolean isJetpackUp = nbt.getBoolean("dream_jetpack_up");
        isJetpackUp = !isJetpackUp;
        nbt.putBoolean("dream_jetpack_up", isJetpackUp);

    }

    public static void setHadJetpackOn(IEntityDataSaver player, boolean hadJetpackOn) {
        NbtCompound nbt = player.getPersistentData();

        nbt.putBoolean("dream_jetpack_was_on", hadJetpackOn);
    }

    public static boolean hadJetpackOn(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getBoolean("dream_jetpack_was_on");
    }

}
