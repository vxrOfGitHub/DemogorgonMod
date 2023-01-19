package net.vxr.vxrofmods.event;

import com.eliotlash.mclib.math.functions.limit.Min;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.util.*;

import java.util.ArrayList;
import java.util.List;

public class ServerTickHandler implements ServerTickEvents.StartTick{
    @Override
    public void onStartTick(MinecraftServer server) {
        // Server stuff
        if(serverRestartedForMissionTime) {
            LoadMissionsTime(server);
            serverRestartedForMissionTime = false;
        }
        DailyMissionsTime(server);
        WeeklyMissionsTime(server);
        SaveAndLoadMissionsRerolledTime(server);
        // Sync with Players
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            PlayerDailyMissionsTime(player);
            PlayerWeeklyMissionsTime(player);
        }


    }

    private void SaveAndLoadMissionsRerolledTime(MinecraftServer server) {
        NbtCompound nbt = new NbtCompound();
        if(server.getDataCommandStorage().get(saveMissionsRerollTimesID) != null) {
            nbt = server.getDataCommandStorage().get(saveMissionsRerollTimesID);
            if(nbt.getInt(dailyMissionRerollTimesKey) > 0) {
                totalDailyRerolls = nbt.getInt(dailyMissionRerollTimesKey);
            } if(nbt.getInt(weeklyMissionRerollTimesKey)  > 0) {
                totalWeeklyRerolls = nbt.getInt(weeklyMissionRerollTimesKey);
            }
        }
        nbt.putInt(dailyMissionRerollTimesKey, totalDailyRerolls);
        nbt.putInt(weeklyMissionRerollTimesKey, totalWeeklyRerolls);
        nbt.putInt(dailyMissionCountdownKey, DailyMissionCountdown);
        nbt.putInt(weeklyMissionCountdownKey, WeeklyMissionCountdown);
        server.getDataCommandStorage().set(saveMissionsRerollTimesID, nbt);
    }

    private void LoadMissionsTime(MinecraftServer server) {
        DailyMissionCountdown = server.getDataCommandStorage().get(saveMissionsRerollTimesID).getInt(dailyMissionCountdownKey);
        WeeklyMissionCountdown = server.getDataCommandStorage().get(saveMissionsRerollTimesID).getInt(weeklyMissionCountdownKey);
    }

    public static final Identifier saveMissionsRerollTimesID = new Identifier(WW2Mod.MOD_ID + "_save_missions_reroll_times");
    public static final String dailyMissionRerollTimesKey = "daily_mission_reroll_times";
    public static final String weeklyMissionRerollTimesKey = "weekly_mission_reroll_times";
    public static final String dailyMissionCountdownKey = "daily_mission_countdown";
    public static final String weeklyMissionCountdownKey = "weekly_mission_countdown";

    private void PlayerDailyMissionsTime(ServerPlayerEntity player) {
        MissionsData.setDailyMissionTime(((IEntityDataSaver) player), DailyMissionCountdown);
    }

    private void PlayerSetDailyMission1(ServerPlayerEntity player) {
        MissionsData.setRandomDailyMission1((((IEntityDataSaver) player)), itemsForDailyMission, mobsForDailyMission);
    }
    private void PlayerSetDailyMission2(ServerPlayerEntity player) {
        MissionsData.setRandomDailyMission2(((IEntityDataSaver) player), itemsForDailyMission, mobsForDailyMission);
    }
    private void PlayerSetDailyMission3 (ServerPlayerEntity player) {
        MissionsData.setRandomDailyMission3(((IEntityDataSaver) player), itemsForDailyMission, mobsForDailyMission);
    }
    private void PlayerSetWeeklyMission1(IEntityDataSaver player) {
        MissionsWeeklyData.setRandomWeeklyMission1((player), itemsForWeeklyMission, mobsForWeeklyMission);
    }
    private void PlayerSetWeeklyMission2 (IEntityDataSaver player) {
        MissionsWeeklyData.setRandomWeeklyMission2((player), itemsForWeeklyMission, mobsForWeeklyMission);
    }
    private void PlayerSetWeeklyMission3(IEntityDataSaver player) {
        MissionsWeeklyData.setRandomWeeklyMission3((player), itemsForWeeklyMission, mobsForWeeklyMission);
    }

    public static int totalDailyRerolls = 0;
    public static int totalWeeklyRerolls = 0;

    private void DailyMissionsTime(MinecraftServer server){
        if(DailyMissionCountdown > 0) {
            DailyMissionCountdown--;
        } else {
            NbtCompound nbt = new NbtCompound();
            if(server.getDataCommandStorage().get(saveMissionsRerollTimesID) != null) {
                nbt = server.getDataCommandStorage().get(saveMissionsRerollTimesID);
            }
            totalDailyRerolls++;
            nbt.putInt(dailyMissionRerollTimesKey, totalDailyRerolls);
            server.getDataCommandStorage().set(saveMissionsRerollTimesID, nbt);
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                IEntityDataSaver playerSaver = ((IEntityDataSaver) player);
                PlayerSetDailyMission1(player);
                PlayerSetDailyMission2(player);
                PlayerSetDailyMission3(player);
                DailyMissionCountdown = MaxDailyMissionCountdown;
                MissionsData.setPlayerDailyRerollTimes(playerSaver, totalDailyRerolls);
            }
        }
    }

    private void PlayerWeeklyMissionsTime(ServerPlayerEntity player) {
        MissionsData.setWeeklyMissionTime(((IEntityDataSaver) player), WeeklyMissionCountdown);
    }

    private void WeeklyMissionsTime(MinecraftServer server){
        if(WeeklyMissionCountdown > 0) {
            WeeklyMissionCountdown--;
        } else {
            totalWeeklyRerolls++;
            NbtCompound nbt = new NbtCompound();
            if(server.getDataCommandStorage().get(saveMissionsRerollTimesID) != null) {
                nbt = server.getDataCommandStorage().get(saveMissionsRerollTimesID);
            }
            nbt.putInt(weeklyMissionRerollTimesKey, totalWeeklyRerolls);
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                IEntityDataSaver playerSaver = ((IEntityDataSaver) player);
                PlayerSetWeeklyMission1(playerSaver);
                PlayerSetWeeklyMission2(playerSaver);
                PlayerSetWeeklyMission3(playerSaver);
                WeeklyMissionCountdown = MaxWeeklyMissionCountdown;
                MissionsData.setPlayerWeeklyRerollTimes((playerSaver), totalWeeklyRerolls);
            }
        }
    }

    public static void SetupDailyMissions() {
        mobsForDailyMission.add(EntityType.ZOMBIE);
        amountOfMobToKillForDailyMission.add(20);
        mobsForDailyMission.add(EntityType.DROWNED);
        amountOfMobToKillForDailyMission.add(12);
        mobsForDailyMission.add(EntityType.BEE);
        amountOfMobToKillForDailyMission.add(2);
        mobsForDailyMission.add(EntityType.PIG);
        amountOfMobToKillForDailyMission.add(5);
        mobsForDailyMission.add(EntityType.SHEEP);
        amountOfMobToKillForDailyMission.add(5);
        mobsForDailyMission.add(EntityType.COW);
        amountOfMobToKillForDailyMission.add(5);
        mobsForDailyMission.add(EntityType.SKELETON);
        amountOfMobToKillForDailyMission.add(8);
        mobsForDailyMission.add(EntityType.PIGLIN);
        amountOfMobToKillForDailyMission.add(3);
        mobsForDailyMission.add(EntityType.COD);
        amountOfMobToKillForDailyMission.add(3);
        mobsForDailyMission.add(EntityType.SQUID);
        amountOfMobToKillForDailyMission.add(3);
        mobsForDailyMission.add(EntityType.SALMON);
        amountOfMobToKillForDailyMission.add(3);
        mobsForDailyMission.add(EntityType.SPIDER);
        amountOfMobToKillForDailyMission.add(8);
        mobsForDailyMission.add(EntityType.ENDERMAN);
        amountOfMobToKillForDailyMission.add(1);
        mobsForDailyMission.add(EntityType.CHICKEN);
        amountOfMobToKillForDailyMission.add(10);
        mobsForDailyMission.add(EntityType.CREEPER);
        amountOfMobToKillForDailyMission.add(5);

        itemsForDailyMission.add(Items.APPLE.getDefaultStack());
        amountOfItemsForDailyMission.add(3);
        itemsForDailyMission.add(Items.STICK.getDefaultStack());
        amountOfItemsForDailyMission.add(64);
        itemsForDailyMission.add(Items.CARROT.getDefaultStack());
        amountOfItemsForDailyMission.add(32);
        itemsForDailyMission.add(Items.POTATO.getDefaultStack());
        amountOfItemsForDailyMission.add(32);
        itemsForDailyMission.add(Items.WHEAT.getDefaultStack());
        amountOfItemsForDailyMission.add(32);
        itemsForDailyMission.add(Items.COPPER_INGOT.getDefaultStack());
        amountOfItemsForDailyMission.add(15);
        itemsForDailyMission.add(Items.GUNPOWDER.getDefaultStack());
        amountOfItemsForDailyMission.add(15);
        itemsForDailyMission.add(Items.ROTTEN_FLESH.getDefaultStack());
        amountOfItemsForDailyMission.add(32);
        itemsForDailyMission.add(Items.COAL.getDefaultStack());
        amountOfItemsForDailyMission.add(20);
        itemsForDailyMission.add(Items.ANDESITE.getDefaultStack());
        amountOfItemsForDailyMission.add(64);
        itemsForDailyMission.add(Items.COBBLESTONE.getDefaultStack());
        amountOfItemsForDailyMission.add(64);
        itemsForDailyMission.add(Items.STONE.getDefaultStack());
        amountOfItemsForDailyMission.add(32);
        itemsForDailyMission.add(Items.SMOOTH_STONE.getDefaultStack());
        amountOfItemsForDailyMission.add(32);
        itemsForDailyMission.add(Items.DIORITE.getDefaultStack());
        amountOfItemsForDailyMission.add(48);
        itemsForDailyMission.add(Items.RED_DYE.getDefaultStack());
        amountOfItemsForDailyMission.add(10);
        itemsForDailyMission.add(Items.WHITE_DYE.getDefaultStack());
        amountOfItemsForDailyMission.add(15);
        itemsForDailyMission.add(Items.YELLOW_DYE.getDefaultStack());
        amountOfItemsForDailyMission.add(10);
        itemsForDailyMission.add(Items.BLACK_DYE.getDefaultStack());
        amountOfItemsForDailyMission.add(10);
        itemsForDailyMission.add(Items.ARROW.getDefaultStack());
        amountOfItemsForDailyMission.add(20);
        itemsForDailyMission.add(Items.OAK_SAPLING.getDefaultStack());
        amountOfItemsForDailyMission.add(5);
        itemsForDailyMission.add(Items.BIRCH_SAPLING.getDefaultStack());
        amountOfItemsForDailyMission.add(3);
        itemsForDailyMission.add(Items.TORCH.getDefaultStack());
        amountOfItemsForDailyMission.add(128);
        itemsForDailyMission.add(Items.QUARTZ.getDefaultStack());
        amountOfItemsForDailyMission.add(16);
        itemsForDailyMission.add(Items.SPIDER_EYE.getDefaultStack());
        amountOfItemsForDailyMission.add(1);
        itemsForDailyMission.add(Items.POINTED_DRIPSTONE.getDefaultStack());
        amountOfItemsForDailyMission.add(3);
        itemsForDailyMission.add(Items.COOKIE.getDefaultStack());
        amountOfItemsForDailyMission.add(1);

    }
    public static void SetupWeeklyMissions() {
        itemsForWeeklyMission.add(Items.AXOLOTL_BUCKET.getDefaultStack());
        amountOfItemsForWeeklyMission.add(3);
        itemsForWeeklyMission.add(Items.ACTIVATOR_RAIL.getDefaultStack());
        amountOfItemsForWeeklyMission.add(64);
        itemsForWeeklyMission.add(Items.OBSERVER.getDefaultStack());
        amountOfItemsForWeeklyMission.add(64);
        itemsForWeeklyMission.add(Items.REPEATER.getDefaultStack());
        amountOfItemsForWeeklyMission.add(64);
        itemsForWeeklyMission.add(Items.COMPARATOR.getDefaultStack());
        amountOfItemsForWeeklyMission.add(64);
        itemsForWeeklyMission.add(Items.TRAPPED_CHEST.getDefaultStack());
        amountOfItemsForWeeklyMission.add(64);
        itemsForWeeklyMission.add(Items.HONEY_BOTTLE.getDefaultStack());
        amountOfItemsForWeeklyMission.add(10);
        itemsForWeeklyMission.add(Items.HONEYCOMB.getDefaultStack());
        amountOfItemsForWeeklyMission.add(15);
        itemsForWeeklyMission.add(Items.LIME_DYE.getDefaultStack());
        amountOfItemsForWeeklyMission.add(32);
        itemsForWeeklyMission.add(Items.BAMBOO.getDefaultStack());
        amountOfItemsForWeeklyMission.add(64);
        itemsForWeeklyMission.add(Items.BEE_NEST.getDefaultStack());
        amountOfItemsForWeeklyMission.add(1);
        itemsForWeeklyMission.add(Items.ACACIA_LOG.getDefaultStack());
        amountOfItemsForWeeklyMission.add(64);
        itemsForWeeklyMission.add(Items.MANGROVE_LOG.getDefaultStack());
        amountOfItemsForWeeklyMission.add(32);
        itemsForWeeklyMission.add(Items.WARPED_STEM.getDefaultStack());
        amountOfItemsForWeeklyMission.add(32);
        itemsForWeeklyMission.add(Items.CRIMSON_STEM.getDefaultStack());
        amountOfItemsForWeeklyMission.add(48);
        itemsForWeeklyMission.add(Items.BELL.getDefaultStack());
        amountOfItemsForWeeklyMission.add(12);
        itemsForWeeklyMission.add(Items.TOTEM_OF_UNDYING.getDefaultStack());
        amountOfItemsForWeeklyMission.add(1);
        itemsForWeeklyMission.add(Items.GOLDEN_APPLE.getDefaultStack());
        amountOfItemsForWeeklyMission.add(10);
        itemsForWeeklyMission.add(Items.AMETHYST_SHARD.getDefaultStack());
        amountOfItemsForWeeklyMission.add(13);
        itemsForWeeklyMission.add(Items.SHULKER_SHELL.getDefaultStack());
        amountOfItemsForWeeklyMission.add(16);
        itemsForWeeklyMission.add(Items.ENDER_PEARL.getDefaultStack());
        amountOfItemsForWeeklyMission.add(12);
        itemsForWeeklyMission.add(Items.NAME_TAG.getDefaultStack());
        amountOfItemsForWeeklyMission.add(20);
        itemsForWeeklyMission.add(Items.WITHER_ROSE.getDefaultStack());
        amountOfItemsForWeeklyMission.add(1);
        itemsForWeeklyMission.add(Items.GHAST_TEAR.getDefaultStack());
        amountOfItemsForWeeklyMission.add(2);
        itemsForWeeklyMission.add(Items.WITHER_SKELETON_SKULL.getDefaultStack());
        amountOfItemsForWeeklyMission.add(1);
        itemsForWeeklyMission.add(Items.PRISMARINE_SHARD.getDefaultStack());
        amountOfItemsForWeeklyMission.add(16);
        itemsForWeeklyMission.add(Items.PRISMARINE_CRYSTALS.getDefaultStack());
        amountOfItemsForWeeklyMission.add(16);
        itemsForWeeklyMission.add(Items.TURTLE_EGG.getDefaultStack());
        amountOfItemsForWeeklyMission.add(2);
        itemsForWeeklyMission.add(Items.POWDER_SNOW_BUCKET.getDefaultStack());
        amountOfItemsForWeeklyMission.add(1);
        itemsForWeeklyMission.add(Items.GOAT_HORN.getDefaultStack());
        amountOfItemsForWeeklyMission.add(1);
        itemsForWeeklyMission.add(Items.CHORUS_FRUIT.getDefaultStack());
        amountOfItemsForWeeklyMission.add(128);
        itemsForWeeklyMission.add(Items.IRON_HORSE_ARMOR.getDefaultStack());
        amountOfItemsForWeeklyMission.add(1);
        itemsForWeeklyMission.add(Items.DRAGON_BREATH.getDefaultStack());
        amountOfItemsForWeeklyMission.add(2);
        itemsForWeeklyMission.add(Items.LEAD.getDefaultStack());
        amountOfItemsForWeeklyMission.add(2);

        mobsForWeeklyMission.add(EntityType.WITHER);
        amountOfMobToKillForWeeklyMission.add(1);
        mobsForWeeklyMission.add(EntityType.BEE);
        amountOfMobToKillForWeeklyMission.add(10);
        mobsForWeeklyMission.add(EntityType.ZOMBIFIED_PIGLIN);
        amountOfMobToKillForWeeklyMission.add(15);
        mobsForWeeklyMission.add(EntityType.BLAZE);
        amountOfMobToKillForWeeklyMission.add(10);
        mobsForWeeklyMission.add(EntityType.WITHER_SKELETON);
        amountOfMobToKillForWeeklyMission.add(8);
        mobsForWeeklyMission.add(EntityType.GHAST);
        amountOfMobToKillForWeeklyMission.add(5);
        mobsForWeeklyMission.add(EntityType.WARDEN);
        amountOfMobToKillForWeeklyMission.add(1);
        mobsForWeeklyMission.add(EntityType.ENDER_DRAGON);
        amountOfMobToKillForWeeklyMission.add(1);
        mobsForWeeklyMission.add(EntityType.ZOGLIN);
        amountOfMobToKillForWeeklyMission.add(1);
        mobsForWeeklyMission.add(EntityType.HOGLIN);
        amountOfMobToKillForWeeklyMission.add(4);
        mobsForWeeklyMission.add(EntityType.WITCH);
        amountOfMobToKillForWeeklyMission.add(2);
        mobsForWeeklyMission.add(EntityType.WANDERING_TRADER);
        amountOfMobToKillForWeeklyMission.add(1);
        mobsForWeeklyMission.add(EntityType.TROPICAL_FISH);
        amountOfMobToKillForWeeklyMission.add(2);
        mobsForWeeklyMission.add(EntityType.STRIDER);
        amountOfMobToKillForWeeklyMission.add(2);
        mobsForWeeklyMission.add(EntityType.ZOMBIE);
        amountOfMobToKillForWeeklyMission.add(100);
        mobsForWeeklyMission.add(EntityType.SKELETON);
        amountOfMobToKillForWeeklyMission.add(80);
        mobsForWeeklyMission.add(EntityType.SPIDER);
        amountOfMobToKillForWeeklyMission.add(75);
        mobsForWeeklyMission.add(EntityType.SHULKER);
        amountOfMobToKillForWeeklyMission.add(12);
        mobsForWeeklyMission.add(EntityType.SLIME);
        amountOfMobToKillForWeeklyMission.add(20);
        mobsForWeeklyMission.add(EntityType.SNOW_GOLEM);
        amountOfMobToKillForWeeklyMission.add(100);
        mobsForWeeklyMission.add(EntityType.POLAR_BEAR);
        amountOfMobToKillForWeeklyMission.add(1);
        mobsForWeeklyMission.add(EntityType.PUFFERFISH);
        amountOfMobToKillForWeeklyMission.add(2);
        mobsForWeeklyMission.add(EntityType.PLAYER);
        amountOfMobToKillForWeeklyMission.add(3);
        mobsForWeeklyMission.add(EntityType.PIGLIN_BRUTE);
        amountOfMobToKillForWeeklyMission.add(3);
        mobsForWeeklyMission.add(EntityType.BAT);
        amountOfMobToKillForWeeklyMission.add(10);
        mobsForWeeklyMission.add(EntityType.RABBIT);
        amountOfMobToKillForWeeklyMission.add(1);
        mobsForWeeklyMission.add(EntityType.PHANTOM);
        amountOfMobToKillForWeeklyMission.add(10);
        mobsForWeeklyMission.add(EntityType.HORSE);
        amountOfMobToKillForWeeklyMission.add(4);
        mobsForWeeklyMission.add(EntityType.IRON_GOLEM);
        amountOfMobToKillForWeeklyMission.add(5);
        mobsForWeeklyMission.add(EntityType.ENDERMITE);
        amountOfMobToKillForWeeklyMission.add(3);
        mobsForWeeklyMission.add(EntityType.HUSK);
        amountOfMobToKillForWeeklyMission.add(3);

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

    public static boolean serverRestarted = true;
    public static boolean serverRestartedForMissionTime = true;
}
