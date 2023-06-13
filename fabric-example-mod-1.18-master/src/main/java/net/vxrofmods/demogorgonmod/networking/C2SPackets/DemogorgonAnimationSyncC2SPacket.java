package net.vxrofmods.demogorgonmod.networking.C2SPackets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonEntity;

public class DemogorgonAnimationSyncC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {

        ServerWorld world = player.getServerWorld();

        Entity entity = world.getEntityById(buf.readInt());

        if(entity instanceof DemogorgonEntity demogorgon) {
            demogorgon.setAnimation(buf.readInt());
        }
    }
}
