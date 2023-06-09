package net.vxrofmods.demogorgonmod.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;

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


    public static void writeDDTargetsPositionToTarget(IEntityDataSaver target, Vec3d pos) {

        writeDDTargetsXPositionToTarget(target, pos.x);
        writeDDTargetsYPositionToTarget(target, pos.y);
        writeDDTargetsZPositionToTarget(target, pos.z);
    }
    public static void writeDDTargetsPositionToTarget(IEntityDataSaver target, double x, double y, double z) {

        writeDDTargetsXPositionToTarget(target, x);
        writeDDTargetsYPositionToTarget(target, y);
        writeDDTargetsZPositionToTarget(target, z);
    }

    public static Vec3d readDDTargetsPositionFromTarget(IEntityDataSaver target) {
        double x = readDDTargetsXPositionFromTarget(target);
        double y = readDDTargetsYPositionFromTarget(target);
        double z = readDDTargetsZPositionFromTarget(target);

        return new Vec3d(x, y, z);
    }

    public static void writeDDTargetsXPositionToTarget(IEntityDataSaver target, double posX) {
        NbtCompound nbt = target.getPersistentData();
        nbt.putDouble("demogorgon.dd_target_position_x", posX);
    }

    public static double readDDTargetsXPositionFromTarget(IEntityDataSaver target) {
        NbtCompound nbt = target.getPersistentData();

        return nbt.getInt("demogorgon.dd_target_position_x");
    }

    public static void writeDDTargetsYPositionToTarget(IEntityDataSaver target, double posY) {
        NbtCompound nbt = target.getPersistentData();
        nbt.putDouble("demogorgon.dd_target_position_y", posY);
    }

    public static double readDDTargetsYPositionFromTarget(IEntityDataSaver target) {
        NbtCompound nbt = target.getPersistentData();

        return nbt.getInt("demogorgon.dd_target_position_y");
    }
    public static void writeDDTargetsZPositionToTarget(IEntityDataSaver target, double posZ) {
        NbtCompound nbt = target.getPersistentData();
        nbt.putDouble("demogorgon.dd_target_position_z", posZ);
    }

    public static double readDDTargetsZPositionFromTarget(IEntityDataSaver target) {
        NbtCompound nbt = target.getPersistentData();

        return nbt.getInt("demogorgon.dd_target_position_z");
    }

    public static void setPortalLivingTime(IEntityDataSaver portal, int livingTime) {
        NbtCompound nbt = portal.getPersistentData();

        nbt.putInt("demogorgon_portal.living_time", livingTime);
    }

    public static int getPortalLivingTime(IEntityDataSaver portal) {
        NbtCompound nbt = portal.getPersistentData();

        return nbt.getInt("demogorgon_portal.living_time");
    }

}
