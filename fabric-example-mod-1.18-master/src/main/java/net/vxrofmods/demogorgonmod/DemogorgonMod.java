package net.vxrofmods.demogorgonmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.vxrofmods.demogorgonmod.entity.ModEntities;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonEntity;
import net.vxrofmods.demogorgonmod.item.ModItemGroup;
import net.vxrofmods.demogorgonmod.item.ModItems;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DemogorgonMod implements ModInitializer {
	public static final String MOD_ID = "demogorgonmod";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static void main(String[] args) {
	}

	@Override
	public void onInitialize() {

		ModItemGroup.registerItemGroups();
		ModItems.registerModItems();

		FabricDefaultAttributeRegistry.register(ModEntities.DEMOGORGON, DemogorgonEntity.setAttributes());
	}
}


