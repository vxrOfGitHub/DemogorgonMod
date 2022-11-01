package net.vxr.vxrofmods.command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.vxr.vxrofmods.util.CustomMoneyData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

public class SetMoneyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("setmoney")
                .requires(source -> source.hasPermissionLevel(2))
                .then((CommandManager.argument("amount", IntegerArgumentType.integer()))
                        .executes((context) -> runSetMoney(context, IntegerArgumentType.getInteger(context, "amount")))));

    }
    private static int runSetMoney(CommandContext<ServerCommandSource> context, int newMoney) throws CommandSyntaxException {

        IEntityDataSaver player = (IEntityDataSaver)context.getSource().getPlayer();

        assert player != null;
        CustomMoneyData.setMoney(player, newMoney);

        context.getSource().sendFeedback(Text.literal(context.getSource().getPlayer().getName().getString() + " set " + "§6" + newMoney + " Coins" + "§l"), true);
        context.getSource().sendFeedback(Text.literal( "You have currently " + "§6" + CustomMoneyData.getMoney(player) + " Coins" + "§l"),
                true);

        return 1;
    }
}
