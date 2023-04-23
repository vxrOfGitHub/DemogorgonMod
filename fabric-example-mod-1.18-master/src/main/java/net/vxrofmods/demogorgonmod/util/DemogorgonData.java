package net.vxrofmods.demogorgonmod.util;

import net.minecraft.nbt.NbtCompound;

public class DemogorgonData {

    public static void setPlayDimensionDriftSubmergeAnimation(IEntityDataSaver demogorgon, boolean playing) {
        NbtCompound nbt = demogorgon.getPersistentData();
        nbt.putBoolean("shouldPlaySubmergeAnimation", playing);
    }

    public static boolean getPlayDimensionDriftSubmergeAnimation(IEntityDataSaver demogorgon) {
        NbtCompound nbt = demogorgon.getPersistentData();

        return nbt.getBoolean("shouldPlaySubmergeAnimation");
    }

    public static void setSpawnDimensionDriftParticles(IEntityDataSaver demogorgon, boolean spawning) {
        NbtCompound nbt = demogorgon.getPersistentData();
        nbt.putBoolean("shouldSpawnDDParticles", spawning);
    }

    public static boolean getSpawnDimensionDriftParticles(IEntityDataSaver demogorgon) {
        NbtCompound nbt = demogorgon.getPersistentData();

        return nbt.getBoolean("shouldSpawnDDParticles");
    }

    public static void writeTargetToDDTargetNBT(IEntityDataSaver target, boolean isTarget) {
        NbtCompound nbt = target.getPersistentData();
        nbt.putBoolean("isDDTarget", isTarget);
    }

    public static boolean getDDTargetinNBT(IEntityDataSaver target) {
        NbtCompound nbt = target.getPersistentData();

        return nbt.getBoolean("isDDTarget");
    }

}
