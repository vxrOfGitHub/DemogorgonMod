package net.vxrofmods.demogorgonmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;

public class ModItemGroup {
    public static ItemGroup DEMOGORGON_MOD = Registry.register(Registries.ITEM_GROUP, new Identifier(DemogorgonMod.MOD_ID, "demogorgon_mod"),
            FabricItemGroup.builder()
                    .displayName(Text.literal("Demogorgon Item Group"))
                    .icon(() -> new ItemStack(ModItems.DEMOGORGON_SPAWN_EGG)).entries((displayContext, entries) -> {
                        entries.add(ModItems.DEMOGORGON_HEAD_HELMET);
                        entries.add(ModItems.DEMOGORGON_SPAWN_EGG);
                        entries.add(ModItems.DEMO_DOG_SPAWN_EGG);
                    }).build());

    public static void registerItemGroups() {

    }
}
