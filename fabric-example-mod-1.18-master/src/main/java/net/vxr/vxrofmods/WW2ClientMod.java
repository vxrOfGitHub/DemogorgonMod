package net.vxr.vxrofmods;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.vxr.vxrofmods.block.ModBlocks;
import net.vxr.vxrofmods.entity.client.DemogorgonRenderer;
import net.vxr.vxrofmods.entity.ModEntities;
import net.vxr.vxrofmods.entity.client.PenguinAvatarRenderer;
import net.vxr.vxrofmods.entity.client.armor.PenguinAvatarHelmetRenderer;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.screen.DiamondMinerScreen;
import net.vxr.vxrofmods.screen.ModScreenHandlers;
import net.vxr.vxrofmods.util.ModModelPredicateProvider;
import org.lwjgl.glfw.GLFW;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Environment(EnvType.CLIENT)
public class WW2ClientMod implements ClientModInitializer {
    private static KeyBinding Jetpack;
    private boolean UseJetpack;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.DEMOGORGON, DemogorgonRenderer::new);
        EntityRendererRegistry.register(ModEntities.PENGUIN_AVATAR, PenguinAvatarRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(new PenguinAvatarHelmetRenderer(), ModItems.PENGUIN_HELMET);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DIAMOND_MINER_BLOCK, RenderLayer.getCutout());

        ScreenRegistry.register(ModScreenHandlers.DIAMOND_MINER_SCREEN_HANDLER, DiamondMinerScreen::new);

        ModModelPredicateProvider.registerModModels();


        // Keybinds
        Jetpack = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.vxrofmods.jetpack", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O, "category.vxrofmods.ww2mods"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (Jetpack.wasPressed()) {
                UseJetpack = true;
            }
        });
        // Keybinds end

    }
}
