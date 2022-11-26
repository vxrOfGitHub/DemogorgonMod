package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.vxr.vxrofmods.item.ModItems;

public class EntityUnloadEvent implements ServerEntityEvents.Unload{
    @Override
    public void onUnload(Entity entity, ServerWorld world) {
        if(entity instanceof EnderDragonEntity) {
            world.spawnEntity(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ModItems.DRAGONS_TEETH)));
        }
    }
}
