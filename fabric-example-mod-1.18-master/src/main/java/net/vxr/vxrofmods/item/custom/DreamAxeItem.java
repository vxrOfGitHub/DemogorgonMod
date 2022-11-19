package net.vxr.vxrofmods.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.vxr.vxrofmods.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DreamAxeItem extends AxeItem {
    public DreamAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient()) {

            if (!stack.hasNbt()) {
                NbtCompound nbt = new NbtCompound();
                nbt.putBoolean("dream_axe_mode_setter", false);
                stack.setNbt(nbt);
            }
            stack.getNbt().putInt("dream_axe_break_status_count", 0);

            if (isValuableBlock(state.getBlock()) && stack.getNbt().getBoolean("dream_axe_mode_setter")) {
                breakBlockBelow(world, pos, stack);
                breakBlockOnTop(world, pos, stack);
                breakBlockNorth(world, pos, stack);
                breakBlockEast(world, pos, stack);
                breakBlockSouth(world, pos, stack);
                breakBlockWest(world, pos, stack);
            }
            if (stack.getNbt().getInt("dream_axe_break_status_count") > 749) {
                stack.damage(749, miner, (e) -> {
                    e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                });
            } else {
                stack.damage(stack.getNbt().getInt("dream_axe_break_status_count"), miner, (e) -> {
                    e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                });
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }
    private void breakBlockNorth(World world, BlockPos pos, ItemStack stack) {
        for(int i = 1; i <= 15; i++) {
            Block blockNorth = world.getBlockState(pos.north(i)).getBlock();
            BlockPos posNorth = pos.north(i);
            if(isValuableBlock(blockNorth)) {
                world.breakBlock(pos.north(i), true);
                breakBlockOnTop(world, posNorth, stack);
                breakBlockBelow(world, posNorth, stack);
                breakBlockWest(world, posNorth, stack);
                breakBlockEast(world, posNorth, stack);
                stack.getNbt().putInt("dream_axe_break_status_count", stack.getNbt().getInt("dream_axe_break_status_count") + 1);
            } else {
                break;
            }
        }
    }

    private void breakBlockWest(World world, BlockPos pos, ItemStack stack) {
        for(int i = 1; i <= 15; i++) {
            Block blockWest = world.getBlockState(pos.west(i)).getBlock();
            BlockPos posWest = pos.west(i);
            if(isValuableBlock(blockWest)) {
                world.breakBlock(pos.west(i), true);
                breakBlockOnTop(world, posWest, stack);
                breakBlockBelow(world, posWest,stack);
                breakBlockNorth(world, posWest, stack);
                breakBlockSouth(world, posWest, stack);
                stack.getNbt().putInt("dream_axe_break_status_count", stack.getNbt().getInt("dream_axe_break_status_count") + 1);
            } else {
                break;
            }
        }
    }

    private void breakBlockSouth(World world, BlockPos pos, ItemStack stack) {
        for(int i = 1; i <= 15; i++) {
            Block blockSouth = world.getBlockState(pos.south(i)).getBlock();
            BlockPos posSouth = pos.south(i);
            if(isValuableBlock(blockSouth)) {
                world.breakBlock(pos.south(i), true);
                breakBlockOnTop(world, posSouth, stack);
                breakBlockBelow(world, posSouth, stack);
                breakBlockWest(world, posSouth, stack);
                breakBlockEast(world, posSouth, stack);
                stack.getNbt().putInt("dream_axe_break_status_count", stack.getNbt().getInt("dream_axe_break_status_count") + 1);
            } else {
                break;
            }
        }
    }

    private void breakBlockEast(World world, BlockPos pos, ItemStack stack) {
        for(int i = 1; i <= 15; i++) {
            Block blockEast = world.getBlockState(pos.east(i)).getBlock();
            BlockPos posEast = pos.east(i);
            if(isValuableBlock(blockEast)) {
                world.breakBlock(pos.east(i), true);
                breakBlockOnTop(world, posEast, stack);
                breakBlockBelow(world, posEast,stack);
                breakBlockNorth(world, posEast,stack);
                breakBlockWest(world, posEast, stack);
                stack.getNbt().putInt("dream_axe_break_status_count", stack.getNbt().getInt("dream_axe_break_status_count") + 1);
            } else {
                break;
            }
        }
    }

    private void breakBlockBelow(World world, BlockPos pos, ItemStack stack) {
        for(int i = 1; i <= 15; i++) {
            Block blockBelow = world.getBlockState(pos.down(i)).getBlock();
            if(isValuableBlock(blockBelow)) {
                world.breakBlock(pos.down(i), true);
                stack.getNbt().putInt("dream_axe_break_status_count", stack.getNbt().getInt("dream_axe_break_status_count") + 1);
            } else {
                break;
            }
        }
    }

    private void breakBlockOnTop(World world, BlockPos pos, ItemStack stack) {
        for(int i = 1; i <= 15; i++) {
            Block blockBelow = world.getBlockState(pos.up(i)).getBlock();
            if(isValuableBlock(blockBelow)) {
                world.breakBlock(pos.up(i), true);
                stack.getNbt().putInt("dream_axe_break_status_count", stack.getNbt().getInt("dream_axe_break_status_count") + 1);
            } else {
                break;
            }
        }
    }

    private boolean isValuableBlock(Block block) {
        return Registry.BLOCK.getOrCreateEntry(Registry.BLOCK.getKey(block).get()).isIn(ModTags.Blocks.DREAM_AXE_DETECTABLE_BLOCKS);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.vxrofmods.dream_axe.tooltip.shift"));
        } else {
            tooltip.add(Text.translatable("item.vxrofmods.dream_axe.tooltip"));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (!world.isClient()) {
            ItemStack stack = user.getStackInHand(hand);

            if (stack.getItem().equals(this)) {

                boolean modeSetter;
                NbtCompound nbt = new NbtCompound();

                if (stack.hasNbt()) {
                    nbt = stack.getNbt();
                    assert nbt != null;
                    modeSetter = nbt.getBoolean("dream_axe_mode_setter");
                } else {
                    modeSetter = false;
                    nbt.putBoolean("dream_axe_mode_setter", modeSetter);
                }

                modeSetter = !modeSetter;

                if(modeSetter) {
                    user.sendMessage(Text.literal("§bTree-Breaker activated§r"), true);
                } else {
                    user.sendMessage(Text.literal("§bTree-Breaker deactivated§r"), true);
                }

                nbt.putBoolean("dream_axe_mode_setter", modeSetter);
                stack.setNbt(nbt);
            }
        }
        return super.use(world, user, hand);
    }

}
