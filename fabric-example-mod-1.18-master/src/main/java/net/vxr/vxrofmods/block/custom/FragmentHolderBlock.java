package net.vxr.vxrofmods.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vxr.vxrofmods.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentHolderBlock extends Block {
    public static final BooleanProperty SWAPPED = BooleanProperty.of("swapped");

    public FragmentHolderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {


        if(!world.isClient() && hand == Hand.MAIN_HAND) {

            ItemStack itemStack = player.getStackInHand(hand);
            Item item = Items.EMERALD_BLOCK;

            if(itemStack.getItem() == item && state.get(SWAPPED)) {
                player.sendMessage(Text.literal("SWAPPED"), false);

                world.setBlockState(pos, state.with(SWAPPED, false), Block.NOTIFY_ALL);
                randomFragment(itemStack, player, world);


            } if(itemStack.getItem() != item  && state.get(SWAPPED)) {
                player.sendMessage(Text.literal("STOLEN"), false);

                world.breakBlock(pos, false);

                breakBlocks(world, pos);
                randomFragment(itemStack, player, world);

            } if(itemStack.getItem() != item && !state.get(SWAPPED)) {
                player.sendMessage(Text.literal("SWAPPED STOLEN"), false);

                world.breakBlock(pos, false);

                breakBlocks(world, pos);
                player.dropItem(item);
            }
        }


        return ActionResult.SUCCESS;
    }


    private static void randomFragment(ItemStack itemStack, PlayerEntity player, World world) {
        Random random = new Random();
        List<Item> fragmentList = new ArrayList<>(); // No need to define length
        fragmentList.add(ModItems.DIAMOND_FRAGMENT);
        fragmentList.add(ModItems.IRON_FRAGMENT);
        fragmentList.add(ModItems.EMERALD_FRAGMENT);
        fragmentList.add(ModItems.REDSTONE_FRAGMENT);
        fragmentList.add(ModItems.GOLD_FRAGMENT);
        fragmentList.add(ModItems.LAPIS_LAZULI_FRAGMENT);
        fragmentList.add(ModItems.COPPER_FRAGMENT);
        player.dropItem(fragmentList.get(random.nextInt(fragmentList.size())));

    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos, state.with(SWAPPED, true), Block.NOTIFY_ALL);
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    private void breakBlocks(World world, BlockPos pos) {
        BlockPos pos1 = pos.down(2);
        world.breakBlock(pos1, true);
        world.breakBlock(pos1.north(), true);
        world.breakBlock(pos1.south(), true);
        world.breakBlock(pos1.west(), true);
        world.breakBlock(pos1.east(), true);
        world.breakBlock(pos1.north(2), true);
        world.breakBlock(pos1.south(2), true);
        world.breakBlock(pos1.west(2), true);
        world.breakBlock(pos1.east(2), true);
        breakQuarterBlocks(world, pos1.north().east());
        breakQuarterBlocks(world, pos1.north().west(2));
        breakQuarterBlocks(world, pos1.south(2).east());
        breakQuarterBlocks(world, pos1.south(2).west(2));

    }

    private void breakQuarterBlocks(World world, BlockPos pos) {
        world.breakBlock(pos.north(), true);
        world.breakBlock(pos.east(), true);
        world.breakBlock(pos.north().east(), true);
        world.breakBlock(pos, true);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SWAPPED);
    }

}
