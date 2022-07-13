package net.vxr.vxrofmods.entity.custom;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.vxr.vxrofmods.WW2Mod;

public class ModEntities {
    public static final EntityType<DemogorgonEntity> DEMOGORGON = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(WW2Mod.MOD_ID, "demogorgon"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DemogorgonEntity::new)
                    .dimensions(EntityDimensions.fixed(1.8f, 3.0f)).build());

}
