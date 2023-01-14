package net.vxr.vxrofmods.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.util.DreamJetpackData;
import net.vxr.vxrofmods.util.IEntityDataSaver;

public class DreamJetpackC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
                               PacketByteBuf buf, PacketSender responseSender) {
        // Everything here only happens on the Server
        boolean hasCorrectChestplateOn = player.getInventory().getArmorStack(2).getItem().equals(ModItems.Dream_Chestplate);

        if(hasCorrectChestplateOn) {
            DreamJetpackData.switchJetpackOnOff(((IEntityDataSaver) player));
            ItemStack chestplateStack = player.getInventory().getArmorStack(2);
            NbtCompound nbt = new NbtCompound();
            if(chestplateStack.hasNbt()) {
                nbt = chestplateStack.getNbt();
            }
            nbt.putBoolean("dream_jetpack_on", DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)));
            DreamJetpackData.setHadJetpackOn(((IEntityDataSaver) player), DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player)));

            chestplateStack.setNbt(nbt);
            /*if(DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player))) {
                DreamJetpackData.setEarlierForwardSpeed(((IEntityDataSaver) player), player.speed);
                    player.speed = player.speed + 1000f;
            }*/
            if(!DreamJetpackData.getJetpackOnOff(((IEntityDataSaver) player))) {
                player.setNoGravity(false);
                //player.speed = DreamJetpackData.getEarlierForwardSpeed(((IEntityDataSaver) player));
            }
        }

    }

}
