package net.vxrofmods.demogorgonmod.networking.S2CPackets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DustParticleEffect;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonEntity;
import net.vxrofmods.demogorgonmod.util.DemogorgonData;
import net.vxrofmods.demogorgonmod.util.IEntityDataSaver;
import org.joml.Vector3f;

public class DemogorgonSpawnDDParticlesS2CPacket {
    public static void receive(MinecraftClient minecraftClient, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {

        if(minecraftClient.world != null) {

            DustParticleEffect dustParticleEffect = new DustParticleEffect(new Vector3f(255,0,0), 1f);

            minecraftClient.world.addParticle(dustParticleEffect, buf.readDouble(), buf.readDouble(), buf.readDouble(),
                    buf.readDouble(), buf.readDouble(), buf.readDouble());
        }
    }
}
