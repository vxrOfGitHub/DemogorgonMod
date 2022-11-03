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
import net.vxr.vxrofmods.util.DreamBoostCooldownData;
import net.vxr.vxrofmods.util.DreamJetpackData;
import net.vxr.vxrofmods.util.IEntityDataSaver;
import net.vxr.vxrofmods.util.MissionsData;

import java.util.ArrayList;
import java.util.List;

public class ServerTickHandler implements ServerTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftServer server) {
        // Server stuff
        DailyMissionsTime(server);
        WeeklyMissionsTime();

        // Sync with Players
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            PlayerDailyMissionsTime(player);
            PlayerWeeklyMissionsTime(player);
        }
    }

    private void PlayerDailyMissionsTime(ServerPlayerEntity player) {
        MissionsData.setDailyMissionTime(((IEntityDataSaver) player), DailyMissionCountdown);
    }

    private void PlayerSetDailyMissions(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            MissionsData.setRandomDailyMissions(((IEntityDataSaver)player), itemsForDailyMission, mobsForDailyMission);
        }
    }

    private void DailyMissionsTime(MinecraftServer server){
        if(DailyMissionCountdown > 0) {
            DailyMissionCountdown--;
        } else {
            PlayerSetDailyMissions(server);
            DailyMissionCountdown = MaxDailyMissionCountdown;
        }
    }

    private void PlayerWeeklyMissionsTime(ServerPlayerEntity player) {
        MissionsData.setWeeklyMissionTime(((IEntityDataSaver) player), WeeklyMissionCountdown);
    }

    private void WeeklyMissionsTime(){
        if(WeeklyMissionCountdown > 0) {
            WeeklyMissionCountdown--;
        } else {
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

}
