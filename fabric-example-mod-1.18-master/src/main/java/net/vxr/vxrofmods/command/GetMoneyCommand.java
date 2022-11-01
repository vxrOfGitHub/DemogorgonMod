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

public class GetMoneyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("money")
                .executes(GetMoneyCommand::runShowMoney));

    }
    private static int runShowMoney(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver player = (IEntityDataSaver)context.getSource().getPlayer();

        assert player != null;
        if(CustomMoneyData.getMoney(player) != 0) {
            context.getSource().sendFeedback(
                    Text.literal("You have currently " + "§6§l" + CustomMoneyData.getMoney(player) + " Coins" + "§r§r"), false);
            return 1;
        } else {
            context.getSource().sendMessage
                    (Text.literal("You currently have §c§lnothing!§r§r"));
            return -1;
        }
    }
}
