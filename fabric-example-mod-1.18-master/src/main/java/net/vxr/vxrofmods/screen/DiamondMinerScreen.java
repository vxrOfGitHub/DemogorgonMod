package net.vxr.vxrofmods.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vxr.vxrofmods.WW2Mod;

public class DiamondMinerScreen extends HandledScreen<DiamondMinerScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier(WW2Mod.MOD_ID, "textures/gui/diamond_miner_gui.png");

    public DiamondMinerScreen(DiamondMinerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        if(handler.isCrafting()) {
            drawTexture(matrices, x + 67, y + 43, 177, 1, handler.getScaledProgress(), 36);
        }

        if(handler.hasFuel()) {
            drawTexture(matrices, x + 18, y + 33 + 14 - handler.getScaledFuelProgress(), x + 20,
                    14 - handler.getScaledFuelProgress(), 14, handler.getScaledFuelProgress());
        }

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

}
