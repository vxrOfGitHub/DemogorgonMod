package net.vxr.vxrofmods.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.block.custom.DiamondMinerBlock;
import net.vxr.vxrofmods.block.custom.SpeedyBlock;
import net.vxr.vxrofmods.item.ModItemGroup;

import javax.annotation.Nullable;
import java.util.List;

public class ModBlocks {

    public static final Block Test_BLOCK =  registerBlock("test_block",
            new Block(FabricBlockSettings.of(Material.METAL).strength(2f).requiresTool()), ModItemGroup.Custom_Mods);
    public static final Block SPEEDY_BLOCK =  registerBlock("speedy_block",
            new SpeedyBlock(FabricBlockSettings.of(Material.METAL).strength(2f).requiresTool()), ModItemGroup.Custom_Mods, "tooltip.vxrofmods.speedy_block");
    public static final Block DIAMOND_MINER_BLOCK =  registerBlock("diamond_miner_block",
            new DiamondMinerBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()), ModItemGroup.Custom_Mods);

    private static Block registerBlock(String name, Block block, ItemGroup group, String tooltipKey) {
        registerBlockItem(name, block, group, tooltipKey);
        return Registry.register(Registry.BLOCK, new Identifier(WW2Mod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group, String tooltipKey) {
        return Registry.register(Registry.ITEM, new Identifier(WW2Mod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)) {
                    @Override
                    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                        tooltip.add(new TranslatableText(tooltipKey));
                    }
                });
    }
    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(WW2Mod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(WW2Mod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static void registerModBlocks() {
        WW2Mod.LOGGER.info("Registering ModBlocks" + WW2Mod.MOD_ID);
    }
}
