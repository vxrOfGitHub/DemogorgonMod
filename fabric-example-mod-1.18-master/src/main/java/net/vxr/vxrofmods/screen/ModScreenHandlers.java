package net.vxr.vxrofmods.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;

public class ModScreenHandlers {
    public static ScreenHandlerType<DiamondMinerScreenHandler> DIAMOND_MINER_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        DIAMOND_MINER_SCREEN_HANDLER = new ScreenHandlerType<>(DiamondMinerScreenHandler::new);
                /*ScreenHandlerRegistry.registerSimple(new Identifier(WW2Mod.MOD_ID, "diamond_miner"),
                        DiamondMinerScreenHandler::new); */

    }
}
