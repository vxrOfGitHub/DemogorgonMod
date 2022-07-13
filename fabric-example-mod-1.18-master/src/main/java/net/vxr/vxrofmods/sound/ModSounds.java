package net.vxr.vxrofmods.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.vxr.vxrofmods.WW2Mod;

public class ModSounds {
    public static SoundEvent DEMOGORGON_IDLE = registerSoundEvent("demogorgon_idle");
    public static SoundEvent DEMOGORGON_HURT = registerSoundEvent("demogorgon_hurt");
    public static SoundEvent DEMOGORGON_DEATH = registerSoundEvent("demogorgon_death");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(WW2Mod.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static void registerSounds() {System.out.println("Registering ModSounds for " + WW2Mod.MOD_ID);}

}
