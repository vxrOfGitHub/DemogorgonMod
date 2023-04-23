package net.vxrofmods.demogorgonmod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.networking.C2SPackets.ExampleC2SPacket;
import net.vxrofmods.demogorgonmod.networking.S2CPackets.DemogorgonPlayAnimationSyncS2CPacket;
import net.vxrofmods.demogorgonmod.networking.S2CPackets.DemogorgonSpawnDDParticlesS2CPacket;
import net.vxrofmods.demogorgonmod.networking.S2CPackets.getDDTargetInNbtSyncS2CPacket;

public class ModMessages {

    public static final Identifier DEMOGORGON_PLAY_ANIMATION_SYNC_ID = new Identifier(DemogorgonMod.MOD_ID, "demogorgon_play_animation_sync");
    public static final Identifier GET_DD_TARGET_IN_NBT_SYNC_ID = new Identifier(DemogorgonMod.MOD_ID, "get_dd_target_in_nbt_sync");
    public static final Identifier DEMOGORGON_SPAWN_DD_PARTICLES_ID = new Identifier(DemogorgonMod.MOD_ID, "demogorgon_spawn_dd_particles");
    public static final Identifier EXAMPLE_ID = new Identifier(DemogorgonMod.MOD_ID, "example");


    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleC2SPacket::receive);

    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DEMOGORGON_PLAY_ANIMATION_SYNC_ID, DemogorgonPlayAnimationSyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DEMOGORGON_SPAWN_DD_PARTICLES_ID, DemogorgonSpawnDDParticlesS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(GET_DD_TARGET_IN_NBT_SYNC_ID, getDDTargetInNbtSyncS2CPacket::receive);
    }

}
