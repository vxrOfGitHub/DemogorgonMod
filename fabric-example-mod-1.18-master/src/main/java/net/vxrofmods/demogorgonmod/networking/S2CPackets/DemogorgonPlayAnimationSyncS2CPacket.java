package net.vxrofmods.demogorgonmod.networking.S2CPackets;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonEntity;

public class DemogorgonPlayAnimationSyncS2CPacket {
    public static void receive(MinecraftClient minecraftClient, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {
        System.out.println("In Sync Packet");
        if(minecraftClient.world != null) {
            Entity entity = minecraftClient.world.getEntityById(buf.readInt());
            if(entity instanceof DemogorgonEntity demogorgon) {
                NbtCompound nbt = new NbtCompound();
                demogorgon.readCustomDataFromNbt(nbt);
                nbt.putBoolean("demogorgon.dimension_drift.play_submerge_animation", buf.readBoolean());
                System.out.println("Should Play Animation? " + nbt.getBoolean("demogorgon.dimension_drift.play_submerge_animation"));
                demogorgon.writeCustomDataToNbt(nbt);
            }
        }
        else {
            System.out.println("Packet World = Null");
        }
    }
}
