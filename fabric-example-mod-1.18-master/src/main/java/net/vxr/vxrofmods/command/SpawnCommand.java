package net.vxr.vxrofmods.command;

import com.mojang.brigadier.CommandDispatcher;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.item.custom.SpawnChangerItem;
import net.vxr.vxrofmods.util.CustomMoneyData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

import java.util.Collection;

public class SpawnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {


        serverCommandSourceCommandDispatcher.register(CommandManager.literal("spawn_changer")
                .then(CommandManager.literal("buy")
                        .then(CommandManager.argument("target", EntityArgumentType.entity())
                                .executes(context -> runBuySpawnChangerType(context, EntityArgumentType.getEntity(context, "target"))))));



    }

    private static int runBuySpawnChangerType(CommandContext<ServerCommandSource> context, Entity target) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayer();
        IEntityDataSaver playerSaver = ((IEntityDataSaver) player);

        assert player != null;
        if(CustomMoneyData.getMoney(playerSaver) >= 1000) {
            ItemStack stack = player.getStackInHand(player.getActiveHand());
            if(!stack.isEmpty() && stack.getItem().equals(ModItems.SPAWN_CHANGER)) {
                SpawnChangerItem spawnChangerItem = ((SpawnChangerItem) stack.getItem());
                spawnChangerItem.setEntityTypeOfSpawnChanger((EntityType<? extends MobEntity>) target.getType());
                CustomMoneyData.addOrSubtractMoney(playerSaver, -1000);
                context.getSource().sendFeedback(Text.literal("Your Spawn Changer now converts to " + target.getType().getName().getString()), false);
            } else {
                context.getSource().sendFeedback(Text.literal("§cYour are not holding a Spawn Changer in your Hand!§r"), false);
            }
        } else {
            context.getSource().sendFeedback(Text.literal("§cNot enough Money to do that!§r"), false);
        }


        return 1;

    }
}
