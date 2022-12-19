package net.vxr.vxrofmods.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.item.custom.SpawnChangerItem;
import net.vxr.vxrofmods.util.CustomMoneyData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

import java.util.Collection;
import java.util.Objects;

public class SpawnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {


        serverCommandSourceCommandDispatcher.register(CommandManager.literal("spawn_changer")
                .then(CommandManager.literal("buy")
                        .then(CommandManager.argument("target", IntegerArgumentType.integer())
                                .executes(context -> runBuySpawnChangerType(context, IntegerArgumentType.getInteger(context, "target")))))
                .then(CommandManager.literal("list")
                        .executes(SpawnCommand::runSpawnChangerTypesList)
                        .then(CommandManager.literal("add").requires(source -> source.hasPermissionLevel(2))
                                .then(CommandManager.argument("entity", EntityArgumentType.entity())
                                        .executes(context -> runAddEntityType(context, EntityArgumentType.getEntity(context, "entity")))))));



    }

    private static int runAddEntityType(CommandContext<ServerCommandSource> context, Entity entity) throws CommandSyntaxException {
        SpawnChangerItem.addPossibleEntityTypes(entity.getType());
        context.getSource().sendFeedback(Text.literal(Objects.requireNonNull(context.getSource().getPlayer()).getName().getString() + " added " + entity.getType().getName().getString() + " to the Spawn Changer List"),true);
        return 1;
    }

    private static int runSpawnChangerTypesList(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(Text.literal("It costs §6§l1000 Coins§r§r to convert your Spawn Changer to following:"), false);
        for(int i = 0; i < SpawnChangerItem.possibleEntityTypes.size(); i++) {
            int x = i + 1;
            context.getSource().sendFeedback(Text.literal(x + " = " + SpawnChangerItem.possibleEntityTypes.get(i).getName().getString()), false);
        }
        return 1;
    }

    private static int runBuySpawnChangerType(CommandContext<ServerCommandSource> context, int target) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayer();
        IEntityDataSaver playerSaver = ((IEntityDataSaver) player);
        
        assert player != null;
        if(CustomMoneyData.getMoney(playerSaver) >= 1000) {
            if(target > SpawnChangerItem.possibleEntityTypes.size() || target < 1) {
                context.getSource().sendFeedback(Text.literal("§cThe given Number is bigger or smaller than the List!§r"), false);
            } else {
                target--;
                ItemStack stack = player.getStackInHand(player.getActiveHand());
                if(!stack.isEmpty() && stack.getItem().equals(ModItems.SPAWN_CHANGER)) {
                    SpawnChangerItem.setEntityTypeOfSpawnChanger(stack, target);
                    EntityType<?> entityType = SpawnChangerItem.possibleEntityTypes.get(target);
                    CustomMoneyData.addOrSubtractMoney(playerSaver, -1000);
                    context.getSource().sendFeedback(Text.literal("Your Spawn Changer now converts to " + entityType.getName().getString()), false);
                } else {
                    context.getSource().sendFeedback(Text.literal("§cYour are not holding a Spawn Changer in your Hand!§r"), false);
                }
            }
        } else {
            context.getSource().sendFeedback(Text.literal("§cNot enough Money to do that!§r"), false);
        }


        return 1;

    }
}