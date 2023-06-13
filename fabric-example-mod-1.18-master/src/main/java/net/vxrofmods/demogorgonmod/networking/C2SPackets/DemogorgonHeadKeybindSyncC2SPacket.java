package net.vxrofmods.demogorgonmod.networking.C2SPackets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.vxrofmods.demogorgonmod.entity.ModEntities;
import net.vxrofmods.demogorgonmod.entity.custom.FriendlyDemoDogEntity;
import net.vxrofmods.demogorgonmod.item.ModItems;
import net.vxrofmods.demogorgonmod.item.custom.DemogorgonArmorItem;
import net.vxrofmods.demogorgonmod.util.DemogorgonData;
import net.vxrofmods.demogorgonmod.util.IEntityDataSaver;

public class DemogorgonHeadKeybindSyncC2SPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender packetSender) {

        World world = player.getWorld();

        if(DemogorgonData.getDemogorgonHeadUserState(((IEntityDataSaver) player)) == 0
        && player.getInventory().getArmorStack(3).isOf(ModItems.DEMOGORGON_HEAD_HELMET)) {

            DemogorgonData.setDemogorgonHeadUserState(((IEntityDataSaver) player), 1);
            //ItemStack headItem = player.getInventory().getArmorStack(3);
            FriendlyDemoDogEntity demoDog = new FriendlyDemoDogEntity(ModEntities.FRIENDLY_DEMO_DOG, world);
            demoDog.setOwner(player);
            demoDog.setTamed(true);
            demoDog.setBaby(false);
            //DemogorgonArmorItem.writeDogIDToNbt(headItem, demoDog.getId());
            DemogorgonData.writeDogsIDToOwner(((IEntityDataSaver) player), demoDog.getId());
            if(!DemogorgonData.readDogNameFromNbt(((IEntityDataSaver) player)).equals("")) {
                demoDog.setCustomName(Text.literal(DemogorgonData.readDogNameFromNbt(((IEntityDataSaver) player))));
            }
            demoDog.setPosition(player.getPos());
            world.spawnEntity(demoDog);
        }
        else if (DemogorgonData.getDemogorgonHeadUserState(((IEntityDataSaver) player)) == 1
                && player.getInventory().getArmorStack(3).isOf(ModItems.DEMOGORGON_HEAD_HELMET)) {

            //ItemStack headItem = player.getInventory().getArmorStack(3);
            int id = DemogorgonData.readDogsIDFromOwner(((IEntityDataSaver) player));
            if(id != 0) {
                Entity entity = world.getEntityById(id);
                if(entity != null) {
                    DemogorgonData.writeDogNameToNbt(((IEntityDataSaver) player), "");
                    if(entity.hasCustomName()) {
                        //DemogorgonArmorItem.writeDogNameToNbt(headItem, entity.getCustomName().getString());
                        DemogorgonData.writeDogNameToNbt(((IEntityDataSaver) player), entity.getCustomName().getString());
                    }
                    entity.kill();
                    //DemogorgonArmorItem.writeDogIDToNbt(headItem, 0);
                    DemogorgonData.writeDogsIDToOwner(((IEntityDataSaver) player), 0);
                }
            }
            DemogorgonData.setDemogorgonHeadUserState(((IEntityDataSaver) player), 2);
        }
        else if(DemogorgonData.getDemogorgonHeadUserState(((IEntityDataSaver) player)) == 2
                && player.getInventory().getArmorStack(3).isOf(ModItems.DEMOGORGON_HEAD_HELMET)){
            int cooldown = (DemogorgonData.maxDogSpawnCooldownForHelmet - DemogorgonData.getDogSpawnCooldownForHelmet(((IEntityDataSaver) player))) / 20;
            player.sendMessage(Text.literal("Ability on Cooldown: " + cooldown), true);
        }
    }
}
