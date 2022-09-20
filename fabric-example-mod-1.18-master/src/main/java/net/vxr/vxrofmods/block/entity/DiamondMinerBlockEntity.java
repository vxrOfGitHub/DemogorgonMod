package net.vxr.vxrofmods.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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

import java.util.Optional;

public class DiamondMinerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(3, ItemStack.EMPTY);


    public ItemStack getRenderStack() {
        if(this.getStack(2).isEmpty()) {
            return  this.getStack(1);
        } else {
            return this.getStack(2);
        }
    }

    public void setInventory(DefaultedList<ItemStack> list) {
        for(int i = 0; 0 < inventory.size(); i++) {
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
    private int maxProgress = 72;
    //private int fuelTime = 0;
    //private int maxFuelTime = 0;

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
        Direction localDir = this.getWorld().getBlockState(this.pos).get(DiamondMinerBlock.FACING);

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

            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(DiamondMinerBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for(int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<DiamondMinerRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(DiamondMinerRecipe.Type.INSTANCE, inventory, entity.getWorld());

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput().getItem());

    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item output) {
        return inventory.getStack(2).getItem() == output || inventory.getStack(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(2).getMaxCount() > inventory.getStack(2).getCount();
    }



}
