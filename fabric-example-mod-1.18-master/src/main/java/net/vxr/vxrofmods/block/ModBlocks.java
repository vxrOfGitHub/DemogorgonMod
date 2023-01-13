package net.vxr.vxrofmods.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.block.custom.DreamBlock;
import net.vxr.vxrofmods.block.custom.FragmentHolderBlock;
import net.vxr.vxrofmods.item.ModItemGroup;

import javax.annotation.Nullable;
import java.util.List;

public class ModBlocks {

    public static final Block DREAM_BLOCK =  registerBlock("dream_block",
            new DreamBlock(FabricBlockSettings.of(Material.METAL).strength(6f).requiresTool()
                    .luminance((state) -> state.get(DreamBlock.CLICKED) ? 15:0)), ModItemGroup.Custom_Mods
            , new FabricItemSettings().fireproof());

    public static final Block DREAM_ORE =  registerBlock("dream_ore",
            new OreBlock(FabricBlockSettings.of(Material.METAL).strength(30.0F, 1200.0F).sounds(BlockSoundGroup.STONE).requiresTool()
            , UniformIntProvider.create(3, 7))
            , ModItemGroup.Custom_Mods
            , new FabricItemSettings().fireproof());

    public static final Block FRAGMENT_HOLDER =  registerBlock("fragment_holder",
            new FragmentHolderBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().strength(12f)), ModItemGroup.Custom_Mods);

    /*public static final Block DIAMOND_MINER_BLOCK =  registerBlock("diamond_miner_block",
            new DiamondMinerBlock(FabricBlockSettings.of(Material.METAL).nonOpaque().strength(3f).requiresTool()), ModItemGroup.Custom_Mods);*/

    private static Block registerBlock(String name, Block block, ItemGroup group, String tooltipKey) {
        registerBlockItem(name, block, group, tooltipKey);
        return Registry.register(Registry.BLOCK, new Identifier(WW2Mod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group, String tooltipKey) {
        return Registry.register(Registry.ITEM, new Identifier(WW2Mod.MOD_ID, name),


                new BlockItem(block, new FabricItemSettings().group(group)) {
                    @Override
                    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                        tooltip.add(Text.translatable(tooltipKey));
                    }
                });
    }
    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(WW2Mod.MOD_ID, name), block);
    }
    private static Block registerBlock(String name, Block block, ItemGroup group, FabricItemSettings settings) {
        registerBlockItem(name, block, group, settings);
        return Registry.register(Registry.BLOCK, new Identifier(WW2Mod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group, FabricItemSettings settings) {
        /*if(block.equals(ModBlocks.DREAM_BLOCK) || block.equals(ModBlocks.DREAM_ORE)) {
            return Registry.register(Registry.ITEM, new Identifier(WW2Mod.MOD_ID, name),
                    new BlockItem(block, new FabricItemSettings().fireproof().group(group)));
        }*/
        return Registry.register(Registry.ITEM, new Identifier(WW2Mod.MOD_ID, name),
                new BlockItem(block, settings.group(group)));
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        /*if(block.equals(ModBlocks.DREAM_BLOCK) || block.equals(ModBlocks.DREAM_ORE)) {
            return Registry.register(Registry.ITEM, new Identifier(WW2Mod.MOD_ID, name),
                    new BlockItem(block, new FabricItemSettings().fireproof().group(group)));
        }*/
        return Registry.register(Registry.ITEM, new Identifier(WW2Mod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static void registerModBlocks() {
        WW2Mod.LOGGER.info("Registering ModBlocks" + WW2Mod.MOD_ID);
    }
}
