package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vxr.vxrofmods.command.MissionsCommand;
import net.vxr.vxrofmods.util.*;

import java.util.ArrayList;
import java.util.List;

public class ServerTickHandler implements ServerTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftServer server) {
        // Server stuff
        DailyMissionsTime(server);
        WeeklyMissionsTime(server);

        // Sync with Players
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            PlayerDailyMissionsTime(player);
            PlayerWeeklyMissionsTime(player);
        }

    }

    private void PlayerDailyMissionsTime(ServerPlayerEntity player) {
        MissionsData.setDailyMissionTime(((IEntityDataSaver) player), DailyMissionCountdown);
    }

    private void PlayerSetDailyMission1(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            MissionsData.setRandomDailyMission1(((IEntityDataSaver) player), itemsForDailyMission, mobsForDailyMission);
        }
    }
    private void PlayerSetDailyMission2(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            MissionsData.setRandomDailyMission2(((IEntityDataSaver) player), itemsForDailyMission, mobsForDailyMission);
        }
    }
    private void PlayerSetDailyMission3(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            MissionsData.setRandomDailyMission3(((IEntityDataSaver) player), itemsForDailyMission, mobsForDailyMission);
        }
    }
    private void PlayerSetWeeklyMission1(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            MissionsWeeklyData.setRandomWeeklyMission1(((IEntityDataSaver) player), itemsForWeeklyMission, mobsForWeeklyMission);
        }
    }
    private void PlayerSetWeeklyMission2(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            MissionsWeeklyData.setRandomWeeklyMission2(((IEntityDataSaver) player), itemsForWeeklyMission, mobsForWeeklyMission);
        }
    }
    private void PlayerSetWeeklyMission3(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            MissionsWeeklyData.setRandomWeeklyMission3(((IEntityDataSaver) player), itemsForWeeklyMission, mobsForWeeklyMission);
        }
    }


    private void DailyMissionsTime(MinecraftServer server){
        if(DailyMissionCountdown > 0) {
            DailyMissionCountdown--;
        } else {
            PlayerSetDailyMission1(server);
            PlayerSetDailyMission2(server);
            PlayerSetDailyMission3(server);
            DailyMissionCountdown = MaxDailyMissionCountdown;
        }
    }

    private void PlayerWeeklyMissionsTime(ServerPlayerEntity player) {
        MissionsData.setWeeklyMissionTime(((IEntityDataSaver) player), WeeklyMissionCountdown);
    }

    private void WeeklyMissionsTime(MinecraftServer server){
        if(WeeklyMissionCountdown > 0) {
            WeeklyMissionCountdown--;
        } else {
            PlayerSetWeeklyMission1(server);
            PlayerSetWeeklyMission2(server);
            PlayerSetWeeklyMission3(server);
            WeeklyMissionCountdown = MaxWeeklyMissionCountdown;
        }
    }

    public static int DailyMissionCountdown = 1728000;

    public static int MaxDailyMissionCountdown = 1728000;

    public static int WeeklyMissionCountdown = 12096000;

    public static int MaxWeeklyMissionCountdown = 12096000;

    public static List<ItemStack> itemsForDailyMission = new ArrayList<>();

    public static List<Integer> amountOfItemsForDailyMission = new ArrayList<>();

    public static List<EntityType> mobsForDailyMission = new ArrayList<>();

    public static List<Integer> amountOfMobToKillForDailyMission = new ArrayList<>();

    public static List<ItemStack> itemsForWeeklyMission = new ArrayList<>();

    public static List<Integer> amountOfItemsForWeeklyMission = new ArrayList<>();

    public static List<EntityType> mobsForWeeklyMission = new ArrayList<>();

    public static List<Integer> amountOfMobToKillForWeeklyMission = new ArrayList<>();

}
