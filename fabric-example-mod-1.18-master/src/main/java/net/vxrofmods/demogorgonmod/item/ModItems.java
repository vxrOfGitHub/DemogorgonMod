package net.vxrofmods.demogorgonmod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.ModEntities;

public class ModItems {

    public static final Item TEST_ITEM = registerItem("test_item",
            new Item(new FabricItemSettings()));

    public static final Item DEMOGORGON_SPAWN_EGG = registerItem("demogorgon_spawn_egg",
            new SpawnEggItem(ModEntities.DEMOGORGON, 0x707070, 0x755b5b,
                    new FabricItemSettings()));
    public static final Item DEMO_DOG_SPAWN_EGG = registerItem("demo_dog_spawn_egg",
            new SpawnEggItem(ModEntities.DEMO_DOG, 0x707070, 0x755b5b,
                    new FabricItemSettings()));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(DemogorgonMod.MOD_ID, name), item);
    }

    public static void addItemsToItemGroup() {
        addToItemGroup(ItemGroups.SPAWN_EGGS, DEMOGORGON_SPAWN_EGG);
        addToItemGroup(ModItemGroup.DEMOGORGON_MOD, DEMOGORGON_SPAWN_EGG);
        addToItemGroup(ItemGroups.SPAWN_EGGS, DEMO_DOG_SPAWN_EGG);
        addToItemGroup(ModItemGroup.DEMOGORGON_MOD, DEMO_DOG_SPAWN_EGG);
    }

    private static void addToItemGroup (ItemGroup group, Item item) {
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
    }

    public static void registerModItems() {
        DemogorgonMod.LOGGER.info("Registering Mod Items for " + DemogorgonMod.MOD_ID);

        addItemsToItemGroup();
    }

}
