package net.vxrofmods.demogorgonmod.networking.S2CPackets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class DemogorgonHeadSetVisibilityOfNearMobsS2CPacket {
    public static void receive(MinecraftClient minecraftClient, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {
        List<Entity> entities = new ArrayList<>();
        int amountOfNearEntities = buf.readInt();
        for (int i = 0; i < amountOfNearEntities; i++) {
            if(minecraftClient.world != null) {
                entities.add(minecraftClient.world.getEntityById(buf.readInt()));
            }
        }

        if(!entities.isEmpty()) {
            for (Entity entity : entities) {
                if(entity instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200));
                }
            }
        }

    }
}
