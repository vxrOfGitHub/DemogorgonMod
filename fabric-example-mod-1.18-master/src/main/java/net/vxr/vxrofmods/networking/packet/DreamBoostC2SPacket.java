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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.vxr.vxrofmods.item.ModArmorMaterials;

public class DreamBoostC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Everything here only happens on the Server
        boolean hasBootsOn = !player.getInventory().getArmorStack(0).isEmpty();
        boolean hasCorrectBootsOn = ((ArmorItem)player.getInventory().getArmorStack(0).getItem())
                .getMaterial() == ModArmorMaterials.Dream;
        ServerWorld world = player.getWorld();

        if(hasBootsOn && hasCorrectBootsOn && player.isOnGround()) {
            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS,
                    1F, world.random.nextFloat() * 0.1F - 0.4F);
            player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }
}
