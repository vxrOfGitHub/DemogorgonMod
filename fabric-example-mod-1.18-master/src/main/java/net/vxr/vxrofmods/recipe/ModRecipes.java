package net.vxr.vxrofmods.recipe;


import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.vxr.vxrofmods.WW2Mod;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(WW2Mod.MOD_ID, DiamondMinerRecipe.Serializer.ID),
                DiamondMinerRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(WW2Mod.MOD_ID, DiamondMinerRecipe.Type.ID),
                DiamondMinerRecipe.Type.INSTANCE);
    }

}
