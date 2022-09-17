package net.vxr.vxrofmods;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.vxr.vxrofmods.block.ModBlocks;
import net.vxr.vxrofmods.block.entity.ModBlockEntities;
import net.vxr.vxrofmods.effect.ModEffects;
import net.vxr.vxrofmods.event.PlayerTickHandler;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.networking.ModMessages;
import net.vxr.vxrofmods.recipe.ModRecipes;
import net.vxr.vxrofmods.screen.ModScreenHandlers;
import net.vxr.vxrofmods.sound.ModSounds;
import net.vxr.vxrofmods.util.ModRegistries;
import net.vxr.vxrofmods.world.gen.ModWorldGen;
import net.vxr.vxrofmods.world.structure.ModStructures;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

public class WW2Mod implements ModInitializer {
	public static final String MOD_ID = "vxrofmods";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static void main(String[] args) {
	}

	@Override
	public void onInitialize() {

		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModRegistries.registerModStuffs();
		ModEffects.registerEffects();
		ModSounds.registerSounds();
		ModWorldGen.generateModWorldGen();
		ModBlockEntities.registerAllBlockEntities();
		ModRecipes.registerRecipes();
		ModScreenHandlers.registerAllScreenHandlers();
		ModMessages.registerC2SPackets();
		ModStructures.registerStructureFeatures();

		ServerTickEvents.START_SERVER_TICK.register(new PlayerTickHandler());

		GeckoLib.initialize();
	}
}


