package net.vxr.vxrofmods.command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.mixin.ModEntityDataSaverMixin;
import net.vxr.vxrofmods.util.CustomMoneyData;
import net.vxr.vxrofmods.util.IEntityDataSaver;
import net.vxr.vxrofmods.util.InventoryUtil;

import java.util.Collection;
import java.util.Objects;

public class WithdrawMoneyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {


        serverCommandSourceCommandDispatcher.register(CommandManager.literal("money")
                        .then(CommandManager.literal("withdraw")
                        .then((CommandManager.argument("amount", IntegerArgumentType.integer()))
                        .executes((context) -> runWithdrawMoney(context, IntegerArgumentType.getInteger(context, "amount"))))));



    }
    private static int runWithdrawMoney(CommandContext<ServerCommandSource> context, int withdrawAmount) throws CommandSyntaxException {

        IEntityDataSaver player = (IEntityDataSaver)context.getSource().getPlayer();
        int emptySlot = Objects.requireNonNull(context.getSource().getPlayer()).getInventory().getEmptySlot();
        boolean hasSpaceInInventory = emptySlot > -1;

        if(CustomMoneyData.getMoney(player) >= withdrawAmount && withdrawAmount > 0 && hasSpaceInInventory) {
            CustomMoneyData.addOrSubtractMoney(player, Math.negateExact(withdrawAmount));

            giveCoinWithNbtToCoin(context.getSource().getPlayer(), withdrawAmount);

            context.getSource().sendFeedback(Text.literal("You withdrawed §6§l" + withdrawAmount + " Coins §r§r"), false);
            context.getSource().sendFeedback(
                    Text.literal("You now have " + "§6§l" + CustomMoneyData.getMoney(player) + " Coins" + "§r§r"), false);

        } else if(CustomMoneyData.getMoney(player) < withdrawAmount) {
            context.getSource().sendFeedback(Text.literal("§c§lNot enough Money!§r§r"), false);
        } else if(!hasSpaceInInventory) {
            context.getSource().sendFeedback(Text.literal("§c§lNot enough Space in Inventory!§r§r"), false);
        }

        return 1;
    }

    private static void giveCoinWithNbtToCoin(PlayerEntity player, int coinValue) {
        ItemStack COIN = new ItemStack(ModItems.COIN);
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putInt("vxrofmods.coin_value", coinValue);
        COIN.setNbt(nbtCompound);
        player.giveItemStack(COIN);
    }

}
