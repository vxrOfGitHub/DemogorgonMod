package net.vxr.vxrofmods.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.vxr.vxrofmods.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class DreamAxeItem extends AxeItem {
    public DreamAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if(world.isClient()) {
            BlockPos positionBlockBroken = pos;
            boolean breakBlock = false;
            brokenBlockCount = 0;

            for(int i = 0; i <= positionBlockBroken.getY() + 64; i++) {
                Block blockBelow = world.getBlockState(pos.down(i)).getBlock();

                if(isWoodenBlock(blockBelow)) {
                    breakBlock = true;
                    brokenBlockCount++;
                    break;
                }
            }


        }

        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslatableText("item.vxrofmods.dream_axe.tooltip.shift"));
        } else {
            tooltip.add(new TranslatableText("item.vxrofmods.dream_axe.tooltip"));
        }
    }

    private boolean isWoodenBlock(Block block) {
        return Registry.BLOCK.getOrCreateEntry(Registry.BLOCK.getKey(block).get()).isIn(ModTags.Blocks.DREAM_AXE_DETECTABLE_BLOCKS);
    }

    private static int brokenBlockCount;

}
