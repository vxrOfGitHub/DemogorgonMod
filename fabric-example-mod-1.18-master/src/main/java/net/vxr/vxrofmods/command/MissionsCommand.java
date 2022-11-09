package net.vxr.vxrofmods.command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.vxr.vxrofmods.event.ServerTickHandler;
import net.vxr.vxrofmods.util.IEntityDataSaver;
import net.vxr.vxrofmods.util.MissionsData;
import net.vxr.vxrofmods.util.MissionsWeeklyData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MissionsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {


        serverCommandSourceCommandDispatcher.register(CommandManager.literal("missions")
            .executes(context -> runOutputBothMissions(context))
                // Daily Missions
            .then(CommandManager.literal("daily")
                .executes((context) -> runOutputDailyMissions(context))
            .then(CommandManager.literal( "time").requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("reset")
                .executes(context -> runResetDailyMissions(context)))
            .then(CommandManager.literal("set")
                .then(CommandManager.argument("timeInTicks", IntegerArgumentType.integer(0))
                    .executes(context -> runSetDailyMissions(context, IntegerArgumentType.getInteger(context, "timeInTicks"))))))
            .then(CommandManager.literal("add").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("desiredItem", ItemStackArgumentType.itemStack(commandRegistryAccess))
                    .then(CommandManager.argument("amountOfItemToComplete", IntegerArgumentType.integer(1))
                        .executes(context -> runAddDailyMissionsItems(context, ItemStackArgumentType.getItemStackArgument(context, "desiredItem"),
                            IntegerArgumentType.getInteger(context, "amountOfItemToComplete")))))
                    .then(CommandManager.argument("MobToKill", EntityArgumentType.entities())
                            .then(CommandManager.argument("amountToKill", IntegerArgumentType.integer(1))
                                    .executes(context -> runAddDailyMissionsMobs(context, EntityArgumentType.getEntity(context, "MobToKill"),
                                            IntegerArgumentType.getInteger(context, "amountToKill")))))
                    .then(CommandManager.literal("testPreset").executes(context -> runAddTestPresetDailyMissions(context))))
                    .then(CommandManager.literal("remove").requires(source -> source.hasPermissionLevel(2))
                            .then(CommandManager.argument("numberInList", IntegerArgumentType.integer())
                                    .executes(context -> runRemoveDailyMission(context, IntegerArgumentType.getInteger(context, "numberInList")))))
                    .then(CommandManager.literal("reroll").requires(source -> source.hasPermissionLevel(2))
                            .then(CommandManager.argument("target", EntityArgumentType.players())
                                    .then(CommandManager.literal("1")
                                    .executes(context -> runRerollDailyMissions1(context, EntityArgumentType.getPlayers(context, "target"))))
                                    .then(CommandManager.literal("2")
                                            .executes(context -> runRerollDailyMissions2(context, EntityArgumentType.getPlayers(context, "target"))))
                                    .then(CommandManager.literal("3")
                                            .executes(context -> runRerollDailyMissions3(context, EntityArgumentType.getPlayers(context, "target"))))
                                    .then(CommandManager.literal("all")
                                            .executes(context -> runRerollAllDailyMissions(context, EntityArgumentType.getPlayers(context, "target"))))))
                    .then(CommandManager.literal("complete").then(CommandManager.literal("1")
                            .executes(context -> runCompleteDailyMission1(context)))
                            .then(CommandManager.literal("2").executes(context -> runCompleteDailyMission2(context)))
                            .then(CommandManager.literal("3").executes(context -> runCompleteDailyMission3(context)))
                            .then(CommandManager.literal("all").executes(context -> runCompleteAllDailyMissions(context)))
                    .executes(context -> runCompleteAllDailyMissions(context)))
                    .then(CommandManager.literal("list")
                            .executes(context -> runOuputDailyMissionsList(context))))
                // Weekly Missions
            .then(CommandManager.literal("weekly")
                .executes(context -> runOutputWeeklyMissions(context))
                    .then(CommandManager.literal("time").requires(source -> source.hasPermissionLevel(2))
                            .then(CommandManager.literal("reset")
                                    .executes(context -> runResetWeeklyMissions(context)))
                            .then(CommandManager.literal("set")
                                    .then(CommandManager.argument("timeInTicks", IntegerArgumentType.integer(0))
                                            .executes(context -> runSetWeeklyMissions(context, IntegerArgumentType.getInteger(context, "timeInTicks"))))))
                    .then(CommandManager.literal("add").requires(source -> source.hasPermissionLevel(2))
                            .then(CommandManager.argument("desiredItem", ItemStackArgumentType.itemStack(commandRegistryAccess))
                                    .then(CommandManager.argument("amountOfItemToComplete", IntegerArgumentType.integer(1))
                                            .executes(context -> runAddWeeklyMissionsItems(context, ItemStackArgumentType.getItemStackArgument(context, "desiredItem"),
                                                    IntegerArgumentType.getInteger(context, "amountOfItemToComplete")))))
                            .then(CommandManager.argument("MobToKill", EntityArgumentType.entities())
                                    .then(CommandManager.argument("amountToKill", IntegerArgumentType.integer(1))
                                            .executes(context -> runAddWeeklyMissionsMobs(context, EntityArgumentType.getEntity(context, "MobToKill"),
                                                    IntegerArgumentType.getInteger(context, "amountToKill")))))
                            .then(CommandManager.literal("testPreset").executes(context -> runAddTestPresetWeeklyMissions(context))))
                    .then(CommandManager.literal("remove").requires(source -> source.hasPermissionLevel(2))
                            .then(CommandManager.argument("numberInList", IntegerArgumentType.integer())
                                    .executes(context -> runRemoveWeeklyMission(context, IntegerArgumentType.getInteger(context, "numberInList")))))
                    .then(CommandManager.literal("reroll").requires(source -> source.hasPermissionLevel(2))
                            .then(CommandManager.argument("target", EntityArgumentType.players())
                                    .then(CommandManager.literal("1")
                                            .executes(context -> runRerollWeeklyMissions1(context, EntityArgumentType.getPlayers(context, "target"))))
                                    .then(CommandManager.literal("2")
                                            .executes(context -> runRerollWeeklyMissions2(context, EntityArgumentType.getPlayers(context, "target"))))
                                    .then(CommandManager.literal("3")
                                            .executes(context -> runRerollWeeklyMissions3(context, EntityArgumentType.getPlayers(context, "target"))))
                                    .then(CommandManager.literal("all")
                                            .executes(context -> runRerollAllWeeklyMissions(context, EntityArgumentType.getPlayers(context, "target"))))))
                    .then(CommandManager.literal("complete").then(CommandManager.literal("1")
                                    .executes(context -> runCompleteWeeklyMission1(context)))
                            .then(CommandManager.literal("2").executes(context -> runCompleteWeeklyMission2(context)))
                            .then(CommandManager.literal("3").executes(context -> runCompleteWeeklyMission3(context)))
                            .then(CommandManager.literal("all").executes(context -> runCompleteAllWeeklyMissions(context)))
                            .executes(context -> runCompleteAllWeeklyMissions(context)))
                    .then(CommandManager.literal("list")
                            .executes(context -> runOuputWeeklyMissionsList(context)))));




    }
    public static final int rewardAmountDailyMission = 100;
    public static final int rewardAmountWeeklyMission = 1000;


    // ------------ Weekly Missions -------------------

    private static int runAddWeeklyMissionsItems(CommandContext<ServerCommandSource> context, ItemStackArgument stack, int amountOfItemsNeeded) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        ServerTickHandler.itemsForWeeklyMission.add(stack.getItem().getDefaultStack());
        ServerTickHandler.amountOfItemsForWeeklyMission.add(amountOfItemsNeeded);

        context.getSource().sendFeedback(Text.literal(Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + " added to Weekly Missions:"), true);
        context.getSource().sendFeedback(Text.literal("Collect: " + amountOfItemsNeeded + "x " + stack.getItem().getName().getString()), true);

        return 1;
    }

    private static int runOuputWeeklyMissionsList(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        if(ServerTickHandler.itemsForWeeklyMission.size() <= 0 && ServerTickHandler.mobsForWeeklyMission.size() <= 0){
            context.getSource().sendFeedback(Text.literal("§cNo Weekly Mission yet!§r"), false);
        } else {
            // Collect Item Dailies
            context.getSource().sendFeedback(Text.literal("§d§nAvailable Weekly Mission!§r§r"), false);
            for(int i = 0;i < ServerTickHandler.itemsForWeeklyMission.size();i++) {
                int i1 = i + 1;
                context.getSource().sendFeedback(Text.literal("§3" + i1 + ".§r Collect: " + ServerTickHandler.amountOfItemsForWeeklyMission.get(i) + "x §3"
                        + ServerTickHandler.itemsForWeeklyMission.get(i).getItem().getName().getString() + "§r"), false);
            }
            // Mob kill Dailies
            for(int i = 0;i < ServerTickHandler.mobsForWeeklyMission.size();i++) {
                int i1 = i + 1 + ServerTickHandler.itemsForWeeklyMission.size();
                context.getSource().sendFeedback(Text.literal("§3" + i1 + ".§r Kill: " + ServerTickHandler.amountOfMobToKillForWeeklyMission.get(i) + "x §3"
                        + ServerTickHandler.mobsForWeeklyMission.get(i).getName().getString() + "§r"), false);
            }
        }
        return 1;
    }
    private static int runCompleteAllWeeklyMissions(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        runCompleteWeeklyMission1(context);
        runCompleteWeeklyMission2(context);
        runCompleteWeeklyMission3(context);

        return 1;
    }

    private static int runCompleteWeeklyMission1(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = ((IEntityDataSaver) context.getSource().getPlayer());
        PlayerEntity player = context.getSource().getPlayer();

        assert playerSaver != null;

        if(MissionsWeeklyData.getWeeklyMissionType1(playerSaver) == 0) {

            ItemStack stack = ServerTickHandler.itemsForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission1(playerSaver));
            int amountNeeded = ServerTickHandler.amountOfItemsForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission1(playerSaver));

            assert player != null;
            completeWeeklyItemMission(player, playerSaver, stack, amountNeeded, 1);
        } else {
            assert player != null;
            player.sendMessage(Text.literal("§cMission 1 is not a §o'Collect'§r§c-Mission!§r"));
        }
        return 1;
    }
    private static int runCompleteWeeklyMission2(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = ((IEntityDataSaver) context.getSource().getPlayer());
        PlayerEntity player = context.getSource().getPlayer();

        assert playerSaver != null;

        if(MissionsWeeklyData.getWeeklyMissionType2(playerSaver) == 0) {

            ItemStack stack = ServerTickHandler.itemsForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission2(playerSaver));
            int amountNeeded = ServerTickHandler.amountOfItemsForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission2(playerSaver));

            assert player != null;
            completeWeeklyItemMission(player, playerSaver, stack, amountNeeded, 2);
        } else {
            assert player != null;
            player.sendMessage(Text.literal("§cMission 2 is not a §o'Collect'§r§c-Mission!§r"));
        }
        return 1;
    }
    private static int runCompleteWeeklyMission3(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = ((IEntityDataSaver) context.getSource().getPlayer());
        PlayerEntity player = context.getSource().getPlayer();

        assert playerSaver != null;

        if(MissionsWeeklyData.getWeeklyMissionType3(playerSaver) == 0) {

            ItemStack stack = ServerTickHandler.itemsForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission3(playerSaver));
            int amountNeeded = ServerTickHandler.amountOfItemsForWeeklyMission.get(MissionsWeeklyData.getWeeklyMission3(playerSaver));

            assert player != null;
            completeWeeklyItemMission(player, playerSaver, stack, amountNeeded, 3);
        } else {
            assert player != null;
            player.sendMessage(Text.literal("§cMission 3 is not a §o'Collect'§r§c-Mission!§r"));
        }
        return 1;
    }

    private static void completeWeeklyItemMission(PlayerEntity player, IEntityDataSaver playerSaver,  ItemStack stack, int amountNeeded, int missionNumber) {
        if(!MissionsWeeklyData.getWeeklyMissionComplete(playerSaver, missionNumber)) {
            int amountInInventory = 0;
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack currentStack = player.getInventory().getStack(i);
                if(!currentStack.isEmpty() && currentStack.getItem().equals(stack.getItem())) {
                    amountInInventory = amountInInventory + currentStack.getCount();
                }
            }
            System.out.println("--------You have " + amountInInventory + "x " + stack.getName().getString());
            if(amountInInventory >= amountNeeded) {
                MissionsWeeklyData.setWeeklyMissionComplete(playerSaver, missionNumber, true);
                player.sendMessage(Text.literal("§aCongratulations! You completed Mission " + missionNumber + " !§r"));
                player.sendMessage(Text.literal("§3REWARD:§r §6§l" + rewardAmountWeeklyMission + " Coins§r§r"));

                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack currentStack = player.getInventory().getStack(i);
                    if(!currentStack.isEmpty() && currentStack.getItem().equals(stack.getItem())) {
                        if(amountNeeded >= currentStack.getCount()) {
                            amountNeeded = amountNeeded - currentStack.getCount();
                            currentStack.decrement(currentStack.getCount());
                        } else {
                            currentStack.decrement(amountNeeded);
                            amountNeeded = 0;
                            i = player.getInventory().size();
                            break;
                        }
                    }
                }
            }
            else {
                amountNeeded = amountNeeded - amountInInventory;
                player.sendMessage(Text.literal("§cYou need §3" + amountNeeded + "x " + stack.getItem().getName().getString() + "§r §cmore to complete Mission " + missionNumber + " !§r"));
            }
        } else {
            player.sendMessage(Text.literal("§cYou already completed Mission " + missionNumber + " !§r"));
        }
    }

    private static int runRemoveWeeklyMission(CommandContext<ServerCommandSource> context, int numberInList) throws CommandSyntaxException {

        if(numberInList > ServerTickHandler.itemsForWeeklyMission.size()) {
            int x = numberInList - ServerTickHandler.itemsForWeeklyMission.size();
            x--;
            int y = x + 1 + ServerTickHandler.itemsForWeeklyMission.size();
            context.getSource().sendFeedback(Text.literal(Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + " removed a Weekly Mission:"), true);
            context.getSource().sendFeedback(Text.literal(y + ". Kill: " + ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) + "x " + ServerTickHandler.mobsForWeeklyMission.get(x).getName().getString()), true);
            ServerTickHandler.mobsForWeeklyMission.remove(x);
            ServerTickHandler.amountOfMobToKillForWeeklyMission.remove(x);
        } else {
            numberInList--;
            int x = numberInList + 1;
            context.getSource().sendFeedback(Text.literal(Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + " removed a Weekly Mission:"), true);
            context.getSource().sendFeedback(Text.literal(x + ". Collect: " + ServerTickHandler.amountOfItemsForWeeklyMission.get(numberInList) + "x " + ServerTickHandler.itemsForWeeklyMission.get(numberInList).getItem().getName().getString()), true);
            ServerTickHandler.itemsForWeeklyMission.remove(numberInList);
            ServerTickHandler.amountOfItemsForDailyMission.remove(numberInList);
        }

        return 1;
    }

    private static int runRerollAllWeeklyMissions(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> target) throws CommandSyntaxException {

        runRerollWeeklyMissions1(context, target);
        runRerollWeeklyMissions2(context, target);
        runRerollWeeklyMissions3(context, target);

        return 1;
    }

    private static int runAddTestPresetWeeklyMissions(CommandContext<ServerCommandSource> context)throws CommandSyntaxException {

        ServerTickHandler.itemsForWeeklyMission.add(Items.SALMON.getDefaultStack());
        ServerTickHandler.amountOfItemsForWeeklyMission.add(10);
        ServerTickHandler.itemsForWeeklyMission.add(Items.WATER_BUCKET.getDefaultStack());
        ServerTickHandler.amountOfItemsForWeeklyMission.add(2);
        ServerTickHandler.itemsForWeeklyMission.add(Items.STICK.getDefaultStack());
        ServerTickHandler.amountOfItemsForWeeklyMission.add(20);

        ServerTickHandler.mobsForWeeklyMission.add(EntityType.PIG);
        ServerTickHandler.amountOfMobToKillForWeeklyMission.add(1);
        ServerTickHandler.mobsForWeeklyMission.add(EntityType.SKELETON);
        ServerTickHandler.amountOfMobToKillForWeeklyMission.add(2);
        ServerTickHandler.mobsForWeeklyMission.add(EntityType.SHEEP);
        ServerTickHandler.amountOfMobToKillForWeeklyMission.add(3);

        return 1;
    }

    private static int runRerollWeeklyMissions1(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        for (int i = 0; i < targets.size(); i++) {
            assert playerSaver != null;

            MissionsWeeklyData.setRandomWeeklyMission1(playerSaver, ServerTickHandler.itemsForWeeklyMission, ServerTickHandler.mobsForWeeklyMission);

            assert player != null;
            ((ServerPlayerEntity) targets.toArray()[i]).sendMessage(Text.literal(player.getName().getString() + " has rerolled the" +
                    " 1. Weekly Mission of " + ((ServerPlayerEntity) targets.toArray()[i]).getName().getString()));
        }


        return 1;
    }
    private static int runRerollWeeklyMissions2(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        for (int i = 0; i < targets.size(); i++) {
            assert playerSaver != null;

            MissionsWeeklyData.setRandomWeeklyMission2(playerSaver, ServerTickHandler.itemsForWeeklyMission, ServerTickHandler.mobsForWeeklyMission);

            assert player != null;
            ((ServerPlayerEntity) targets.toArray()[i]).sendMessage(Text.literal(player.getName().getString() + " has rerolled the" +
                    " 1. Weekly Mission of " + ((ServerPlayerEntity) targets.toArray()[i]).getName().getString()));
        }


        return 1;
    }
    private static int runRerollWeeklyMissions3(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        for (int i = 0; i < targets.size(); i++) {
            assert playerSaver != null;

            MissionsWeeklyData.setRandomWeeklyMission3(playerSaver, ServerTickHandler.itemsForWeeklyMission, ServerTickHandler.mobsForWeeklyMission);

            assert player != null;
            ((ServerPlayerEntity) targets.toArray()[i]).sendMessage(Text.literal(player.getName().getString() + " has rerolled the" +
                    " 1. Weekly Mission of " + ((ServerPlayerEntity) targets.toArray()[i]).getName().getString()));
        }


        return 1;
    }

    private static int runAddWeeklyMissionsMobs(CommandContext<ServerCommandSource> context, Entity mobToKill, int amountToKill) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        ServerTickHandler.mobsForWeeklyMission.add(mobToKill.getType());
        ServerTickHandler.amountOfMobToKillForWeeklyMission.add(amountToKill);

        context.getSource().sendFeedback(Text.literal(Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + " added to Weekly Missions:"), true);
        context.getSource().sendFeedback(Text.literal("Kill: " + amountToKill + "x " + mobToKill.getType().getName().getString()), true);

        return 1;
    }

    private static int runOutputWeeklyMissions(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        assert player != null;
        player.sendMessage(Text.literal("§d§nWeekly Mission:§r§r"));
        assert playerSaver != null;
        player.sendMessage(Text.literal("§3Time Left: " + MissionsData.getWeeklyMissionTime(playerSaver) + "§r"));
        player.sendMessage(Text.literal(getWeeklyMission(playerSaver, 1)));
        player.sendMessage(Text.literal(getWeeklyMission(playerSaver, 2)));
        player.sendMessage(Text.literal(getWeeklyMission(playerSaver, 3)));

        return 1;
    }

    private static String getWeeklyMission(IEntityDataSaver playerSaver, int missionNumber) {

        String weeklyMission;

        if(missionNumber <= 1) {

            int x = MissionsWeeklyData.getWeeklyMission1(playerSaver);

            if(MissionsWeeklyData.getWeeklyMissionType1(playerSaver) == 0) {

                weeklyMission = missionNumber + ". Collect: " + ServerTickHandler.amountOfItemsForWeeklyMission.get(x) +
                        "x " + ServerTickHandler.itemsForWeeklyMission.get(x).getItem().getName().getString();

                if(MissionsWeeklyData.getWeeklyMissionComplete(playerSaver,missionNumber)) {
                    weeklyMission = "§2§m" + weeklyMission + "§r";
                }

            } else {

                if(MissionsWeeklyData.getWeeklyMissionComplete(playerSaver, missionNumber)) {
                    weeklyMission = "§2§m" + missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) +
                            "x " + ServerTickHandler.mobsForWeeklyMission.get(x).getName().getString() +
                            "§r§r §2(" + MissionsWeeklyData.getWeeklyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) + ")§r";
                } else {
                    weeklyMission = missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) +
                            "x " + ServerTickHandler.mobsForWeeklyMission.get(x).getName().getString() +
                            " (" + MissionsWeeklyData.getWeeklyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) + ")";
                }
            }
        } else if (missionNumber == 2) {

            int x = MissionsWeeklyData.getWeeklyMission2(playerSaver);

            if(MissionsWeeklyData.getWeeklyMissionType2(playerSaver) == 0) {

                weeklyMission = missionNumber + ". Collect: " + ServerTickHandler.amountOfItemsForWeeklyMission.get(x) +
                        "x " + ServerTickHandler.itemsForWeeklyMission.get(x).getItem().getName().getString();

                if(MissionsWeeklyData.getWeeklyMissionComplete(playerSaver,missionNumber)) {
                    weeklyMission = "§2§m" + weeklyMission + "§r";
                }
            } else {

                if(MissionsWeeklyData.getWeeklyMissionComplete(playerSaver, missionNumber)) {
                    weeklyMission = "§2§m" + missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) +
                            "x " + ServerTickHandler.mobsForWeeklyMission.get(x).getName().getString() +
                            "§r§r §2(" + MissionsWeeklyData.getWeeklyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) + ")§r";
                }
                else {
                    weeklyMission = missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) +
                            "x " + ServerTickHandler.mobsForWeeklyMission.get(x).getName().getString() +
                            " (" + MissionsWeeklyData.getWeeklyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) + ")";
                }
            }

        } else {

            int x = MissionsWeeklyData.getWeeklyMission3(playerSaver);

            if(MissionsWeeklyData.getWeeklyMissionType3(playerSaver) == 0) {

                weeklyMission = missionNumber + ". Collect: " + ServerTickHandler.amountOfItemsForWeeklyMission.get(x) +
                        "x " + ServerTickHandler.itemsForWeeklyMission.get(x).getItem().getName().getString();

                if(MissionsWeeklyData.getWeeklyMissionComplete(playerSaver,missionNumber)) {
                    weeklyMission = "§2§m" + weeklyMission + "§r";
                }
            } else {
                if(MissionsWeeklyData.getWeeklyMissionComplete(playerSaver, missionNumber)) {
                    weeklyMission = "§2§m" + missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) +
                            "x " + ServerTickHandler.mobsForWeeklyMission.get(x).getName().getString() +
                            "§r§r §2(" + MissionsWeeklyData.getWeeklyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) + ")§r";
                }
                else {
                    weeklyMission = missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) +
                            "x " + ServerTickHandler.mobsForWeeklyMission.get(x).getName().getString() +
                            " (" + MissionsWeeklyData.getWeeklyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForWeeklyMission.get(x) + ")";
                }
            }
        }


        return weeklyMission;
    }


    // -------------- Daily Missions-----------------

    private static int runCompleteAllDailyMissions(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        runCompleteDailyMission1(context);
        runCompleteDailyMission2(context);
        runCompleteDailyMission3(context);

        return 1;
    }

    private static int runCompleteDailyMission1(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = ((IEntityDataSaver) context.getSource().getPlayer());
        PlayerEntity player = context.getSource().getPlayer();

        assert playerSaver != null;

        if(MissionsData.getDailyMissionType1(playerSaver) == 0) {

            ItemStack stack = ServerTickHandler.itemsForDailyMission.get(MissionsData.getDailyMission1(playerSaver));
            int amountNeeded = ServerTickHandler.amountOfItemsForDailyMission.get(MissionsData.getDailyMission1(playerSaver));

            assert player != null;
            completeItemMission(player, playerSaver, stack, amountNeeded, 1);
        } else {
            assert player != null;
            player.sendMessage(Text.literal("§cMission 1 is not a §o'Collect'§r§c-Mission!§r"));
        }
        return 1;
    }
    private static int runCompleteDailyMission2(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = ((IEntityDataSaver) context.getSource().getPlayer());
        PlayerEntity player = context.getSource().getPlayer();

        assert playerSaver != null;

        if(MissionsData.getDailyMissionType2(playerSaver) == 0) {

            ItemStack stack = ServerTickHandler.itemsForDailyMission.get(MissionsData.getDailyMission2(playerSaver));
            int amountNeeded = ServerTickHandler.amountOfItemsForDailyMission.get(MissionsData.getDailyMission2(playerSaver));

            assert player != null;
            completeItemMission(player, playerSaver, stack, amountNeeded, 2);
        } else {
            assert player != null;
            player.sendMessage(Text.literal("§cMission 2 is not a §o'Collect'§r§c-Mission!§r"));
        }
        return 1;
    }
    private static int runCompleteDailyMission3(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = ((IEntityDataSaver) context.getSource().getPlayer());
        PlayerEntity player = context.getSource().getPlayer();

        assert playerSaver != null;

        if(MissionsData.getDailyMissionType3(playerSaver) == 0) {

            ItemStack stack = ServerTickHandler.itemsForDailyMission.get(MissionsData.getDailyMission3(playerSaver));
            int amountNeeded = ServerTickHandler.amountOfItemsForDailyMission.get(MissionsData.getDailyMission3(playerSaver));

            assert player != null;
            completeItemMission(player, playerSaver, stack, amountNeeded, 3);
        } else {
            assert player != null;
            player.sendMessage(Text.literal("§cMission 3 is not a §o'Collect'§r§c-Mission!§r"));
        }
        return 1;
    }

    private static void completeItemMission(PlayerEntity player, IEntityDataSaver playerSaver,  ItemStack stack, int amountNeeded, int missionNumber) {
        if(!MissionsData.getDailyMissionComplete(playerSaver, missionNumber)) {
            int amountInInventory = 0;
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack currentStack = player.getInventory().getStack(i);
                if(!currentStack.isEmpty() && currentStack.getItem().equals(stack.getItem())) {
                    amountInInventory = amountInInventory + currentStack.getCount();
                }
            }
            System.out.println("--------You have " + amountInInventory + "x " + stack.getName().getString());
            if(amountInInventory >= amountNeeded) {
                MissionsData.setDailyMissionComplete(playerSaver, missionNumber, true);
                player.sendMessage(Text.literal("§aCongratulations! You completed Mission " + missionNumber + " !§r"));
                player.sendMessage(Text.literal("§bREWARD:§r §6§l" + rewardAmountDailyMission + " Coins§r§r"));

                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack currentStack = player.getInventory().getStack(i);
                    if(!currentStack.isEmpty() && currentStack.getItem().equals(stack.getItem())) {
                        if(amountNeeded >= currentStack.getCount()) {
                            amountNeeded = amountNeeded - currentStack.getCount();
                            currentStack.decrement(currentStack.getCount());
                        } else {
                            currentStack.decrement(amountNeeded);
                            amountNeeded = 0;
                            i = player.getInventory().size();
                            break;
                        }
                    }
                }
            }
            else {
                amountNeeded = amountNeeded - amountInInventory;
                player.sendMessage(Text.literal("§cYou need §b" + amountNeeded + "x " + stack.getItem().getName().getString() + "§r §cmore to complete Mission " + missionNumber + " !§r"));
            }
        } else {
            player.sendMessage(Text.literal("§cYou already completed Mission " + missionNumber + " !§r"));
        }
    }

    private static int runRemoveDailyMission(CommandContext<ServerCommandSource> context, int numberInList) throws CommandSyntaxException {

        if(numberInList > ServerTickHandler.itemsForDailyMission.size()) {
            int x = numberInList - ServerTickHandler.itemsForDailyMission.size();
            x--;
            int y = x + 1 + ServerTickHandler.itemsForDailyMission.size();
            context.getSource().sendFeedback(Text.literal(Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + " removed a Daily Mission:"), true);
            context.getSource().sendFeedback(Text.literal(y + ". Kill: " + ServerTickHandler.amountOfMobToKillForDailyMission.get(x) + "x " + ServerTickHandler.mobsForDailyMission.get(x).getName().getString()), true);
            ServerTickHandler.mobsForDailyMission.remove(x);
            ServerTickHandler.amountOfMobToKillForDailyMission.remove(x);
        } else {
            numberInList--;
            int x = numberInList + 1;
            context.getSource().sendFeedback(Text.literal(Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + " removed a Daily Mission:"), true);
            context.getSource().sendFeedback(Text.literal(x + ". Collect: " + ServerTickHandler.amountOfItemsForDailyMission.get(numberInList) + "x " + ServerTickHandler.itemsForDailyMission.get(numberInList).getItem().getName().getString()), true);
            ServerTickHandler.itemsForDailyMission.remove(numberInList);
            ServerTickHandler.amountOfItemsForDailyMission.remove(numberInList);
        }

        return 1;
    }

    private static int runRerollAllDailyMissions(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> target) throws CommandSyntaxException {

        runRerollDailyMissions1(context, target);
        runRerollDailyMissions2(context, target);
        runRerollDailyMissions3(context, target);

        return 1;
    }

    private static int runAddTestPresetDailyMissions(CommandContext<ServerCommandSource> context)throws CommandSyntaxException {

        ServerTickHandler.itemsForDailyMission.add(Items.SALMON.getDefaultStack());
        ServerTickHandler.amountOfItemsForDailyMission.add(10);
        ServerTickHandler.itemsForDailyMission.add(Items.WATER_BUCKET.getDefaultStack());
        ServerTickHandler.amountOfItemsForDailyMission.add(2);
        ServerTickHandler.itemsForDailyMission.add(Items.STICK.getDefaultStack());
        ServerTickHandler.amountOfItemsForDailyMission.add(20);

        ServerTickHandler.mobsForDailyMission.add(EntityType.PIG);
        ServerTickHandler.amountOfMobToKillForDailyMission.add(1);
        ServerTickHandler.mobsForDailyMission.add(EntityType.SKELETON);
        ServerTickHandler.amountOfMobToKillForDailyMission.add(2);
        ServerTickHandler.mobsForDailyMission.add(EntityType.SHEEP);
        ServerTickHandler.amountOfMobToKillForDailyMission.add(3);

        return 1;
    }

    private static int runRerollDailyMissions1(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        for (int i = 0; i < targets.size(); i++) {
            assert playerSaver != null;

            MissionsData.setRandomDailyMission1(playerSaver, ServerTickHandler.itemsForDailyMission, ServerTickHandler.mobsForDailyMission);

            assert player != null;
            ((ServerPlayerEntity) targets.toArray()[i]).sendMessage(Text.literal(player.getName().getString() + " has rerolled the" +
                    " 1. Daily Mission of " + ((ServerPlayerEntity) targets.toArray()[i]).getName().getString()));
        }


        return 1;
    }
    private static int runRerollDailyMissions2(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        for (int i = 0; i < targets.size(); i++) {
            assert playerSaver != null;

            MissionsData.setRandomDailyMission2(playerSaver, ServerTickHandler.itemsForDailyMission, ServerTickHandler.mobsForDailyMission);

            assert player != null;
            ((ServerPlayerEntity) targets.toArray()[i]).sendMessage(Text.literal(player.getName().getString() + " has rerolled the" +
                    " 1. Daily Mission of " + ((ServerPlayerEntity) targets.toArray()[i]).getName().getString()));
        }


        return 1;
    }
    private static int runRerollDailyMissions3(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        for (int i = 0; i < targets.size(); i++) {
            assert playerSaver != null;

            MissionsData.setRandomDailyMission3(playerSaver, ServerTickHandler.itemsForDailyMission, ServerTickHandler.mobsForDailyMission);

            assert player != null;
            ((ServerPlayerEntity) targets.toArray()[i]).sendMessage(Text.literal(player.getName().getString() + " has rerolled the" +
                    " 1. Daily Mission of " + ((ServerPlayerEntity) targets.toArray()[i]).getName().getString()));
        }


        return 1;
    }

    private static int runAddDailyMissionsMobs(CommandContext<ServerCommandSource> context, Entity mobToKill, int amountToKill) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        ServerTickHandler.mobsForDailyMission.add(mobToKill.getType());
        ServerTickHandler.amountOfMobToKillForDailyMission.add(amountToKill);

        context.getSource().sendFeedback(Text.literal(Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + " added to Daily Missions:"), true);
        context.getSource().sendFeedback(Text.literal("Kill: " + amountToKill + "x " + mobToKill.getType().getName().getString()), true);

        return 1;
    }

    private static int runOutputDailyMissions(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        assert player != null;
        player.sendMessage(Text.literal("§a§nDaily Mission:§r§r"));
        assert playerSaver != null;
        player.sendMessage(Text.literal("§bTime Left: " + MissionsData.getDailyMissionTime(playerSaver) + "§r"));
        player.sendMessage(Text.literal(getDailyMission(playerSaver, 1)));
        player.sendMessage(Text.literal(getDailyMission(playerSaver, 2)));
        player.sendMessage(Text.literal(getDailyMission(playerSaver, 3)));

        return 1;
    }

    private static String getDailyMission(IEntityDataSaver playerSaver, int missionNumber) {

        String dailyMission;

        if(missionNumber <= 1) {

            int x = MissionsData.getDailyMission1(playerSaver);

            if(MissionsData.getDailyMissionType1(playerSaver) == 0) {

                dailyMission = missionNumber + ". Collect: " + ServerTickHandler.amountOfItemsForDailyMission.get(x) +
                        "x " + ServerTickHandler.itemsForDailyMission.get(x).getItem().getName().getString();

                if(MissionsData.getDailyMissionComplete(playerSaver,missionNumber)) {
                    dailyMission = "§2§m" + dailyMission + "§r";
                }

            } else {

                if(MissionsData.getDailyMissionComplete(playerSaver, missionNumber)) {
                    dailyMission = "§2§m" + missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForDailyMission.get(x) +
                            "x " + ServerTickHandler.mobsForDailyMission.get(x).getName().getString() +
                            "§r§r §2(" + MissionsData.getDailyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForDailyMission.get(x) + ")§r";
                } else {
                    dailyMission = missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForDailyMission.get(x) +
                            "x " + ServerTickHandler.mobsForDailyMission.get(x).getName().getString() +
                            " (" + MissionsData.getDailyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForDailyMission.get(x) + ")";
                }
            }
        } else if (missionNumber == 2) {

            int x = MissionsData.getDailyMission2(playerSaver);

            if(MissionsData.getDailyMissionType2(playerSaver) == 0) {

                dailyMission = missionNumber + ". Collect: " + ServerTickHandler.amountOfItemsForDailyMission.get(x) +
                        "x " + ServerTickHandler.itemsForDailyMission.get(x).getItem().getName().getString();

                if(MissionsData.getDailyMissionComplete(playerSaver,missionNumber)) {
                    dailyMission = "§2§m" + dailyMission + "§r";
                }
            } else {

                if(MissionsData.getDailyMissionComplete(playerSaver, missionNumber)) {
                    dailyMission = "§2§m" + missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForDailyMission.get(x) +
                            "x " + ServerTickHandler.mobsForDailyMission.get(x).getName().getString() +
                            "§r§r §2(" + MissionsData.getDailyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForDailyMission.get(x) + ")§r";
                }
                else {
                    dailyMission = missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForDailyMission.get(x) +
                            "x " + ServerTickHandler.mobsForDailyMission.get(x).getName().getString() +
                            " (" + MissionsData.getDailyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForDailyMission.get(x) + ")";
                }
            }

        } else {

            int x = MissionsData.getDailyMission3(playerSaver);

            if(MissionsData.getDailyMissionType3(playerSaver) == 0) {

                dailyMission = missionNumber + ". Collect: " + ServerTickHandler.amountOfItemsForDailyMission.get(x) +
                        "x " + ServerTickHandler.itemsForDailyMission.get(x).getItem().getName().getString();

                if(MissionsData.getDailyMissionComplete(playerSaver,missionNumber)) {
                    dailyMission = "§2§m" + dailyMission + "§r";
                }
            } else {
                if(MissionsData.getDailyMissionComplete(playerSaver, missionNumber)) {
                    dailyMission = "§2§m" + missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForDailyMission.get(x) +
                            "x " + ServerTickHandler.mobsForDailyMission.get(x).getName().getString() +
                            "§r§r §2(" + MissionsData.getDailyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForDailyMission.get(x) + ")§r";
                }
                else {
                    dailyMission = missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForDailyMission.get(x) +
                            "x " + ServerTickHandler.mobsForDailyMission.get(x).getName().getString() +
                            " (" + MissionsData.getDailyMissionProgress(playerSaver, missionNumber) + "/" +
                            ServerTickHandler.amountOfMobToKillForDailyMission.get(x) + ")";
                }
            }
        }


        return dailyMission;
    }

    private static int runOutputBothMissions(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        runOutputDailyMissions(context);
        runOutputWeeklyMissions(context);

        return 1;
    }

    private static int runResetDailyMissions(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        ServerTickHandler.DailyMissionCountdown = ServerTickHandler.MaxDailyMissionCountdown;

        context.getSource().sendFeedback(Text.literal(
                "§n" + Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + "§r has reset the §aDaily-Mission-Timer§r!"), true);
        assert playerSaver != null;
        context.getSource().sendFeedback(Text.literal(
                "§aDaily-Mission-Time§r left: §b" + MissionsData.getDailyMissionTime(playerSaver) + "§r"), true);

        return 1;
    }

    private static int runSetDailyMissions(CommandContext<ServerCommandSource> context, int newTimeInTicks) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        ServerTickHandler.DailyMissionCountdown = newTimeInTicks;

        assert playerSaver != null;
        context.getSource().sendFeedback(Text.literal(
                "§n" + Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + "§r has set the §aDaily-Mission-Timer§r to §b"
                        + MissionsData.getDailyMissionTime(playerSaver) + "§r"), true);

        return 1;
    }

    private static int runResetWeeklyMissions(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        ServerTickHandler.WeeklyMissionCountdown = ServerTickHandler.MaxWeeklyMissionCountdown;

        context.getSource().sendFeedback(Text.literal(
                "§n" + Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + "§r has reset the §dWeekly-Mission-Timer§r!"), true);
        assert playerSaver != null;
        context.getSource().sendFeedback(Text.literal(
                "§dWeekly-Mission-Time§r left: §3" + MissionsData.getWeeklyMissionTime(playerSaver) + "§r"), true);

        return 1;
    }

    private static int runSetWeeklyMissions(CommandContext<ServerCommandSource> context, int newTimeInTicks) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        ServerTickHandler.WeeklyMissionCountdown = newTimeInTicks;

        assert playerSaver != null;
        context.getSource().sendFeedback(Text.literal(
                "§n" + Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + "§r has set the §dWeekly-Mission-Timer§r to §3"
                        + MissionsData.getWeeklyMissionTime(playerSaver) + "§r"), true);

        return 1;
    }

    private static int runAddDailyMissionsItems(CommandContext<ServerCommandSource> context, ItemStackArgument stack, int amountOfItemsNeeded) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        ServerTickHandler.itemsForDailyMission.add(stack.getItem().getDefaultStack());
        ServerTickHandler.amountOfItemsForDailyMission.add(amountOfItemsNeeded);

        context.getSource().sendFeedback(Text.literal(Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + " added to Daily Missions:"), true);
        context.getSource().sendFeedback(Text.literal("Collect: " + amountOfItemsNeeded + "x " + stack.getItem().getName().getString()), true);

        return 1;
    }

    private static int runOuputDailyMissionsList(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        if(ServerTickHandler.itemsForDailyMission.size() <= 0 && ServerTickHandler.mobsForDailyMission.size() <= 0){
            context.getSource().sendFeedback(Text.literal("§cNo Daily Mission yet!§r"), false);
        } else {
            // Collect Item Dailies
            context.getSource().sendFeedback(Text.literal("§a§nAvailable Daily Mission!§r§r"), false);
            for(int i = 0;i < ServerTickHandler.itemsForDailyMission.size();i++) {
                int i1 = i + 1;
                context.getSource().sendFeedback(Text.literal("§b" + i1 + ".§r Collect: " + ServerTickHandler.amountOfItemsForDailyMission.get(i) + "x §b"
                        + ServerTickHandler.itemsForDailyMission.get(i).getItem().getName().getString() + "§r"), false);
            }
            // Mob kill Dailies
            for(int i = 0;i < ServerTickHandler.mobsForDailyMission.size();i++) {
                int i1 = i + 1 + ServerTickHandler.itemsForDailyMission.size();
                context.getSource().sendFeedback(Text.literal("§b" + i1 + ".§r Kill: " + ServerTickHandler.amountOfMobToKillForDailyMission.get(i) + "x §b"
                        + ServerTickHandler.mobsForDailyMission.get(i).getName().getString() + "§r"), false);
            }
        }
        return 1;
    }

}