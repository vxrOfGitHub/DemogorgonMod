package net.vxr.vxrofmods.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.entity.custom.*;

public class ModEntities {
    public static final EntityType<DemogorgonEntity> DEMOGORGON = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(WW2Mod.MOD_ID, "demogorgon"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DemogorgonEntity::new)
                    .dimensions(EntityDimensions.fixed(1.8f, 3.0f)).build());

    public static final EntityType<vxrPenguinAvatarEntity> PENGUIN_AVATAR = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(WW2Mod.MOD_ID, "penguin_avatar"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, vxrPenguinAvatarEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4f, 0.55f)).build());

    public static final EntityType<MichelWolfAvatarEntity> MICHEL_WOLF = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(WW2Mod.MOD_ID, "michel_wolf"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MichelWolfAvatarEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.1f)).build());

    public static final EntityType<MoritzDragonAvatarEntity> MORITZ_DRAGON = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(WW2Mod.MOD_ID, "moritz_dragon"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MoritzDragonAvatarEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4f, 0.55f)).build());

    public static final EntityType<DomeCapybaraAvatarEntity> DOME_CAPYBARA = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(WW2Mod.MOD_ID, "dome_capybara"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DomeCapybaraAvatarEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 1.2f)).build());

    public static final EntityType<PokuCapybaraAvatarEntity> POKU_CAPYBARA = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(WW2Mod.MOD_ID, "poku_capybara"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PokuCapybaraAvatarEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 1.2f)).build());
}
