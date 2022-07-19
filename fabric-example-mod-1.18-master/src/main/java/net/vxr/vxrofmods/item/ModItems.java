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

    public static final Item WAFFLE = registerItem("waffle",
            new Item(new FabricItemSettings().group(ModItemGroup.Custom_Mods).food(ModFoodComponents.WAFFLE)));

    public static final Item Foundation_Egg = registerItem("foundation_egg",
            new Item(new FabricItemSettings().group(ModItemGroup.Custom_Mods)));


    public static final Item Dowsing_Rod = registerItem("dowsing_rod",
            new DowsingRodItem(new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxDamage(16)));

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
            new ArmorItem(ModArmorMaterials.Dream, EquipmentSlot.CHEST,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item Dream_Leggings = registerItem("dream_leggings",
            new ModLeggingsItem(ModArmorMaterials.Dream, EquipmentSlot.LEGS,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item Dream_Boots = registerItem("dream_boots",
            new ArmorItem(ModArmorMaterials.Dream, EquipmentSlot.FEET,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item DEMOGORGON_SPAWN_EGG = registerItem("demogorgon_spawn_egg",
            new SpawnEggItem(ModEntities.DEMOGORGON,0x707070, 0x755b5b,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item PENGUIN_SPAWN_EGG = registerItem("penguin_spawn_egg",
            new SpawnEggItem(ModEntities.PENGUIN_AVATAR,0x46487d, 0xffffff,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods)));

    public static final Item DREAM_BOW = registerItem("dream_bow",
            new BowItem(new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxDamage(1000)));

    public static final Item BACKGROUND_MUSIC_DISC = registerItem("background_music_disc",
            new ModMusicDiskItem(7, ModSounds.BACKGROUND_MUSIC,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));

    public static final Item PENGUIN_HELMET = registerItem("penguin_helmet",
            new PenguinAvatarHelmetItem(ModArmorMaterials.Dream, EquipmentSlot.HEAD,
                    new FabricItemSettings().group(ModItemGroup.Custom_Mods).maxCount(1)));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(WW2Mod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        WW2Mod.LOGGER.info("Registering Mod Items for " + WW2Mod.MOD_ID);
    }


}
