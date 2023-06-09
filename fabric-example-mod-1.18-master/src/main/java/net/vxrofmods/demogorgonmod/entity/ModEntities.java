package net.vxrofmods.demogorgonmod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.custom.DemoDogEntity;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonEntity;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonPortalEntity;

public class ModEntities {
    public static final EntityType<DemogorgonEntity> DEMOGORGON = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(DemogorgonMod.MOD_ID, "demogorgon"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DemogorgonEntity::new)
                    .dimensions(EntityDimensions.fixed(1.8f, 3.0f)).build());

    public static final EntityType<DemoDogEntity> DEMO_DOG = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(DemogorgonMod.MOD_ID, "demo_dog"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DemoDogEntity::new)
                    .dimensions(EntityDimensions.fixed(1.5f, 1f)).build());

    public static final EntityType<DemogorgonPortalEntity> DEMOGORGON_PORTAL = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(DemogorgonMod.MOD_ID, "demogorgon_portal"),
            FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, DemogorgonPortalEntity::new)
                    .dimensions(EntityDimensions.fixed(1f, 1f)).build());
}
