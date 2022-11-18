package net.vxr.vxrofmods.item.custom;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.vxr.vxrofmods.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DreamPickAxeItem extends PickaxeItem {

    public DreamPickAxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {

        if(!world.isClient()) {
            if(!stack.hasNbt()) {
                NbtCompound nbt = new NbtCompound();
                nbt.putBoolean("dream_pickaxe_mode_setter", false);
                stack.setNbt(nbt);
            }
            stack.getNbt().putInt("dream_pickaxe_break_status_count" , 0);

            assert stack.getNbt() != null;
            if(stack.getNbt().getBoolean("dream_pickaxe_mode_setter")) {
                if(pos.getY() - 1 > miner.getPos().getY() || pos.getY() < miner.getPos().getY()) {
                    breakBlockUp(world, pos, state, stack);
                }  else if(miner.getHorizontalFacing() == Direction.NORTH || miner.getHorizontalFacing() == Direction.SOUTH) {
                    breakBlockNorth(world, pos, state, stack);
                } else if(miner.getHorizontalFacing() == Direction.EAST || miner.getHorizontalFacing() == Direction.WEST) {
                    breakBlockEast(world, pos, state, stack);
                }
            }
            if(stack.getNbt().getInt("dream_pickaxe_break_status_count") > 749) {
                stack.damage(749, miner, (e) -> {
                    e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);});
            } else {
                stack.damage(stack.getNbt().getInt("dream_pickaxe_break_status_count"), miner, (e) -> {
                    e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);});
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }

    private void breakBlockNorth(World world, BlockPos pos, BlockState state, ItemStack stack) {
        if(isValuableBlock(state.getBlock())) {
            assert stack.getNbt() != null;
            int breakStatusCount = stack.getNbt().getInt("dream_pickaxe_break_status_count");
            if(isValuableBlock(world.getBlockState(pos.up()).getBlock())) {
                world.breakBlock(pos.up(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.down()).getBlock())) {
                world.breakBlock(pos.down(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.west()).getBlock())) {
                world.breakBlock(pos.west(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.east()).getBlock())) {
                world.breakBlock(pos.east(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.up().west()).getBlock())) {
                world.breakBlock(pos.up().west(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.up().east()).getBlock())) {
                world.breakBlock(pos.up().east(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.down().west()).getBlock())) {
                world.breakBlock(pos.down().west(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.down().east()).getBlock())) {
                world.breakBlock(pos.down().east(), true);
                breakStatusCount++;
            }
            stack.getNbt().putInt("dream_pickaxe_break_status_count", breakStatusCount);
        }
    }

    private void breakBlockUp(World world, BlockPos pos, BlockState state ,ItemStack stack) {
        if(isValuableBlock(state.getBlock())) {
            assert stack.getNbt() != null;
            int breakStatusCount = stack.getNbt().getInt("dream_pickaxe_break_status_count");
            if(isValuableBlock(world.getBlockState(pos.north()).getBlock())) {
                world.breakBlock(pos.north(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.south()).getBlock())) {
                world.breakBlock(pos.south(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.west()).getBlock())) {
                world.breakBlock(pos.west(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.east()).getBlock())) {
                world.breakBlock(pos.east(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.north().west()).getBlock())) {
                world.breakBlock(pos.north().west(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.north().east()).getBlock())) {
                world.breakBlock(pos.north().east(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.south().west()).getBlock())) {
                world.breakBlock(pos.south().west(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.south().east()).getBlock())) {
                world.breakBlock(pos.south().east(), true);
                breakStatusCount++;
            }
            stack.getNbt().putInt("dream_pickaxe_break_status_count", breakStatusCount);
        }
    }

    private void breakBlockEast(World world, BlockPos pos, BlockState state, ItemStack stack) {
        if(isValuableBlock(state.getBlock())) {
            assert stack.getNbt() != null;
            int breakStatusCount = stack.getNbt().getInt("dream_pickaxe_break_status_count");
            if(isValuableBlock(world.getBlockState(pos.up()).getBlock())) {
                world.breakBlock(pos.up(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.down()).getBlock())) {
                world.breakBlock(pos.down(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.south()).getBlock())) {
                world.breakBlock(pos.south(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.north()).getBlock())) {
                world.breakBlock(pos.north(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.up().south()).getBlock())) {
                world.breakBlock(pos.up().south(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.up().north()).getBlock())) {
                world.breakBlock(pos.up().north(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.down().south()).getBlock())) {
                world.breakBlock(pos.down().south(), true);
                breakStatusCount++;
            }
            if(isValuableBlock(world.getBlockState(pos.down().north()).getBlock())) {
                world.breakBlock(pos.down().north(), true);
                breakStatusCount++;
            }
            stack.getNbt().putInt("dream_pickaxe_break_status_count", breakStatusCount);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.vxrofmods.dream_pickaxe.tooltip.shift"));
        } else {
            tooltip.add(Text.translatable("item.vxrofmods.dream_pickaxe.tooltip"));
        }
    }

    private boolean isValuableBlock(Block block) {
        return Registry.BLOCK.getOrCreateEntry(Registry.BLOCK.getKey(block).get()).isIn(ModTags.Blocks.DREAM_PICKAXE_DETECTABLE_BLOCKS);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient()) {
            ItemStack stack = user.getStackInHand(hand);

            if(stack.getItem().equals(this)) {

                boolean modeSetter;
                NbtCompound nbt = new NbtCompound();

                if(stack.hasNbt()) {
                    nbt = stack.getNbt();
                    assert nbt != null;
                    modeSetter = nbt.getBoolean("dream_pickaxe_mode_setter");
                } else {
                    modeSetter = false;
                    nbt.putBoolean("dream_pickaxe_mode_setter", modeSetter);
                }

                modeSetter = !modeSetter;

                if(modeSetter) {
                    user.sendMessage(Text.literal("§b3x3-Breaker activated§r"), true);
                } else {
                    user.sendMessage(Text.literal("§b3x3-Breaker deactivated§r"), true);
                }


                nbt.putBoolean("dream_pickaxe_mode_setter", modeSetter);
                stack.setNbt(nbt);

            }
        }
        return super.use(world, user, hand);
    }
}
