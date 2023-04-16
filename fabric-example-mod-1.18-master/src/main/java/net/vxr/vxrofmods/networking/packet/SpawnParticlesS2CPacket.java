package net.vxr.vxrofmods.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class SpawnParticlesS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {

        /*System.out.println("In Spawn Particles S2C Packet");
        BlockPos pos = buf.readBlockPos();
        int particleCount = buf.readInt();
        double radius = buf.readDouble();
        double centerX = buf.readDouble();
        if(client.world != null) {
            double increment = (2 * Math.PI) / particleCount;
            for (int i = 0; i < particleCount; i++) {
                double angle = i * increment;
                double x = centerX + (radius * Math.cos(angle));
                double z = centerZ + (radius * Math.sin(angle));
                client.world.addParticle(particle, x, centerY, z, 0, 0.1, 0);
            }
        }*/
    }
}
