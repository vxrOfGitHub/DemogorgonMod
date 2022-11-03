package net.vxr.vxrofmods.command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.vxr.vxrofmods.util.CustomMoneyData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

import java.util.Collection;

public class AddMoneyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {


        serverCommandSourceCommandDispatcher.register(CommandManager.literal("money")
                        .then(CommandManager.literal("add").requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .then((CommandManager.argument("amount", IntegerArgumentType.integer()))
                        .executes((context) -> runAddMoney(context, EntityArgumentType.getPlayers(context, "targets"),
                                IntegerArgumentType.getInteger(context, "amount")))))));



    }
    private static int runAddMoney(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> targets , int additionalMoney) throws CommandSyntaxException {

        IEntityDataSaver player = (IEntityDataSaver)context.getSource().getPlayer();

        //IEntityDataSaver targetsSaver = (IEntityDataSaver) targets;


        assert player != null;
        for(int i = 0;i<targets.toArray().length;i++) {
                CustomMoneyData.addOrSubtractMoney(((IEntityDataSaver) targets.toArray()[i]), additionalMoney);
                context.getSource().sendFeedback(Text.literal(((ServerPlayerEntity) targets.toArray()[i]).getName().getString() + " added " + "§6§l" + additionalMoney +
                        " Coins" + "§r§r" + " to " + ((ServerPlayerEntity) targets.toArray()[i]).getName().getString()), true);
            if(CustomMoneyData.getMoney(((IEntityDataSaver) targets.toArray()[i])) == 0) {
                context.getSource().sendFeedback(Text.literal(((ServerPlayerEntity) targets.toArray()[i]).getName().getString() + " has now " + "§c§lnothing!§r§r"),
                        true);
            } else {
                context.getSource().sendFeedback(Text.literal(((ServerPlayerEntity) targets.toArray()[i]).getName().getString() + " has now " + "§6§l" + CustomMoneyData.getMoney(((IEntityDataSaver) targets.toArray()[i])) + " Coins" + "§r§r"),
                        true);
            }
        }

        return 1;
    }
}
