package net.vxr.vxrofmods.util;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.vxr.vxrofmods.item.ModItems;

public class ModLootTableModifiers {

    private static final Identifier ENDER_DRAGON_ID
            = new Identifier("minecraft", "entities/ender_dragon");
    private static final Identifier WARDEN_ID
            = new Identifier("minecraft", "entities/warden");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {

            if(ENDER_DRAGON_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.DRAGONS_TEETH)) // Drops Dragons Teeth
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build()); // Amount to Drop = 1
                tableBuilder.pool(poolBuilder.build());
            }

            if(WARDEN_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(ModItems.WARDENS_EAR_ADE)) // Drops Wardens Ear Ade
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build()); // Amount to Drop = 1
                tableBuilder.pool(poolBuilder.build());
            }

        });
    }
}
