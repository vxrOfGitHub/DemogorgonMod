package net.vxrofmods.demogorgonmod.networking.S2CPackets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.vxrofmods.demogorgonmod.util.DemogorgonData;
import net.vxrofmods.demogorgonmod.util.IEntityDataSaver;

public class getDDTargetInNbtSyncS2CPacket {

    public static void receive(MinecraftClient minecraftClient, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {
        if(minecraftClient.world != null) {
            Entity entity = minecraftClient.world.getEntityById(buf.readInt());
            if(entity instanceof LivingEntity target) {
                DemogorgonData.writeTargetToDDTargetNBT(((IEntityDataSaver) target),
                        buf.readBoolean());
            }
        }
    }
}
