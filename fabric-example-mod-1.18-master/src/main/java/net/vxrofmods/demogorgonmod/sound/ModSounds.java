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
    public static final Identifier DEMOGORGON_SCREAM_1_ID = new Identifier(DemogorgonMod.MOD_ID + ":demogorgon_scream_1");
    public static final Identifier DEMOGORGON_DD_ATTACK_1_ID = new Identifier(DemogorgonMod.MOD_ID + ":demogorgon_dd_attack_1");

    // Sound Events
    public static SoundEvent DEMOGORGON_IDLE = SoundEvent.of(DEMOGORGON_IDLE_ID);
    public static SoundEvent DEMOGORGON_DEATH = SoundEvent.of(DEMOGORGON_DEATH_ID);
    public static SoundEvent DEMOGORGON_HURT_SOUND = SoundEvent.of(DEMOGORGON_HURT_ID);
    public static SoundEvent DEMOGORGON_SCREAM_1_SOUND = SoundEvent.of(DEMOGORGON_SCREAM_1_ID);
    public static SoundEvent DEMOGORGON_DD_ATTACK_1_SOUND = SoundEvent.of(DEMOGORGON_DD_ATTACK_1_ID);


    public static void registerSounds() {
        System.out.println("Registering ModSounds for " + DemogorgonMod.MOD_ID);
        Registry.register(Registries.SOUND_EVENT, DEMOGORGON_HURT_ID, DEMOGORGON_HURT_SOUND);
        Registry.register(Registries.SOUND_EVENT, DEMOGORGON_IDLE_ID, DEMOGORGON_IDLE);
        Registry.register(Registries.SOUND_EVENT, DEMOGORGON_DEATH_ID, DEMOGORGON_DEATH);
        Registry.register(Registries.SOUND_EVENT, DEMOGORGON_SCREAM_1_ID, DEMOGORGON_SCREAM_1_SOUND);
        Registry.register(Registries.SOUND_EVENT, DEMOGORGON_DD_ATTACK_1_ID, DEMOGORGON_DD_ATTACK_1_SOUND);
    }

}
