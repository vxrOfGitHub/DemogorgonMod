package net.vxr.vxrofmods.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vxr.vxrofmods.item.ModArmorMaterials;
import net.vxr.vxrofmods.util.DreamJetpackData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

public class DreamJetpackUpC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Everything here only happens on the Server
        boolean hasChestplateOn = !player.getInventory().getArmorStack(2).isEmpty();
        boolean hasCorrectChestplateOn = ((ArmorItem)player.getInventory().getArmorStack(2).getItem())
                .getMaterial() == ModArmorMaterials.Dream;


        if(hasChestplateOn && hasCorrectChestplateOn) {
            DreamJetpackData.switchJetpackUp(((IEntityDataSaver) player));
            DreamJetpackData.setJetpackOnOff(((IEntityDataSaver) player), true);
            ItemStack helmetStack = player.getInventory().getArmorStack(2);
            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean("dream_jetpack_on", DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)));
            helmetStack.setNbt(nbt);
        }

    }

}
