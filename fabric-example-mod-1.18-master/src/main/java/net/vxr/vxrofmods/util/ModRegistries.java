package net.vxr.vxrofmods.util;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.vxr.vxrofmods.entity.custom.DemogorgonEntity;
import net.vxr.vxrofmods.entity.ModEntities;
import net.vxr.vxrofmods.entity.custom.vxrPenguinAvatarEntity;

public class ModRegistries {
    public static void registerModStuffs() {
        registerAttributes();
    }
    private static void registerAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntities.DEMOGORGON, DemogorgonEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(ModEntities.PENGUIN_AVATAR, vxrPenguinAvatarEntity.setAttributes());
    }
}
