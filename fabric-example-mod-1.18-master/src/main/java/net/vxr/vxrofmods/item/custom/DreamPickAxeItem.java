package net.vxr.vxrofmods.item.custom;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.vxr.vxrofmods.util.ModTags;

public class DreamPickAxeItem extends PickaxeItem {

    public DreamPickAxeItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        breakStatusCount = 0;
        if(pos.getY() - 1 > miner.getPos().getY() || pos.getY() < miner.getPos().getY()) {
            breakBlockUp(world, pos, state);
        }  else if(miner.getHorizontalFacing() == Direction.NORTH || miner.getHorizontalFacing() == Direction.SOUTH) {
            breakBlockNorth(world, pos, state);
        } else if(miner.getHorizontalFacing() == Direction.EAST || miner.getHorizontalFacing() == Direction.WEST) {
            breakBlockEast(world, pos, state);
        }

        stack.damage(breakStatusCount, miner, (e) -> {
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);});

        return super.postMine(stack, world, state, pos, miner);
    }

    private void breakBlockNorth(World world, BlockPos pos, BlockState state) {
        if(isValuableBlock(state.getBlock())) {
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
        }
    }

    private void breakBlockUp(World world, BlockPos pos, BlockState state) {
        if(isValuableBlock(state.getBlock())) {
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
        }
    }

    private void breakBlockEast(World world, BlockPos pos, BlockState state) {
        if(isValuableBlock(state.getBlock())) {
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
        }
    }

    private boolean isValuableBlock(Block block) {
        return Registry.BLOCK.getOrCreateEntry(Registry.BLOCK.getKey(block).get()).isIn(ModTags.Blocks.DREAM_PICKAXE_DETECTABLE_BLOCKS);
    }

    private static int breakStatusCount = 0;

}
