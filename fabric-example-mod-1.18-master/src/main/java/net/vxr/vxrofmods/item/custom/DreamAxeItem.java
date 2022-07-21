package net.vxr.vxrofmods.item.custom;

import net.minecraft.block.AirBlock;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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
        if(isValuableBlock(state.getBlock())) {
            breakBlockBelow(world, pos);
            breakBlockOnTop(world, pos);
            breakBlockNorth(world, pos);
            breakBlockEast(world, pos);
            breakBlockSouth(world, pos);
            breakBlockWest(world, pos);
        }


        return super.postMine(stack, world, state, pos, miner);
    }

    private void breakBlockNorth(World world, BlockPos pos) {
        for(int i = 1; i <= 15; i++) {
            Block blockNorth = world.getBlockState(pos.north(i)).getBlock();
            BlockPos posNorth = pos.north(i);
            if(isValuableBlock(blockNorth)) {
                world.breakBlock(pos.north(i), true);
                breakBlockOnTop(world, posNorth);
                breakBlockBelow(world, posNorth);
                breakBlockWest(world, posNorth);
                breakBlockEast(world, posNorth);
            } else {
                break;
            }
        }
    }
    private void breakBlockWest(World world, BlockPos pos) {
        for(int i = 1; i <= 15; i++) {
            Block blockWest = world.getBlockState(pos.west(i)).getBlock();
            BlockPos posWest = pos.west(i);
            if(isValuableBlock(blockWest)) {
                world.breakBlock(pos.west(i), true);
                breakBlockOnTop(world, posWest);
                breakBlockBelow(world, posWest);
                breakBlockNorth(world, posWest);
                breakBlockSouth(world, posWest);
            } else {
                break;
            }
        }
    }

    private void breakBlockSouth(World world, BlockPos pos) {
        for(int i = 1; i <= 15; i++) {
            Block blockSouth = world.getBlockState(pos.south(i)).getBlock();
            BlockPos posSouth = pos.south(i);
            if(isValuableBlock(blockSouth)) {
                world.breakBlock(pos.south(i), true);
                breakBlockOnTop(world, posSouth);
                breakBlockBelow(world, posSouth);
                breakBlockWest(world, posSouth);
                breakBlockEast(world, posSouth);
            } else {
                break;
            }
        }
    }

    private void breakBlockEast(World world, BlockPos pos) {
        for(int i = 1; i <= 15; i++) {
            Block blockEast = world.getBlockState(pos.east(i)).getBlock();
            BlockPos posEast = pos.east(i);
            if(isValuableBlock(blockEast)) {
                world.breakBlock(pos.east(i), true);
                breakBlockOnTop(world, posEast);
                breakBlockBelow(world, posEast);
                breakBlockNorth(world, posEast);
                breakBlockWest(world, posEast);
            } else {
                break;
            }
        }
    }

    private void breakBlockBelow(World world, BlockPos pos) {
        for(int i = 1; i <= 15; i++) {
            Block blockBelow = world.getBlockState(pos.down(i)).getBlock();
            if(isValuableBlock(blockBelow)) {
                world.breakBlock(pos.down(i), true);
            } else {
                break;
            }
        }
    }

    private void breakBlockOnTop(World world, BlockPos pos) {
        for(int i = 1; i <= 15; i++) {
            Block blockBelow = world.getBlockState(pos.up(i)).getBlock();
            if(isValuableBlock(blockBelow)) {
                world.breakBlock(pos.up(i), true);
            } else {
                break;
            }
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getWorld().isClient()) {
            BlockPos positionClicked = context.getBlockPos();
            PlayerEntity player = context.getPlayer();
            boolean foundBlock = false;

            for(int i = 0; i <= positionClicked.getY() + 64; i++) {
                Block blockBelow = context.getWorld().getBlockState(positionClicked.down(i)).getBlock();

                if(isValuableBlock(blockBelow)) {
                    outputValuableCoordinates(positionClicked.down(i), player, blockBelow);
                    foundBlock = true;
                    break;
                }
            }

            if(!foundBlock) {
                player.sendMessage(new TranslatableText("item.vxrofmods.dream_axe.no_wood"), false);
            }
        }

        context.getStack().damage(1, context.getPlayer(),
                (player) -> player.sendToolBreakStatus(player.getActiveHand()));

        return super.useOnBlock(context);
    }

    private void outputValuableCoordinates(BlockPos blockPos, PlayerEntity player, Block blockBelow) {
        player.sendMessage(new LiteralText("Found " + blockBelow.asItem().getName().getString() + " at: " +
                "(" + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ() + ")"), false);
    }

    private boolean isValuableBlock(Block block) {
        return Registry.BLOCK.getOrCreateEntry(Registry.BLOCK.getKey(block).get()).isIn(ModTags.Blocks.DREAM_AXE_DETECTABLE_BLOCKS);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslatableText("item.vxrofmods.dream_axe.tooltip.shift"));
        } else {
            tooltip.add(new TranslatableText("item.vxrofmods.dream_axe.tooltip"));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        setModeBreak(!isModeBreak());
        if(isModeBreak()) {
            user.sendMessage(new LiteralText("§bTree-Breaker activated§r"), true);
        } else if (!isModeBreak()) {
            user.sendMessage(new LiteralText("§bTree-Breaker deactivated§r"), true);
        }
        return super.use(world, user, hand);
    }

    public boolean isModeBreak() {
        return isModeBreak;
    }

    public void setModeBreak(boolean modeBreak) {
        isModeBreak = modeBreak;
    }

    private boolean isModeBreak;

}
