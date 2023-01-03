package net.vxr.vxrofmods.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;

public class PlayFireExtinctionSoundS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {

        System.out.println("In Play Fire Extinction S2C Packet");
        assert client.player != null;
        client.player.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1f, 10f);
    }
}
