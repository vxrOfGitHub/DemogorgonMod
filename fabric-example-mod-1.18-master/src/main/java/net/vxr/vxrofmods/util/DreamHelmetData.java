package net.vxr.vxrofmods.util;

import net.minecraft.nbt.NbtCompound;

public class DreamHelmetData {

    public static void setHadHelmetOn(IEntityDataSaver player, boolean hadHelmetOn) {
        NbtCompound nbt = player.getPersistentData();

        nbt.putBoolean("dream_helmet_had_helmet_on", hadHelmetOn);
    }

    public static boolean hadHelmetOn(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();
        return nbt.getBoolean("dream_helmet_had_helmet_on");
    }

}
