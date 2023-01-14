package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vxr.vxrofmods.command.MissionsCommand;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.util.CustomMoneyData;
import net.vxr.vxrofmods.util.IEntityDataSaver;
import net.vxr.vxrofmods.util.MissionsData;
import net.vxr.vxrofmods.util.MissionsWeeklyData;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public class ModPlayerEventCopyFrom implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        IEntityDataSaver original = ((IEntityDataSaver) oldPlayer);
        IEntityDataSaver player = ((IEntityDataSaver) newPlayer);

        // Save Money when dying
        saveMoney(player, original, oldPlayer, alive);
        // Save Missions when dying
        saveMissions(player, original);

    }

    private void saveMissions(IEntityDataSaver player, IEntityDataSaver original) {
        System.out.println("SavedMissions!");
        // Saving Daily Missions
        MissionsData.setDailyMission1(player, MissionsData.getDailyMission1(original));
        MissionsData.setDailyMission2(player, MissionsData.getDailyMission2(original));
        MissionsData.setDailyMission3(player, MissionsData.getDailyMission3(original));
        // Saving Weekly Missions
        MissionsWeeklyData.setWeeklyMission1(player, MissionsWeeklyData.getWeeklyMission1(original));
        MissionsWeeklyData.setWeeklyMission2(player, MissionsWeeklyData.getWeeklyMission2(original));
        MissionsWeeklyData.setWeeklyMission3(player, MissionsWeeklyData.getWeeklyMission3(original));
        // Saving both Progress and both Complete Missions
        for(int i = 1; i <=3; i++) {
            NbtCompound nbt = player.getPersistentData();
            nbt.putBoolean("daily_mission_" + i + "_complete", MissionsData.getDailyMissionComplete(original, i));
            nbt.putBoolean("weekly_mission_" + i + "_complete", MissionsWeeklyData.getWeeklyMissionComplete(original, i));
            nbt.putInt("daily_mission_" + i + "_progress", MissionsData.getDailyMissionProgress(original, i));
            nbt.putInt("weekly_mission_" + i + "_progress", MissionsWeeklyData.getWeeklyMissionProgress(player, i));
        }
        // Saving Both Reroll Times
        MissionsData.setPlayerDailyRerollTimes(player, MissionsData.getPlayerDailyRerollTimes(original));
        MissionsData.setPlayerWeeklyRerollTimes(player, MissionsData.getPlayerWeeklyRerollTimes(original));
    }

    private void saveMoney(IEntityDataSaver player, IEntityDataSaver original, PlayerEntity oldPlayer, boolean alive) {

        int originalMoneyAmount = original.getPersistentData().getInt("current_money");

        if(alive) {
            System.out.println("Is Alive");
            player.getPersistentData().putInt("current_money", originalMoneyAmount);
        } else {
            int looseAmount = originalMoneyAmount / 10;
            int newMoneyAmount = originalMoneyAmount - looseAmount;
            int amountToDrop = looseAmount / 2;
            System.out.println("NewMoneyAmount: " + newMoneyAmount);
            player.getPersistentData().putInt("current_money", newMoneyAmount);

            ItemStack moneyToDrop = new ItemStack(ModItems.COIN);
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putInt("vxrofmods.coin_value", amountToDrop);
            moneyToDrop.setNbt(nbtCompound);

            oldPlayer.dropStack(moneyToDrop);
        }
    }
}
