package net.vxr.vxrofmods.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.item.inventory.ImplementedInventory;
import net.vxr.vxrofmods.screen.DiamondMinerScreenHandler;
import org.jetbrains.annotations.Nullable;

public class DiamondMinerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(3, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public DiamondMinerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DIAMOND_MINER, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0: return DiamondMinerBlockEntity.this.progress;
                    case 1: return DiamondMinerBlockEntity.this.maxProgress;
                    //case 2: return DiamondMinerBlockEntity.this.fuelTime;
                    //case 3: return DiamondMinerBlockEntity.this.maxFuelTime;
                    default: return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0: DiamondMinerBlockEntity.this.progress = value; break;
                    case 1: DiamondMinerBlockEntity.this.maxProgress = value; break;
                    //case 2: DiamondMinerBlockEntity.this.fuelTime = value; break;
                    //case 3: DiamondMinerBlockEntity.this.maxFuelTime = value; break;
                }
            }

            public int size() {
                return 2;
            }
        };
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
        nbt.putInt("ore_miner.progress", progress);
        //nbt.putInt("miner.fuelTime", fuelTime);
        //nbt.putInt("miner.maxFuelTime", maxFuelTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("ore_miner.progress");
        //fuelTime = nbt.getInt("miner.fuelTime");
        //maxFuelTime = nbt.getInt("miner.maxFuelTime");
    }

   /* private void consumeFuel() {
        if(!getStack(0).isEmpty()) {
            this.fuelTime = FuelRegistry.INSTANCE.get(this.removeStack(0, 1).getItem());
            this.maxFuelTime = this.fuelTime;
        }
    }

     public static void tick(World world, BlockPos pos, BlockState state, DiamondMinerBlockEntity entity) {
        if(isConsumingFuel(entity)) {
            entity.fuelTime--;
        }

        if(hasRecipe(entity)) {
            if(hasFuelInFuelSlot(entity) && !isConsumingFuel(entity)) {
                entity.consumeFuel();
            }
            if(isConsumingFuel(entity)) {
                entity.progress++;
                if(entity.progress > entity.maxProgress) {
                    craftItem(entity);
                }
            }
        } else {
            entity.resetProgress();
        }
    } */

    public static void tick(World world, BlockPos blockPos, BlockState state, DiamondMinerBlockEntity entity) {
        if(world.isClient()) {
            return;
        }

        if(hasRecipe(entity)) {
            entity.progress++;
            markDirty(world,blockPos, state);
            if(entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }

    }

    /* private static boolean hasFuelInFuelSlot(DiamondMinerBlockEntity entity) {
        return !entity.getStack(0).isEmpty();
    }

    private static boolean isConsumingFuel(DiamondMinerBlockEntity entity) {
        return entity.fuelTime > 0;
    }

    private static boolean hasRecipe(DiamondMinerBlockEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<DiamondMinerRecipe> match = world.getRecipeManager()
                .getFirstMatch(DiamondMinerRecipe.Type.INSTANCE, inventory, world);

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput());
    } */

    private static boolean hasRecipe(DiamondMinerBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        boolean hasDiamondFragmentInFirstSlot = entity.getStack(1).getItem() == ModItems.DIAMOND_FRAGMENT;

        return hasDiamondFragmentInFirstSlot && canInsertAmountIntoOutputSlot(inventory, 1)
                && canInsertItemIntoOutputSlot(inventory, Items.DIAMOND);
    }

    /* private static void craftItem(DiamondMinerBlockEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<DiamondMinerRecipe> match = world.getRecipeManager()
                .getFirstMatch(DiamondMinerRecipe.Type.INSTANCE, inventory, world);

        if(match.isPresent()) {
            entity.removeStack(1,1);
            entity.setStack(2, new ItemStack(match.get().getOutput().getItem(),
                    entity.getStack(2).getCount() + 1));

            entity.resetProgress();
        }
    } */

    private static void craftItem(DiamondMinerBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        if(hasRecipe(entity)) {
            entity.removeStack(1, 1);

            entity.setStack(2, new ItemStack(Items.DIAMOND,
                    entity.getStack(2).getCount() + 1));

            entity.resetProgress();
        }

    }



    private void resetProgress() {
        this.progress = 0;
    }

    /* private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, ItemStack output) {
        return inventory.getStack(2).getItem() == output.getItem() || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    } */

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item output) {
        return inventory.getStack(2).getItem() == output || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory, int count) {
        return  inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount() + count;
    }


}
