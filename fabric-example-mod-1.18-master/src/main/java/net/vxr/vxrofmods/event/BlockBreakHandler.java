package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vxr.vxrofmods.WW2Mod;
import net.vxr.vxrofmods.item.ModItems;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class BlockBreakHandler implements PlayerBlockBreakEvents.After{
    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        luckOfTheSeedEnchantment(world, player, pos, state);
    }


    private void luckOfTheSeedEnchantment (World world, PlayerEntity player, BlockPos pos, BlockState state) {
        if(!world.isClient()) {
            System.out.println("-------after BlockBreak !world.isClient()");
            if(state.getBlock() instanceof CropBlock) {
                if(state.get(((CropBlock) state.getBlock()).getAgeProperty()) == ((CropBlock) state.getBlock()).getMaxAge()) {
                    System.out.println("-----Block was a CropBlock");
                    System.out.println("-----Stack in Hand has: " + player.getStackInHand(player.getActiveHand()).getEnchantments().size() + " Enchants");
                    ItemStack stack = player.getMainHandStack();
                    for(int i = 0; i < stack.getEnchantments().size(); i++) {
                        System.out.println("------Checking for Enchants");
                        System.out.println("-----Checking this Enchant: " + player.getMainHandStack().getEnchantments().get(i).toString());

                        if(stack.getEnchantments().get(i).toString().contains(WW2Mod.MOD_ID + ":luck_of_the_seed")) {
                            System.out.println("------found with correct Enchant");
                            if(stack.getEnchantments().get(i).toString().contains("lvl:3s")) {
                                System.out.println("------ Enchantment with Level 3");

                                if(state.getBlock().equals(Blocks.WHEAT)) {
                                    int x = nextInt(0, 5);
                                    for( int y = 0; y < x; y++) {
                                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                                new ItemStack(Items.WHEAT)));
                                    }
                                }else if(state.getBlock().equals(Blocks.BEETROOTS)) {
                                    int x = nextInt(0, 5);
                                    for( int y = 0; y < x; y++) {
                                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                                new ItemStack(Items.BEETROOT)));
                                    }
                                }
                                else {
                                    int x = nextInt(0,6);
                                    for(int y = 0; y < x; y++) {
                                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                                state.getBlock().getPickStack(world, pos, state)));
                                    }
                                }
                            }
                            if(stack.getEnchantments().get(i).toString().contains("lvl:2s")) {
                                System.out.println("------ Enchantment with Level 2");
                                if(state.getBlock().equals(Blocks.WHEAT)) {
                                    int x = nextInt(0, 4);
                                    for( int y = 0; y < x; y++) {
                                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                                new ItemStack(Items.WHEAT)));
                                    }
                                } else if(state.getBlock().equals(Blocks.BEETROOTS)) {
                                    int x = nextInt(0, 4);
                                    for( int y = 0; y < x; y++) {
                                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                                new ItemStack(Items.BEETROOT)));
                                    }
                                }
                                else {
                                    int x = nextInt(0,4);
                                    for(int y = 0; y < x; y++) {
                                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                                state.getBlock().getPickStack(world, pos, state)));
                                    }
                                }
                            }
                            if(stack.getEnchantments().get(i).toString().contains("lvl:1s")) {
                                System.out.println("------ Enchantment with Level 1");
                                if(state.getBlock().equals(Blocks.WHEAT)) {
                                    int x = nextInt(0, 3);
                                    for( int y = 0; y < x; y++) {
                                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                                new ItemStack(Items.WHEAT)));
                                    }
                                } else if(state.getBlock().equals(Blocks.BEETROOTS)) {
                                    int x = nextInt(0, 3);
                                    for( int y = 0; y < x; y++) {
                                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                                new ItemStack(Items.BEETROOT)));
                                    }
                                }
                                else {
                                    int x = nextInt(0,3);
                                    for(int y = 0; y < x; y++) {
                                        world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                                state.getBlock().getPickStack(world, pos, state)));
                                    }
                                }
                            }
                            break;
                        }
                    }
                }

            }
        }
    }
}
