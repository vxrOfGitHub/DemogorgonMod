package net.vxrofmods.demogorgonmod;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.vxrofmods.demogorgonmod.data.ModLootTableGenerator;
import net.vxrofmods.demogorgonmod.data.ModModelProvider;
import net.vxrofmods.demogorgonmod.data.ModRecipeGenerator;

public class DemogorgonModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModLootTableGenerator::new);
        pack.addProvider(ModRecipeGenerator::new);
        pack.addProvider(ModModelProvider::new);
    }
}
