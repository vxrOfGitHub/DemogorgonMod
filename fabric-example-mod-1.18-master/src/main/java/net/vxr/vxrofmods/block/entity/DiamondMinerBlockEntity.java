package net.vxr.vxrofmods.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.vxr.vxrofmods.block.custom.DiamondMinerBlock;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.networking.ModMessages;
import net.vxr.vxrofmods.recipe.DiamondMinerRecipe;
import net.vxr.vxrofmods.screen.DiamondMinerScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DiamondMinerBlockEntity /*extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory*/ {

    /*private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(3, ItemStack.EMPTY);


    public ItemStack getRenderStack() {
        return this.getStack(0);
    }

    public void setInventory(DefaultedList<ItemStack> inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            this.inventory.set(i, inventory.get(i));
        }
    }

    @Override
    public void markDirty() {
        if(!world.isClient()) {
            PacketByteBuf data = PacketByteBufs.create();
            data.writeInt(inventory.size());
            for(int i = 0; i < inventory.size(); i++) {
                data.writeItemStack(inventory.get(i));
            }
            data.writeBlockPos(getPos());

            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                ServerPlayNetworking.send(player, ModMessages.ITEM_SYNC, data);
            }
        }
        super.markDirty();
    }

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 2000;
    private final int diamondMineTime = 72000;
    private final int ironMineTime = 24000;
    private final int goldMineTime = 24000;
    private final int emeraldMineTime = 48000;
    private final int copperMineTime = 20000;
    private final int redstoneMineTime = 16000;
    private final int lapisMineTime = 34000;

    //private int fuelTime = 0;
    //private int maxFuelTime = 0;

    public DiamondMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DIAMOND_MINER, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0: return DiamondMinerBlockEntity.this.progress;
                    case 1: return DiamondMinerBlockEntity.this.maxProgress;
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0: DiamondMinerBlockEntity.this.progress = value; break;
                    case 1: DiamondMinerBlockEntity.this.maxProgress = value; break;

                }
            }

            public int size() {
                return 2;
            }
        };
    }

    private static int getMineTimeFromItem (DiamondMinerBlockEntity entity) {
        if(entity.getStack(1).getItem().equals(ModItems.DIAMOND_FRAGMENT)) {
            return entity.diamondMineTime;
        }else if(entity.getStack(1).getItem().equals(ModItems.IRON_FRAGMENT)) {
            return entity.ironMineTime;
        }else if(entity.getStack(1).getItem().equals(ModItems.GOLD_FRAGMENT)) {
            return entity.goldMineTime;
        }else if(entity.getStack(1).getItem().equals(ModItems.EMERALD_FRAGMENT)) {
            return entity.emeraldMineTime;
        }else if(entity.getStack(1).getItem().equals(ModItems.COPPER_FRAGMENT)) {
            return entity.copperMineTime;
        }else if(entity.getStack(1).getItem().equals(ModItems.REDSTONE_FRAGMENT)) {
            return entity.redstoneMineTime;
        }else if(entity.getStack(1).getItem().equals(ModItems.LAPIS_LAZULI_FRAGMENT)) {
            return entity.lapisMineTime;
        } else {
            return 2000;
        }
    }

    private static int getMineTimeWithPickaxe (DiamondMinerBlockEntity entity) {
         if(entity.getStack(0).getItem().equals(Items.STONE_PICKAXE)) {
            return getMineTimeFromItem(entity) * 95 / 100;
        } else if(entity.getStack(0).getItem().equals(Items.IRON_PICKAXE)) {
            return getMineTimeFromItem(entity) * 90 / 100;
        } else if(entity.getStack(0).getItem().equals(Items.GOLDEN_PICKAXE)) {
            return getMineTimeFromItem(entity) * 80 / 100;
        } else if(entity.getStack(0).getItem().equals(Items.DIAMOND_PICKAXE)) {
            return getMineTimeFromItem(entity) * 80 / 100;
        } else if(entity.getStack(0).getItem().equals(Items.NETHERITE_PICKAXE)) {
            return getMineTimeFromItem(entity) * 70 / 100;
        } else if(entity.getStack(0).getItem().equals(ModItems.Dream_Pickaxe)) {
            return getMineTimeFromItem(entity) * 60 / 100;
        } else {
            return getMineTimeFromItem(entity);
        }

    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Ore Miner");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DiamondMinerScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("diamond_miner.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("diamond_miner.progress");
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, DiamondMinerBlockEntity entity) {
        if(world.isClient()) {
            return;
        }
        if(hasRecipe(entity)) {
            entity.maxProgress = getMineTimeWithPickaxe(entity);
            entity.progress++;
            markDirty(world, blockPos, state);
            if(entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }


    }



    private void resetProgress() {
        this.progress = 0;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {

        if (side == Direction.UP) {
                return slot == 1;
        }
        if (side == Direction.EAST || side == Direction.WEST || side == Direction.NORTH || side == Direction.SOUTH) {
            return slot == 0;
        }
            return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction side) {
        if(side == Direction.DOWN) {
            return slot == 2;
        }
        return false;
    }

    private int individualProgress;

    private static void craftItem(DiamondMinerBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        Optional<DiamondMinerRecipe> recipe = entity.getWorld().getRecipeManager()
                .getFirstMatch(DiamondMinerRecipe.Type.INSTANCE, inventory, entity.getWorld());
        if(hasRecipe(entity)) {

            entity.setStack(2, new ItemStack(recipe.get().getOutput().getItem(),
                    entity.getStack(2).getCount() + 1));
            damagePickaxeAfterCraft(inventory.getStack(1), inventory.getStack(0));

            entity.resetProgress();
        }
    }

    private static void damagePickaxeAfterCraft(ItemStack stackFragment, ItemStack stackPickaxe) {
        if(stackPickaxe.isDamageable()) {
            if(stackFragment.getItem().equals(ModItems.DIAMOND_FRAGMENT)) {
                stackPickaxe.setDamage(stackPickaxe.getDamage() + 20);
            } if(stackFragment.getItem().equals(ModItems.IRON_FRAGMENT)) {
                stackPickaxe.setDamage(stackPickaxe.getDamage() + 5);
            } if(stackFragment.getItem().equals(ModItems.GOLD_FRAGMENT)) {
                stackPickaxe.setDamage(stackPickaxe.getDamage() + 6);
            } if(stackFragment.getItem().equals(ModItems.EMERALD_FRAGMENT)) {
                stackPickaxe.setDamage(stackPickaxe.getDamage() + 8);
            } if(stackFragment.getItem().equals(ModItems.LAPIS_LAZULI_FRAGMENT)) {
                stackPickaxe.setDamage(stackPickaxe.getDamage() + 9);
            } if(stackFragment.getItem().equals(ModItems.COPPER_FRAGMENT)) {
                stackPickaxe.setDamage(stackPickaxe.getDamage() + 4);
            } if(stackFragment.getItem().equals(ModItems.REDSTONE_FRAGMENT)) {
                stackPickaxe.setDamage(stackPickaxe.getDamage() + 2);
            }
        }
         if(stackPickaxe.getDamage() >= stackPickaxe.getMaxDamage()) {
            stackPickaxe.setCount(stackPickaxe.getCount() - 1);
        }
    }

    private static boolean hasCorrectPickaxeInSlot(SimpleInventory inventory) {
        if(inventory.getStack(1).getItem().equals(ModItems.IRON_FRAGMENT) || inventory.getStack(1).getItem().equals(ModItems.COPPER_FRAGMENT) ||
                inventory.getStack(1).getItem().equals(ModItems.LAPIS_LAZULI_FRAGMENT)) {
            return inventory.getStack(0).getItem().equals(Items.IRON_PICKAXE) ||
                    inventory.getStack(0).getItem().equals(Items.STONE_PICKAXE) ||
                    inventory.getStack(0).getItem().equals(Items.DIAMOND_PICKAXE) ||
                    inventory.getStack(0).getItem().equals(Items.NETHERITE_PICKAXE) ||
                    inventory.getStack(0).getItem().equals(ModItems.Dream_Pickaxe);
            } else if(inventory.getStack(1).getItem().equals(ModItems.GOLD_FRAGMENT) || inventory.getStack(1).getItem().equals(ModItems.REDSTONE_FRAGMENT) ||
                inventory.getStack(1).getItem().equals(ModItems.EMERALD_FRAGMENT) || inventory.getStack(1).getItem().equals(ModItems.DIAMOND_FRAGMENT)) {
            return inventory.getStack(0).getItem().equals(Items.IRON_PICKAXE) ||
                    inventory.getStack(0).getItem().equals(Items.DIAMOND_PICKAXE) ||
                    inventory.getStack(0).getItem().equals(Items.NETHERITE_PICKAXE) ||
                    inventory.getStack(0).getItem().equals(ModItems.Dream_Pickaxe);
        } else {
            return false;
        }
    }

    private static boolean hasRecipe(DiamondMinerBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }
        boolean hasCorrectPickaxeInSlot = hasCorrectPickaxeInSlot(inventory);

        Optional<DiamondMinerRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(DiamondMinerRecipe.Type.INSTANCE, inventory, entity.getWorld());
        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory) && hasCorrectPickaxeInSlot
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput().getItem());

    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item output) {
        return inventory.getStack(2).getItem() == output || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }
*/

}
