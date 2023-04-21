package net.vxrofmods.demogorgonmod.networking.C2SPackets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class ExampleC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {

        //Everything here happens ONLY on the Server!
        //EntityType.PLAYER.spawn(player.getWorld().toServerWorld(), player.getBlockPos(), SpawnReason.TRIGGERED);

    }
}
