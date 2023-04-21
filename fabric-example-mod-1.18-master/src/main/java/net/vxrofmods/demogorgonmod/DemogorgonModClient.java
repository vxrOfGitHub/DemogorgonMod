package net.vxrofmods.demogorgonmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.vxrofmods.demogorgonmod.entity.ModEntities;
import net.vxrofmods.demogorgonmod.entity.client.DemogorgonRenderer;
import net.vxrofmods.demogorgonmod.networking.ModMessages;

@Environment(EnvType.CLIENT)
public class DemogorgonModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(ModEntities.DEMOGORGON,DemogorgonRenderer::new);

        ModMessages.registerS2CPackets();
    }

}
