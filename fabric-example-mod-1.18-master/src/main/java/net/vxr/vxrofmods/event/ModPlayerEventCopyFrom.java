package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.util.IEntityDataSaver;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public class ModPlayerEventCopyFrom implements ServerPlayerEvents.CopyFrom {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        IEntityDataSaver original = ((IEntityDataSaver) oldPlayer);
        IEntityDataSaver player = ((IEntityDataSaver) newPlayer);

        // Save Money when dying
        saveMoney(player, original, oldPlayer);

    }

    private void saveMoney(IEntityDataSaver player, IEntityDataSaver original, PlayerEntity oldPlayer) {

        int originalMoneyAmount = original.getPersistentData().getInt("current_money");
        int looseAmount = originalMoneyAmount / 10;
        int newMoneyAmount = originalMoneyAmount - looseAmount;
        int amountToDrop = looseAmount / 2;

        player.getPersistentData().putInt("current_money", newMoneyAmount);

        ItemStack moneyToDrop = new ItemStack(ModItems.COIN);
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putInt("vxrofmods.coin_value", amountToDrop);
        moneyToDrop.setNbt(nbtCompound);

        oldPlayer.dropStack(moneyToDrop);
    }
}
