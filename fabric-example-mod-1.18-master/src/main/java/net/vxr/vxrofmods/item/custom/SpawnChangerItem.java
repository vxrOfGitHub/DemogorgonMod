package net.vxr.vxrofmods.item.custom;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpawnChangerItem extends Item {
    private static final Map<EntityType<? extends MobEntity>, SpawnChangerItem> SPAWN_CHANGER_TYPE = Maps.newIdentityHashMap();
    private EntityType<?> type = EntityType.SILVERFISH;

    public static List<EntityType> possibleEntityTypes = new ArrayList<>();

    public SpawnChangerItem(/*EntityType<? extends MobEntity> type,*/ Item.Settings settings) {
        super(settings);
        //this.type = type;
        SPAWN_CHANGER_TYPE.put((EntityType<? extends MobEntity>) type, this);
    }

    public static void addPossibleEntityTypes() {
        possibleEntityTypes.add(EntityType.SKELETON);
        possibleEntityTypes.add(EntityType.SLIME);
        possibleEntityTypes.add(EntityType.ALLAY);
        possibleEntityTypes.add(EntityType.WARDEN);
    }

    public void setEntityTypeOfSpawnChanger(EntityType<? extends MobEntity> type) {
        this.type = type;
        SPAWN_CHANGER_TYPE.put(type, this);
    }

    public EntityType<?> getEntityTypeOfSpawnChanger() {
        return this.type;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal(this.asItem().getName().toString() + " for " + this.type.getName().getString()));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (!world.isClient() && !(this.type.equals(EntityType.SILVERFISH))) {
            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(Blocks.SPAWNER)) {
                BlockEntity blockEntity = world.getBlockEntity(blockPos);
                if (blockEntity instanceof MobSpawnerBlockEntity) {
                    MobSpawnerLogic mobSpawnerLogic = ((MobSpawnerBlockEntity) blockEntity).getLogic();
                    mobSpawnerLogic.setEntityId(this.type);
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







