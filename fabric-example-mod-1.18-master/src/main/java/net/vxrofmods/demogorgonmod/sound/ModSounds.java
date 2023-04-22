package net.vxrofmods.demogorgonmod.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;

public class ModSounds {

    // Sound Identifiers
    public static final Identifier DEMOGORGON_HURT_ID = new Identifier(DemogorgonMod.MOD_ID + ":demogorgon_hurt");
    public static final Identifier DEMOGORGON_IDLE_ID = new Identifier(DemogorgonMod.MOD_ID + ":demogorgon_idle");
    public static final Identifier DEMOGORGON_DEATH_ID = new Identifier(DemogorgonMod.MOD_ID + ":demogorgon_death");

    // Sound Events
    public static SoundEvent DEMOGORGON_IDLE = SoundEvent.of(DEMOGORGON_IDLE_ID);
    public static SoundEvent DEMOGORGON_DEATH = SoundEvent.of(DEMOGORGON_DEATH_ID);
    public static SoundEvent DEMOGORGON_HURT_SOUND = SoundEvent.of(DEMOGORGON_HURT_ID);


    public static void registerSounds() {
        System.out.println("Registering ModSounds for " + DemogorgonMod.MOD_ID);
        Registry.register(Registries.SOUND_EVENT, DEMOGORGON_HURT_ID, DEMOGORGON_HURT_SOUND);
        Registry.register(Registries.SOUND_EVENT, DEMOGORGON_IDLE_ID, DEMOGORGON_IDLE);
        Registry.register(Registries.SOUND_EVENT, DEMOGORGON_DEATH_ID, DEMOGORGON_DEATH);
    }

}
