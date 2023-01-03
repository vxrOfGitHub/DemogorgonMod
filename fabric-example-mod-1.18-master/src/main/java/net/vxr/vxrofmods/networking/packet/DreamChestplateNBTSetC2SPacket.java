package net.vxr.vxrofmods.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vxr.vxrofmods.util.DreamJetpackData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

public class DreamChestplateNBTSetC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Everything here only happens on the Server
        ItemStack stack = player.getInventory().getArmorStack(2);
        if(!stack.hasNbt()) {
            stack.setNbt(new NbtCompound());
        }
        stack.getNbt().putBoolean("dream_jetpack_on", DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)));
    }
}
