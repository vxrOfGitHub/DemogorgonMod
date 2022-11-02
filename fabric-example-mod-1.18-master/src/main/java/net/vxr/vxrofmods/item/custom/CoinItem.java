package net.vxr.vxrofmods.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.vxr.vxrofmods.util.CustomMoneyData;
import net.vxr.vxrofmods.util.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoinItem extends Item {

    public CoinItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasNbt();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasNbt()) {
            tooltip.add(Text.literal("Value: §6§l" + stack.getNbt().getInt("vxrofmods.coin_value") + " Coin(s)§r§r"));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemstack = user.getStackInHand(hand);

        if(itemstack.hasNbt()) {
            int coinValue = itemstack.getNbt().getInt("vxrofmods.coin_value");
            CustomMoneyData.addOrSubtractMoney(((IEntityDataSaver) user), coinValue);
            itemstack.decrement(1);
            if(i == 1) {
                user.sendMessage(Text.literal("You deposited §6§l" + coinValue + " Coins §r§r"), false);
                user.sendMessage(
                        Text.literal("You now have " + "§6§l" + CustomMoneyData.getMoney(((IEntityDataSaver) user)) + " Coins" + "§r§r"), false);
                i = 0;
            } else {
                i++;
            }
        }
        return super.use(world, user, hand);
    }

    private int i;

}
