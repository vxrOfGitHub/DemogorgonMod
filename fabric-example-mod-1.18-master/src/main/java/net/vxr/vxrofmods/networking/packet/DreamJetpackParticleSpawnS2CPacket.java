package net.vxr.vxrofmods.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;

public class DreamJetpackParticleSpawnS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {

        /*BlockPos position = buf.readBlockPos();

        assert client.world != null;
        client.world.addParticle(ParticleTypes.LARGE_SMOKE,
                position.getX(), position.getY(), position.getZ(),
                0.0d, -1.0d, 0.0d); */
    }
}
