package net.vxrofmods.demogorgonmod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.DemogorgonMod;

public class ModItemGroup {
    public static ItemGroup DEMOGORGON_MOD;

    public static void registerItemGroups() {
        DEMOGORGON_MOD = FabricItemGroup.builder(new Identifier(DemogorgonMod.MOD_ID, "demogorgon_mod"))
                .displayName(Text.literal("Demogorgon Item Group"))
                .icon(() -> new ItemStack(ModItems.DEMOGORGON_SPAWN_EGG)).build();
    }
}
