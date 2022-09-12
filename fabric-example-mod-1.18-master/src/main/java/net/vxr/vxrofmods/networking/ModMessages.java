package net.vxr.vxrofmods.networking;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.vxr.vxrofmods.networking.packet.DreamBoostC2SPacket;
import net.vxr.vxrofmods.networking.packet.DreamJetpackC2SPacket;
import net.vxr.vxrofmods.networking.packet.DreamJetpackUpC2SPacket;
import net.vxr.vxrofmods.networking.packet.DreamVisionC2SPacket;

public class ModMessages {
    public static final Identifier DREAM_BOOST_ID = new Identifier(WW2Mod.MOD_ID, "dream_boost");
    public static final Identifier DREAM_VISION_ID = new Identifier(WW2Mod.MOD_ID, "dream_vision");
    public static final Identifier DREAM_JETPACK_ID = new Identifier(WW2Mod.MOD_ID, "dream_jetpack");
    public static final Identifier DREAM_JETPACK_UP_ID = new Identifier(WW2Mod.MOD_ID, "dream_jetpack_up");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(DREAM_VISION_ID, DreamVisionC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DREAM_BOOST_ID, DreamBoostC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DREAM_JETPACK_ID, DreamJetpackC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DREAM_JETPACK_UP_ID, DreamJetpackUpC2SPacket::receive);
    }

    public static void registerS2CPackets() {

    }

}
