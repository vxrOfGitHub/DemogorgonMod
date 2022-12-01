package net.vxr.vxrofmods.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;

import net.minecraft.util.registry.Registry;
import net.vxr.vxrofmods.entity.ModEntities;
import net.vxr.vxrofmods.item.custom.*;
import net.vxr.vxrofmods.sound.ModSounds;

public class ModItems {
    public static final Item Spawn_Ingot = registerItem("spawn_ingot",
            new Item(new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item Dream_Star = registerItem("dream_star",
            new Item(new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item COIN = registerItem("coin",
            new CoinItem(new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item DREAM_SHARD = registerItem("dream_shard",
            new Item(new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item WARDENS_HEARING_AID = registerItem("wardens_hearing_aid",
            new Item(new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item DRAGONS_CLAW = registerItem("dragons_claw",
            new Item(new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item Foundation_Egg = registerItem("foundation_egg",
            new Item(new FabricItemSettings().group(ModItemGroup.Custom_Mods)));


    public static final Item Dream_Sword = registerItem("dream_sword",
            new DreamSword(ModToolMaterials.DREAM, 2, -2.0f,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item Dream_Axe = registerItem("dream_axe",
        new DreamAxeItem(ModToolMaterials.DREAM, 4, -2.6f,
                new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item Dream_Pickaxe = registerItem("dream_pickaxe",
            new DreamPickAxeItem(ModToolMaterials.DREAM, 0, -2.4f,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item Dream_Helmet = registerItem("dream_helmet",
            new ModHelmetItem(ModArmorMaterials.Dream, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item Dream_Chestplate = registerItem("dream_chestplate",
            new ModChestplateItem(ModArmorMaterials.Dream, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item Dream_Leggings = registerItem("dream_leggings",
            new ModLeggingsItem(ModArmorMaterials.Dream, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item Dream_Boots = registerItem("dream_boots",
            new DreamBootsItem(ModArmorMaterials.Dream, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item DEMOGORGON_SPAWN_EGG = registerItem("demogorgon_spawn_egg",
            new SpawnEggItem(ModEntities.DEMOGORGON,0x707070, 0x755b5b,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item PENGUIN_SPAWN_EGG = registerItem("penguin_spawn_egg",
            new SpawnEggItem(ModEntities.PENGUIN_AVATAR,0x46487d, 0xffffff,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item JOSH_SUMPFMAUS_SPAWN_EGG = registerItem("josh_sumpfmaus_spawn_egg",
            new SpawnEggItem(ModEntities.JOSH_SUMPFMAUS,0x0f2226, 0x46fd62,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item MICHEL_WOLF_SPAWN_EGG = registerItem("michel_wolf_spawn_egg",
            new SpawnEggItem(ModEntities.MICHEL_WOLF,0x555555, 0xFFAA00,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item DOME_CAPYBARA_SPAWN_EGG = registerItem("dome_capybara_spawn_egg",
            new SpawnEggItem(ModEntities.DOME_CAPYBARA,0x7f6849, 0x302d28,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item POKU_CAPYBARA_SPAWN_EGG = registerItem("poku_capybara_spawn_egg",
            new SpawnEggItem(ModEntities.POKU_CAPYBARA,0x040404, 0x996a55,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item MORITZ_DRAGON_SPAWN_EGG = registerItem("moritz_dragon_spawn_egg",
            new SpawnEggItem(ModEntities.MORITZ_DRAGON,0x161616, 0x000000,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item BACKGROUND_MUSIC_DISC = registerItem("background_music_disc",
            new ModMusicDiskItem(7, ModSounds.BACKGROUND_MUSIC,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1), 54));

    public static final Item PENGUIN_HELMET = registerItem("penguin_helmet",
            new PenguinAvatarHelmetItem(ModArmorMaterials.AVATAR, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item JOSH_SUMPFMAUS_HELMET = registerItem("josh_sumpfmaus_helmet",
            new JoshSumpfmausAvatarHelmetItem(ModArmorMaterials.AVATAR, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item MICHEL_WOLF_HELMET = registerItem("michel_wolf_helmet",
            new MichelWolfAvatarHelmetItem(ModArmorMaterials.AVATAR, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item DOME_CAPYBARA_HELMET = registerItem("dome_capybara_helmet",
            new DomeCapybaraAvatarHelmetItem(ModArmorMaterials.AVATAR, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item POKU_CAPYBARA_HELMET = registerItem("poku_capybara_helmet",
            new PokuCapybaraAvatarHelmetItem(ModArmorMaterials.AVATAR, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item MORITZ_DRAGON_HELMET = registerItem("moritz_dragon_helmet",
            new MoritzDragonAvatarHelmetItem(ModArmorMaterials.AVATAR, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item DIAMOND_FRAGMENT = registerItem("diamond_fragment",
            new FragmentItem(new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item IRON_FRAGMENT = registerItem("iron_fragment",
            new FragmentItem(new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item EMERALD_FRAGMENT = registerItem("emerald_fragment",
            new FragmentItem(new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item REDSTONE_FRAGMENT = registerItem("redstone_fragment",
            new FragmentItem(new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item GOLD_FRAGMENT = registerItem("gold_fragment",
            new FragmentItem(new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item LAPIS_LAZULI_FRAGMENT = registerItem("lapis_lazuli_fragment",
            new FragmentItem(new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item COPPER_FRAGMENT = registerItem("copper_fragment",
            new FragmentItem(new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(WW2Mod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        WW2Mod.LOGGER.info("Registering Mod Items for " + WW2Mod.MOD_ID);
    }


}
