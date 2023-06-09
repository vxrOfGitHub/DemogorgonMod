package net.vxrofmods.demogorgonmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.passive.WolfEntity;
import net.vxrofmods.demogorgonmod.entity.ModEntities;
import net.vxrofmods.demogorgonmod.entity.custom.DemoDogEntity;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonEntity;
import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonPortalEntity;
import net.vxrofmods.demogorgonmod.entity.custom.FriendlyDemoDogEntity;
import net.vxrofmods.demogorgonmod.event.ServerPlayerTickHandler;
import net.vxrofmods.demogorgonmod.item.ModItemGroup;
import net.vxrofmods.demogorgonmod.item.ModItems;
import net.vxrofmods.demogorgonmod.networking.ModMessages;
import net.vxrofmods.demogorgonmod.sound.ModSounds;
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
		ModSounds.registerSounds();

		ServerTickEvents.START_SERVER_TICK.register(new ServerPlayerTickHandler());

		FabricDefaultAttributeRegistry.register(ModEntities.DEMOGORGON, DemogorgonEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.DEMO_DOG, DemoDogEntity.setAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.FRIENDLY_DEMO_DOG, FriendlyDemoDogEntity.setAttributes());

		ModMessages.registerC2SPackets();
	}
}


