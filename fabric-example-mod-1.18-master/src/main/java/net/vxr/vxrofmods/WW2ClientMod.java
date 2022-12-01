package net.vxr.vxrofmods;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.vxr.vxrofmods.block.ModBlocks;
import net.vxr.vxrofmods.block.entity.ModBlockEntities;
import net.vxr.vxrofmods.entity.ModEntities;
import net.vxr.vxrofmods.entity.client.*;
import net.vxr.vxrofmods.entity.client.armor.*;
import net.vxr.vxrofmods.event.KeyInputHandler;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.networking.ModMessages;
import net.vxr.vxrofmods.screen.DiamondMinerScreen;
import net.vxr.vxrofmods.screen.ModScreenHandlers;
import net.vxr.vxrofmods.util.ModModelPredicateProvider;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Environment(EnvType.CLIENT)
public class WW2ClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.DEMOGORGON, DemogorgonRenderer::new);
        EntityRendererRegistry.register(ModEntities.PENGUIN_AVATAR, PenguinAvatarRenderer::new);
        EntityRendererRegistry.register(ModEntities.JOSH_SUMPFMAUS, JoshSumpfmausAvatarRenderer::new);
        EntityRendererRegistry.register(ModEntities.MICHEL_WOLF, MichelWolfAvatarRenderer::new);
        EntityRendererRegistry.register(ModEntities.DOME_CAPYBARA, DomeCapybaraAvatarRenderer::new);
        EntityRendererRegistry.register(ModEntities.POKU_CAPYBARA, PokuCapybaraAvatarRenderer::new);
        EntityRendererRegistry.register(ModEntities.MORITZ_DRAGON, MoritzDragonAvatarRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(new PenguinAvatarHelmetRenderer(), ModItems.PENGUIN_HELMET);
        GeoArmorRenderer.registerArmorRenderer(new JoshSumpfmausAvatarHelmetRenderer(), ModItems.JOSH_SUMPFMAUS_HELMET);
        GeoArmorRenderer.registerArmorRenderer(new MichelWolfAvatarHelmetRenderer(), ModItems.MICHEL_WOLF_HELMET);
        GeoArmorRenderer.registerArmorRenderer(new DomeCapybaraAvatarHelmetRenderer(), ModItems.DOME_CAPYBARA_HELMET);
        GeoArmorRenderer.registerArmorRenderer(new PokuCapybaraAvatarHelmetRenderer(), ModItems.POKU_CAPYBARA_HELMET);
        GeoArmorRenderer.registerArmorRenderer(new MoritzDragonAvatarHelmetRenderer(), ModItems.MORITZ_DRAGON_HELMET);
        GeoArmorRenderer.registerArmorRenderer(new DreamHelmetRenderer(), ModItems.Dream_Helmet);
        GeoArmorRenderer.registerArmorRenderer(new DreamChestplateRenderer(), ModItems.Dream_Chestplate);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DIAMOND_MINER_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FRAGMENT_HOLDER, RenderLayer.getCutout());

        HandledScreens.register(ModScreenHandlers.DIAMOND_MINER_SCREEN_HANDLER, DiamondMinerScreen::new);

        ModModelPredicateProvider.registerModModels();

        KeyInputHandler.register();

        ModMessages.registerS2CPackets();

        BlockEntityRendererRegistry.register(ModBlockEntities.DIAMOND_MINER, DiamondMinerBlockEntityRenderer::new);


    }

}
