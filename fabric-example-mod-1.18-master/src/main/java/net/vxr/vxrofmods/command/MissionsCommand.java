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
            .then(CommandManager.literal("reset").requires(source -> source.hasPermissionLevel(2))
                .executes(context -> runResetDailyMissions(context)))
            .then(CommandManager.literal("set").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("timeInTicks", IntegerArgumentType.integer(0))
                    .executes(context -> runSetDailyMissions(context, IntegerArgumentType.getInteger(context, "timeInTicks")))))
            .then(CommandManager.literal("add").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("desiredItem", ItemStackArgumentType.itemStack(commandRegistryAccess))
                    .then(CommandManager.argument("amountOfItemToComplete", IntegerArgumentType.integer(1))
                        .executes(context -> runAddDailyMissions(context, ItemStackArgumentType.getItemStackArgument(context, "desiredItem"),
                            IntegerArgumentType.getInteger(context, "amountOfItemToComplete"))))))
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
    private static int runOutputDailyMissions(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        assert player != null;
        player.sendMessage(Text.literal("§a§nDaily Mission:§r§r"));
        assert playerSaver != null;
        player.sendMessage(Text.literal("§bTime Left: " + MissionsData.getDailyMissionTime(playerSaver) + "§r"));
        player.sendMessage(Text.literal("1. "));
        player.sendMessage(Text.literal("2. "));
        player.sendMessage(Text.literal("3. "));

        return 1;
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

    private static int runAddDailyMissions(CommandContext<ServerCommandSource> context, ItemStackArgument stack, int amountOfItemsNeeded) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        ServerTickHandler.itemsForDailyMission.add(stack.getItem().getDefaultStack());
        ServerTickHandler.amountOfItemsForDailyMission.add(amountOfItemsNeeded);

        return 1;
    }

    private static int runOuputDailyMissionsList(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();

        if(ServerTickHandler.itemsForDailyMission.size() <= 0){
            context.getSource().sendFeedback(Text.literal("§cNo Daily Mission yet!§r"), false);
        } else {
            context.getSource().sendFeedback(Text.literal("§a§nAvailable Daily Mission!§r§r"), false);
            for(int i = 0;i < ServerTickHandler.itemsForDailyMission.size();i++) {
                int i1 = i + 1;
                context.getSource().sendFeedback(Text.literal("§b" + i1 + ".§r " + ServerTickHandler.amountOfItemsForDailyMission.get(i) + "x §b"
                        + ServerTickHandler.itemsForDailyMission.get(i).getItem().getName().getString() + "§r"), false);
            }
        }
        return 1;
    }

}
