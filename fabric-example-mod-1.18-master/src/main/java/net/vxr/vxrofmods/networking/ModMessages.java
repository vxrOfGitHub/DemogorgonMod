package net.vxr.vxrofmods.networking;

import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.vxr.vxrofmods.networking.packet.DreamBoostC2SPacket;
import net.vxr.vxrofmods.networking.packet.DreamVisionC2SPacket;
import net.vxr.vxrofmods.networking.packet.ExampleC2SPacket;

public class ModMessages {
    public static final Identifier DREAM_BOOST_ID = new Identifier(WW2Mod.MOD_ID, "dream_boost");
    public static final Identifier DREAM_VISION_ID = new Identifier(WW2Mod.MOD_ID, "dream_vision");
    public static final Identifier EXAMPLE_ID = new Identifier(WW2Mod.MOD_ID, "example");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleC2SPacket::receive);
        //ServerPlayNetworking.registerGlobalReceiver(DREAM_VISION_ID, DreamVisionC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DREAM_BOOST_ID, DreamBoostC2SPacket::receive);
    }

    public static void registerS2CPackets() {

    }

}
