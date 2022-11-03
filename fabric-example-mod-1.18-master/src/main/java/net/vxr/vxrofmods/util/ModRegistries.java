package net.vxr.vxrofmods.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.vxr.vxrofmods.command.*;
import net.vxr.vxrofmods.entity.custom.DemogorgonEntity;
import net.vxr.vxrofmods.entity.ModEntities;
import net.vxr.vxrofmods.entity.custom.vxrPenguinAvatarEntity;
import net.vxr.vxrofmods.event.ModPlayerEventCopyFrom;

public class ModRegistries {
    public static void registerModStuffs() {
        registerAttributes();
        registerCommands();
        registerEvents();
    }
    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.DEMOGORGON, DemogorgonEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.PENGUIN_AVATAR, vxrPenguinAvatarEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.MORITZ_DRAGON, vxrPenguinAvatarEntity.setAttributes());
    }


    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(GetMoneyCommand::register);
        CommandRegistrationCallback.EVENT.register(AddMoneyCommand::register);
        CommandRegistrationCallback.EVENT.register(SetMoneyCommand::register);
        CommandRegistrationCallback.EVENT.register(WithdrawMoneyCommand::register);
        CommandRegistrationCallback.EVENT.register(MissionsCommand::register);
        CommandRegistrationCallback.EVENT.register(TestCommand::register);

    }

    private static void registerEvents() {
        ServerPlayerEvents.COPY_FROM.register(new ModPlayerEventCopyFrom());
    }
}
