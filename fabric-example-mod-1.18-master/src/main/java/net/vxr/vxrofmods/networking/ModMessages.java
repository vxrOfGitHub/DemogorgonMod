package net.vxr.vxrofmods.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.networking.packet.*;

public class ModMessages {
    public static final Identifier DREAM_BOOST_ID = new Identifier(WW2Mod.MOD_ID, "dream_boost");
    public static final Identifier DREAM_VISION_ID = new Identifier(WW2Mod.MOD_ID, "dream_vision");
    public static final Identifier DREAM_JETPACK_ID = new Identifier(WW2Mod.MOD_ID, "dream_jetpack");
    //public static final Identifier DREAM_JETPACK_UP_ID = new Identifier(WW2Mod.MOD_ID, "dream_jetpack_up");
    public static final Identifier FLY_UPWARD_ID = new Identifier(WW2Mod.MOD_ID, "fly_upward");
    public static final Identifier FLY_DOWNWARD_ID = new Identifier(WW2Mod.MOD_ID, "fly_downward");
    public static final Identifier PLAY_FIRE_EXTINCTION_SOUND_ID = new Identifier(WW2Mod.MOD_ID, "play_fire_extinction_sound");
    //public static final Identifier TEST_FOR_FLYING_ID = new Identifier(WW2Mod.MOD_ID, "fly_test");
    public static final Identifier DREAM_CHESTPLATE_NBT_SET_ID = new Identifier(WW2Mod.MOD_ID, "dream_chestplate_nbt_set");


    //public static final Identifier ITEM_SYNC = new Identifier(WW2Mod.MOD_ID, "item_sync");
    public static final Identifier DREAM_JETPACK_PARTICLE_SPAWN = new Identifier(WW2Mod.MOD_ID, "dream_jetpack_particle_spawn");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(DREAM_VISION_ID, DreamVisionC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DREAM_BOOST_ID, DreamBoostC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DREAM_JETPACK_ID, DreamJetpackC2SPacket::receive);
        //ServerPlayNetworking.registerGlobalReceiver(DREAM_JETPACK_UP_ID, DreamJetpackUpC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(FLY_UPWARD_ID, PlayerFlyUpwardC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(FLY_DOWNWARD_ID, PlayerFlyDownwardC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DREAM_CHESTPLATE_NBT_SET_ID, DreamChestplateNBTSetC2SPacket::receive);

    }

    public static void registerS2CPackets() {
        //ClientPlayNetworking.registerGlobalReceiver(ITEM_SYNC, ItemStackSyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DREAM_JETPACK_PARTICLE_SPAWN, DreamJetpackParticleSpawnS2CPacket::receive);
        //ClientPlayNetworking.registerGlobalReceiver(TEST_FOR_FLYING_ID, TestForFlyingS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(PLAY_FIRE_EXTINCTION_SOUND_ID, PlayFireExtinctionSoundS2CPacket::receive);
    }

}
