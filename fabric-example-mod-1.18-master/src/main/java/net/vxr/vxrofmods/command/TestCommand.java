package net.vxr.vxrofmods.command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.vxr.vxrofmods.util.CustomMoneyData;
import net.vxr.vxrofmods.util.IEntityDataSaver;
import net.vxr.vxrofmods.util.MissionsData;

import java.util.Collection;

public class TestCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {


        serverCommandSourceCommandDispatcher.register(CommandManager.literal("testing")
                        .then(CommandManager.literal("set").requires(source -> source.hasPermissionLevel(2))
                        .then((CommandManager.argument("amount", IntegerArgumentType.integer()))
                        .executes((context) -> runSetTest(context, IntegerArgumentType.getInteger(context, "amount")))))
                .then(CommandManager.literal("get")
                        .executes(context -> runGetTest(context))));



    }
    private static int runSetTest(CommandContext<ServerCommandSource> context , int setTestNumber) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        assert playerSaver != null;
        MissionsData.setDailyMission1(playerSaver, setTestNumber);

        player.sendMessage(Text.literal("SetDailyMission1: " + setTestNumber));

        return 1;
    }

    private static int runGetTest(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        IEntityDataSaver playerSaver = (IEntityDataSaver)context.getSource().getPlayer();
        PlayerEntity player = context.getSource().getPlayer();

        player.sendMessage(Text.literal("GetDailyMission1: " + MissionsData.getDailyMission1(playerSaver)));

        return 1;
    }
}
