package net.vxr.vxrofmods.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import net.vxr.vxrofmods.entity.ModEntities;

public class ModEntitySpawn {
    public static void addEntitySpawn() {
        /*BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.END_MIDLANDS), SpawnGroup.CREATURE,
                ModEntities.DEMOGORGON, 1000, 1, 1);

        SpawnRestrictionAccessor.callRegister(ModEntities.DEMOGORGON, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
    */}

}
