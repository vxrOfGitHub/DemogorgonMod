package net.vxrofmods.demogorgonmod.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import net.vxrofmods.demogorgonmod.item.ModItems;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeGenerator extends FabricRecipeProvider {

    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {

        //Smelting Recipe Example
        /*offerSmelting(exporter, List.of(ModItems.TEST_ITEM), RecipeCategory.MISC, ModItems.DEMOGORGON_SPAWN_EGG,
            3f, 300, "citrine"); */

        //Crafting Recipe Example
        /*
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.TEST_ITEM)
                .pattern("###")
                .pattern("CCC")
                .pattern("###")
                .input('#', Items.ITEM_FRAME)
                .input('C', Items.NAME_TAG)
                .criterion(FabricRecipeProvider.hasItem(Items.ITEM_FRAME),
                        FabricRecipeProvider.conditionsFromItem(Items.ITEM_FRAME))
                .criterion(FabricRecipeProvider.hasItem(Items.NAME_TAG),
                        FabricRecipeProvider.conditionsFromItem(Items.NAME_TAG))
                .offerTo(exporter, new Identifier(FabricRecipeProvider.getRecipeName(ModItems.TEST_ITEM)));
         */
    }
}
