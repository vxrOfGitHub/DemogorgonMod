package net.vxr.vxrofmods.util;

import net.minecraft.nbt.NbtCompound;

public class DreamBoostCooldownData {
    public static int resetCooldown(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        int dream_boost_cooldown = nbt.getInt("dream_boost_cooldown");

        nbt.putInt("dream_boost_cooldown", 400);

        return dream_boost_cooldown;
    }

    public static int removeCooldownTick(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        int dream_boost_cooldown = nbt.getInt("dream_boost_cooldown");
        if(dream_boost_cooldown <= 0) {
            dream_boost_cooldown = 0;
        } else {
            dream_boost_cooldown--;
        }

        nbt.putInt("dream_boost_cooldown", dream_boost_cooldown);

        return dream_boost_cooldown;
    }

    public static int getCooldown(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getInt("dream_boost_cooldown");
    }

}
