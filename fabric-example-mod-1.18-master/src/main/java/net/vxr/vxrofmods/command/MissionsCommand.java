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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.vxr.vxrofmods.event.ServerTickHandler;
import net.vxr.vxrofmods.util.CustomMoneyData;
import net.vxr.vxrofmods.util.IEntityDataSaver;
import net.vxr.vxrofmods.util.MissionsData;

import java.util.Collection;
import java.util.Objects;

public class MissionsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {


        serverCommandSourceCommandDispatcher.register(CommandManager.literal("missions")
            .executes(context -> runOutputBothMissions(context))
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
                                            IntegerArgumentType.getInteger(context, "amountToKill"))))))
                    .then(CommandManager.literal("reroll").requires(source -> source.hasPermissionLevel(2))
                            .then(CommandManager.argument("target", EntityArgumentType.players())
                                    .executes(context -> runRerollDailyMissions(context, EntityArgumentType.getPlayers(context, "target")))))
                    .then(CommandManager.literal("list")
                            .executes(context -> runOuputDailyMissionsList(context))))
            .then(CommandManager.literal("weekly")
                .executes(context -> runOutputWeeklyMissions(context))
            .then(CommandManager.literal("reset").requires(source -> source.hasPermissionLevel(2))
                .executes(context -> runResetWeeklyMissions(context)))
            .then(CommandManager.literal("set").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("timeInTicks", IntegerArgumentType.integer(0))
                    .executes(context -> runSetWeeklyMissions(context, IntegerArgumentType.getInteger(context, "timeInTicks")))))));




    }

    private static int runRerollDailyMissions(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        for (int i = 0; i < targets.size(); i++) {
            MissionsData.setRandomDailyMissions(((IEntityDataSaver) targets.toArray()[i]),
                    ServerTickHandler.itemsForDailyMission, ServerTickHandler.mobsForDailyMission);
            assert player != null;
            ((ServerPlayerEntity) targets.toArray()[i]).sendMessage(Text.literal(player.getName().getString() + "has rerolled the" +
                    " Daily Missions of " + ((ServerPlayerEntity) targets.toArray()[i]).getName().getString()));
        }


        return 1;
    }

    private static int runAddDailyMissionsMobs(CommandContext<ServerCommandSource> context, Entity mobToKill, int amountToKill) throws CommandSyntaxException {
        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        ServerTickHandler.mobsForDailyMission.add(mobToKill.getType());
        ServerTickHandler.amountOfMobToKillForDailyMission.add(amountToKill);

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
        int missionNumberInList = MissionsData.getDailyMissionOfNumber(playerSaver, missionNumber);

        if(MissionsData.isDailyMissionOfNumberTypeItem(playerSaver, missionNumber)) {

            dailyMission = missionNumber + ". Collect: " + ServerTickHandler.amountOfItemsForDailyMission.get(missionNumberInList) +
            "x " + ServerTickHandler.itemsForDailyMission.get(missionNumberInList).getItem().getName().getString();

        } else {
            dailyMission = missionNumber + ". Kill: " + ServerTickHandler.amountOfMobToKillForDailyMission.get(missionNumberInList) +
            "x " + ServerTickHandler.mobsForDailyMission.get(missionNumberInList).getName().getString() +
            " (" + MissionsData.getDailyMissionProgress(playerSaver, missionNumber) + "/" +
            ServerTickHandler.amountOfMobToKillForDailyMission.get(missionNumberInList) + ")";
        }

        return dailyMission;
    }

    private static int runOutputWeeklyMissions(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        assert player != null;
        player.sendMessage(Text.literal("§d§nWeekly Mission:§r§r"));
        assert playerSaver != null;
        player.sendMessage(Text.literal("§bTime Left: " + MissionsData.getWeeklyMissionTime(playerSaver) + "§r"));
        player.sendMessage(Text.literal("1. "));
        player.sendMessage(Text.literal("2. "));
        player.sendMessage(Text.literal("3. "));

        return 1;
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
                "§dWeekly-Mission-Time§r left: §b" + MissionsData.getWeeklyMissionTime(playerSaver) + "§r"), true);

        return 1;
    }

    private static int runSetWeeklyMissions(CommandContext<ServerCommandSource> context, int newTimeInTicks) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        ServerTickHandler.WeeklyMissionCountdown = newTimeInTicks;

        assert playerSaver != null;
        context.getSource().sendFeedback(Text.literal(
                "§n" + Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + "§r has set the §dWeekly-Mission-Timer§r to §b"
                        + MissionsData.getWeeklyMissionTime(playerSaver) + "§r"), true);

        return 1;
    }

    private static int runAddDailyMissionsItems(CommandContext<ServerCommandSource> context, ItemStackArgument stack, int amountOfItemsNeeded) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        ServerTickHandler.itemsForDailyMission.add(stack.getItem().getDefaultStack());
        ServerTickHandler.amountOfItemsForDailyMission.add(amountOfItemsNeeded);

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
