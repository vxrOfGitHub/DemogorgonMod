package net.vxrofmods.demogorgonmod.networking.C2SPackets;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;

public class DemogorgonAnimationStateUpdateC2SPacket implements Packet {

    private int animationState;

    public DemogorgonAnimationStateUpdateC2SPacket() {
        // Default constructor is required for packet serialization
    }

    public DemogorgonAnimationStateUpdateC2SPacket(int animationState) {
        this.animationState = animationState;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(animationState);
    }

    public void read(PacketByteBuf buf) {
        animationState = buf.readInt();
    }

    @Override
    public void apply(PacketListener listener) {
        // Handle the packet on the server side
        if (listener instanceof ServerPlayPacketListener) {
            handlePacket((ServerPlayPacketListener) listener);
        }
    }

    public void handlePacket(ServerPlayPacketListener listener) {
        // Access the animation state sent by the client
        // Update the server-side logic based on the animation state
    }

}
