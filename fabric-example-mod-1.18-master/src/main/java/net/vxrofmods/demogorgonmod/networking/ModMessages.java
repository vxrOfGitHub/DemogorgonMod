package net.vxrofmods.demogorgonmod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.networking.C2SPackets.DemogorgonAnimationStateUpdateC2SPacket;
import net.vxrofmods.demogorgonmod.networking.C2SPackets.DemogorgonAnimationSyncC2SPacket;
import net.vxrofmods.demogorgonmod.networking.C2SPackets.DemogorgonHeadKeybindSyncC2SPacket;
import net.vxrofmods.demogorgonmod.networking.C2SPackets.ExampleC2SPacket;
import net.vxrofmods.demogorgonmod.networking.S2CPackets.DemogorgonHeadSetVisibilityOfNearMobsS2CPacket;
import net.vxrofmods.demogorgonmod.networking.S2CPackets.DemogorgonPlayAnimationSyncS2CPacket;
import net.vxrofmods.demogorgonmod.networking.S2CPackets.DemogorgonSpawnDDParticlesS2CPacket;
import net.vxrofmods.demogorgonmod.networking.S2CPackets.GetDDTargetInNbtSyncS2CPacket;

public class ModMessages {

    public static final Identifier DEMOGORGON_PLAY_ANIMATION_SYNC_ID = new Identifier(DemogorgonMod.MOD_ID, "demogorgon_play_animation_sync");
    public static final Identifier GET_DD_TARGET_IN_NBT_SYNC_ID = new Identifier(DemogorgonMod.MOD_ID, "get_dd_target_in_nbt_sync");
    public static final Identifier DEMOGORGON_SPAWN_DD_PARTICLES_ID = new Identifier(DemogorgonMod.MOD_ID, "demogorgon_spawn_dd_particles");

    public static final Identifier DEMOGORGON_ANIMATION_STATE_UPDATE = new Identifier(DemogorgonMod.MOD_ID, "demogorgon_animation_state_update");
    public static final Identifier DEMOGORGON_ANIMATION_SYNC_ID = new Identifier(DemogorgonMod.MOD_ID, "demogorgon_animation_sync");
    public static final Identifier DEMOGORGON_HEAD_KEYBIND_SYNC_ID = new Identifier(DemogorgonMod.MOD_ID, "demogorgon_head_keybind_sync_id");
    public static final Identifier DEMOGORGON_HEAD_SET_VISIBILITY_OF_NEAR_MOBS_ID = new Identifier(DemogorgonMod.MOD_ID, "demogorgon_head_set_visibility_of_near_mobs_id");
    public static final Identifier EXAMPLE_ID = new Identifier(DemogorgonMod.MOD_ID, "example");



    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(EXAMPLE_ID, ExampleC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DEMOGORGON_ANIMATION_SYNC_ID, DemogorgonAnimationSyncC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(DEMOGORGON_HEAD_KEYBIND_SYNC_ID, DemogorgonHeadKeybindSyncC2SPacket::receive);


        // Updating Demogorgon Animations
        ServerPlayNetworking.registerGlobalReceiver(DEMOGORGON_ANIMATION_STATE_UPDATE, (server, player, handler, buf, responseSender) -> {
            DemogorgonAnimationStateUpdateC2SPacket packet = new DemogorgonAnimationStateUpdateC2SPacket();
            packet.read(buf);
            packet.handlePacket(handler);
        });
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DEMOGORGON_PLAY_ANIMATION_SYNC_ID, DemogorgonPlayAnimationSyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DEMOGORGON_SPAWN_DD_PARTICLES_ID, DemogorgonSpawnDDParticlesS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(GET_DD_TARGET_IN_NBT_SYNC_ID, GetDDTargetInNbtSyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(DEMOGORGON_HEAD_SET_VISIBILITY_OF_NEAR_MOBS_ID, DemogorgonHeadSetVisibilityOfNearMobsS2CPacket::receive);
    }
}

