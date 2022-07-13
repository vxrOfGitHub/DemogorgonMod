package net.vxr.vxrofmods.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;

public class ModItemGroup {
    public static final ItemGroup Custom_Mods = FabricItemGroupBuilder.build(new Identifier(WW2Mod.MOD_ID, "custom_mods"),
            () -> new ItemStack(ModItems.Dream_Star));
}
