package net.vxr.vxrofmods.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vxr.vxrofmods.util.DreamJetpackData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

public class PlayerFlyDownwardC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Everything here only happens on the Server
        //float airSpeed = player.airStrafingSpeed;
        if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player))) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 2, -10, true, false));
            //player.updateVelocity(10, new Vec3d(player.getX(), player.getY() + 1000, player.getZ()));
        }
    }
}
