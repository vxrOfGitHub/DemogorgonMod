package net.vxr.vxrofmods.networking.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.vxr.vxrofmods.networking.ModMessages;

public class TestForFlyingS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        System.out.println("In S2C Packet:");
        if(client.player != null) {
            System.out.println("Player is not null!");
            boolean jumpKeyPressed = client.player.input.jumping;
            if(jumpKeyPressed) {
                System.out.println("This Player pressed the JumpKey");
                ClientPlayNetworking.send(ModMessages.FLY_UPWARD_ID, PacketByteBufs.create());
                //player.addStatusEffect(new StatusEffectInstance(((ServerPlayerEntity) player).upwardSpeed = 1000))
            }
            if(client.player.input.sneaking) {
                ClientPlayNetworking.send(ModMessages.FLY_DOWNWARD_ID, PacketByteBufs.create());
            }
        }

    }
}
