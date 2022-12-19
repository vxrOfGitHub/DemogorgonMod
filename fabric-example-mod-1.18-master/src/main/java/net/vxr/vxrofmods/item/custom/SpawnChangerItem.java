package net.vxr.vxrofmods.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SpawnChangerItem extends Item {
    //private static final Map<EntityType<? extends MobEntity>, SpawnChangerItem> SPAWN_CHANGER_TYPE = Maps.newIdentityHashMap();
    //private EntityType<?> type = EntityType.SILVERFISH;

    public static List<EntityType> possibleEntityTypes = new ArrayList<>();

    public SpawnChangerItem(/*EntityType<? extends MobEntity> type,*/ Item.Settings settings) {
        super(settings);
        //this.type = type;
        //SPAWN_CHANGER_TYPE.put((EntityType<? extends MobEntity>) type, this);
    }

    public static void addPossibleEntityTypesPreset() {
        possibleEntityTypes.add(EntityType.SKELETON);
        possibleEntityTypes.add(EntityType.SLIME);
        possibleEntityTypes.add(EntityType.ALLAY);
        possibleEntityTypes.add(EntityType.WARDEN);
    }

    public static void addPossibleEntityTypes(EntityType<?> type) {
        possibleEntityTypes.add(type);
    }

    public static void setEntityTypeOfSpawnChanger(ItemStack stack, int typeInList) {
        NbtCompound nbt = new NbtCompound();
        if(stack.hasNbt()) {
            nbt = stack.getNbt();
        }
        assert nbt != null;
        nbt.putInt("vxrofmods.spawn_changer", typeInList);
        stack.setNbt(nbt);
    }

    public static int getEntityTypeOfSpawnChanger(ItemStack stack) {
        if(stack.hasNbt()) {
            assert stack.getNbt() != null;
            return stack.getNbt().getInt("vxrofmods.spawn_changer");
        }
        return -1;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        assert stack.getNbt() != null;
        int typeInList = stack.getNbt().getInt("vxrofmods.spawn_changer");
        if(typeInList >= 0) {
            EntityType<?> type = possibleEntityTypes.get(typeInList);

            tooltip.add(Text.literal(stack.getName().getString() + " for " + type.getName().getString()));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!stack.hasNbt()) {
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("vxrofmods.spawn_changer", -1);
            stack.setNbt(nbt);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        ItemStack stack = context.getStack();
        int typeInList = stack.getNbt().getInt("vxrofmods.spawn_changer");

        if (!world.isClient() && typeInList != -1) {
            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(Blocks.SPAWNER)) {
                BlockEntity blockEntity = world.getBlockEntity(blockPos);
                if (blockEntity instanceof MobSpawnerBlockEntity) {
                    EntityType<?> type = possibleEntityTypes.get(typeInList);
                    MobSpawnerLogic mobSpawnerLogic = ((MobSpawnerBlockEntity) blockEntity).getLogic();
                    mobSpawnerLogic.setEntityId(type);
                    blockEntity.markDirty();
                    world.updateListeners(blockPos, blockState, blockState, 3);
                    world.emitGameEvent(context.getPlayer(), GameEvent.BLOCK_CHANGE, blockPos);
                    itemStack.decrement(1);
                    return ActionResult.CONSUME;
                }
            }

            return ActionResult.CONSUME;

        } else {
            return ActionResult.SUCCESS;
        }
    }
}







