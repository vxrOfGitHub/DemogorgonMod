package net.vxr.vxrofmods.mixin;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class ModSpawnerOnPlaced {

    @Inject(method = "onPlaced", at = @At("HEAD"))
    protected void injectOnPlacedMethod(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if(!world.isClient() && state.isOf(Blocks.SPAWNER) && placer instanceof PlayerEntity player) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MobSpawnerBlockEntity) {
                EntityType<?> type = EntityType.SILVERFISH;
                MobSpawnerLogic mobSpawnerLogic = ((MobSpawnerBlockEntity) blockEntity).getLogic();
                mobSpawnerLogic.setEntityId(type);
                blockEntity.markDirty();
                world.updateListeners(pos, state, state, 3);
                world.emitGameEvent(placer, GameEvent.BLOCK_CHANGE, pos);
            }
        }
    }
}
