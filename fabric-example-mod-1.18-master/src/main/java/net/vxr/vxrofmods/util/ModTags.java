package net.vxr.vxrofmods.util;

import com.mojang.datafixers.types.templates.Tag;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.vxr.vxrofmods.WW2Mod;

import javax.swing.text.html.HTML;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> DOWSING_ROD_DETECTABLE_BLOCKS =
                createTag("dowsing_rod_detectable_blocks");

        public static final TagKey<Block> DREAM_AXE_DETECTABLE_BLOCKS =
                createTag("dream_axe_detectable_blocks");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(Registry.BLOCK_KEY,new Identifier(WW2Mod.MOD_ID, name));
        }

        private static TagKey<Block> createCommonTag(String name) {
            return TagKey.of(Registry.BLOCK_KEY,new Identifier("c", name));
        }

    }

    public static class Items {
        public static final TagKey<Item> SPAWN_INGOTS = createCommonTag("spawn_ingots");
        public static final TagKey<Item> DREAM_STARS = createCommonTag("dream_stars");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(Registry.ITEM_KEY,new Identifier(WW2Mod.MOD_ID, name));
        }

        private static TagKey<Item> createCommonTag(String name) {
            return TagKey.of(Registry.ITEM_KEY,new Identifier("c", name));
        }

    }

}
