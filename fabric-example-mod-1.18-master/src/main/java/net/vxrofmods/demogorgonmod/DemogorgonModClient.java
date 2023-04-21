package net.vxrofmods.demogorgonmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.vxrofmods.demogorgonmod.entity.ModEntities;
import net.vxrofmods.demogorgonmod.entity.client.DemogorgonRenderer;

@Environment(EnvType.CLIENT)
public class DemogorgonModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(ModEntities.DEMOGORGON,DemogorgonRenderer::new);
    }

}
