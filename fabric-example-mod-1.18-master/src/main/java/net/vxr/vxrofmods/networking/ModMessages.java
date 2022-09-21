package net.vxr.vxrofmods.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.vxr.vxrofmods.networking.packet.*;

public class ModMessages {
    public static final Identifier DREAM_BOOST_ID = new Identifier(WW2Mod.MOD_ID, "dream_boost");
    public static final Identifier DREAM_VISION_ID = new Identifier(WW2Mod.MOD_ID, "dream_vision");
    public static final Identifier DREAM_JETPACK_ID = new Identifier(WW2Mod.MOD_ID, "dream_jetpack");
    public static final Identifier DREAM_JETPACK_UP_ID = new Identifier(WW2Mod.MOD_ID, "dream_jetpack_up");


    public static final Identifier ITEM_SYNC = new Identifier(WW2Mod.MOD_ID, "item_sync");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(DREAM_VISION_ID, DreamVisionC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DREAM_BOOST_ID, DreamBoostC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DREAM_JETPACK_ID, DreamJetpackC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DREAM_JETPACK_UP_ID, DreamJetpackUpC2SPacket::receive);

    }

    public static void registerS2CPackets() {
        //ClientPlayNetworking.registerGlobalReceiver(ITEM_SYNC, ItemStackSyncS2CPacket::receive);
    }

}
