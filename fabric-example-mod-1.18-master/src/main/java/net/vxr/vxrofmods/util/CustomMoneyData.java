package net.vxr.vxrofmods.util;

import net.minecraft.nbt.NbtCompound;

public class CustomMoneyData {
    public static int addOrSubtractMoney(IEntityDataSaver player, int additionalMoney) {
        NbtCompound nbt = player.getPersistentData();
        int currentMoney = nbt.getInt("current_money");
        int newCurrentMoney = currentMoney + additionalMoney;

        nbt.putInt("current_money", newCurrentMoney);

        return newCurrentMoney;
    }

    public static int setMoney(IEntityDataSaver player, int newCurrentMoney) {
        NbtCompound nbt = player.getPersistentData();
        int currentMoney = nbt.getInt("current_money");
        nbt.putInt("current_money", newCurrentMoney);

        return newCurrentMoney;
    }

    public static int getMoney(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        return nbt.getInt("current_money");
    }

}
