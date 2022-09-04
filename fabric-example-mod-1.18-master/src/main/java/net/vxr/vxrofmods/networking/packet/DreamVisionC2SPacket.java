package net.vxr.vxrofmods.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.vxr.vxrofmods.item.ModArmorMaterials;

public class DreamVisionC2SPacket {

    /* public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Everything here only happens on the Server
        boolean hasHelmetOn = !player.getInventory().getArmorStack(3).isEmpty();
        boolean hasCorrectHelmetOn = ((ArmorItem)player.getInventory().getArmorStack(3).getItem())
                .getMaterial() == ModArmorMaterials.Dream;
        boolean hasPlayerEffect = player.hasStatusEffect(StatusEffects.NIGHT_VISION);

        if(hasHelmetOn && hasCorrectHelmetOn && hasPlayerEffect) {

            player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        } else if(hasHelmetOn && hasCorrectHelmetOn && !hasPlayerEffect) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 2147400000, 1));
        }
    } */
}
