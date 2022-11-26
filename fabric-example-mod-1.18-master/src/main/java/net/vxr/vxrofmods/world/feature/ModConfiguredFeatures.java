package net.vxr.vxrofmods.world.feature;

import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.*;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {
    public static final List<OreFeatureConfig.Target> END_DREAM_ORES = List.of(
            OreFeatureConfig.createTarget(new BlockMatchRuleTest(Blocks.END_STONE), ModBlocks.DREAM_ORE.getDefaultState()));

    public static final RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> DREAM_ORE =
            ConfiguredFeatures.register("dream_ore", Feature.ORE, new OreFeatureConfig(END_DREAM_ORES, 20));

    public static void registerConfiguredFeatures() {
        WW2Mod.LOGGER.debug("Registering the ModConfiguredFeatures for " + WW2Mod.MOD_ID);
    }

}
