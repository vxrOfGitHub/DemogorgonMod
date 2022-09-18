package net.vxr.vxrofmods.item.custom;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

public class ModMusicDiskItem extends MusicDiscItem {

    public ModMusicDiskItem(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds) {
        super(comparatorOutput, sound, settings, lengthInSeconds);
    }
}
