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

    public static void writeAnimationToNBT(IEntityDataSaver demogorgon, int animation) {
        NbtCompound nbt = demogorgon.getPersistentData();
        nbt.putInt("demogorgon.animation", animation);
    }

    public static int getAnimationInNBT(IEntityDataSaver demogorgon) {
        NbtCompound nbt = demogorgon.getPersistentData();

        return nbt.getInt("demogorgon.animation");
    }


    public static void writePlayedDDAttack1Sound(IEntityDataSaver demogorgon, boolean played) {
        NbtCompound nbt = demogorgon.getPersistentData();
        nbt.putBoolean("played_dd_attack_1_sound", played);
    }

    public static boolean getPlayedDDAttack1Sound(IEntityDataSaver demogorgon) {
        NbtCompound nbt = demogorgon.getPersistentData();

        return nbt.getBoolean("played_dd_attack_1_sound");
    }


    public static void writeDDAttack1CurrentTick(IEntityDataSaver demogorgon, int startTick) {
        NbtCompound nbt = demogorgon.getPersistentData();
        nbt.putInt("demogorgon.dd_attack_1_start_tick", startTick);
    }

    public static int readDDAttack1CurrentTick(IEntityDataSaver demogorgon) {
        NbtCompound nbt = demogorgon.getPersistentData();

        return nbt.getInt("demogorgon.dd_attack_1_start_tick");
    }


}
