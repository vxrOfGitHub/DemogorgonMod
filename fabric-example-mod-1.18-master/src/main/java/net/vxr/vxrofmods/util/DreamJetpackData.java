package net.vxr.vxrofmods.util;

import net.minecraft.nbt.NbtCompound;

public class DreamJetpackData {
    public static boolean switchJetpackOnOff(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        boolean isJetpackOn = nbt.getBoolean("dream_jetpack_on");
        isJetpackOn = !isJetpackOn;
        nbt.putBoolean("dream_jetpack_on", isJetpackOn);

        return isJetpackOn;
    }

    public static boolean setJetpackUp(IEntityDataSaver player, boolean newIsJetpackUp) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putBoolean("dream_jetpack_up", newIsJetpackUp);

        return newIsJetpackUp;
    }

    public static boolean getJetpackOnOff(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        boolean isJetpackOn = nbt.getBoolean("dream_jetpack_on");

        return isJetpackOn;
    }

    public static boolean getJetpackUp(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        boolean isJetpackUp = nbt.getBoolean("dream_jetpack_up");

        return isJetpackUp;
    }

    public static boolean switchJetpackUp(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        boolean isJetpackUp = nbt.getBoolean("dream_jetpack_up");
        isJetpackUp = !isJetpackUp;
        nbt.putBoolean("dream_jetpack_up", isJetpackUp);

        return isJetpackUp;

    }
}
