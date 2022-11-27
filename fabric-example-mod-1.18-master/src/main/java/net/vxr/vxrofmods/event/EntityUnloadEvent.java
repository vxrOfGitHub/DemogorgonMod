package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.vxr.vxrofmods.item.ModItems;
import net.vxr.vxrofmods.util.IEntityDataSaver;

public class EntityUnloadEvent implements ServerEntityEvents.Unload{
    @Override
    public void onUnload(Entity entity, ServerWorld world) {
        if(entity instanceof EnderDragonEntity dragon && !dragon.world.isClient()) {
            /*System.out.println("Would he drop Item: " + ((IEntityDataSaver) dragon).getPersistentData().getBoolean("vxrofmods_dragon_can_drop"));
            if(((IEntityDataSaver) dragon).getPersistentData().getBoolean("vxrofmods_dragon_can_drop")) {
                System.out.println("-----------Dragon Droppped Item");
                world.spawnEntity(new ItemEntity(world, dragon.getX(), dragon.getY(), dragon.getZ(), new ItemStack(ModItems.DRAGONS_CLAW)));
            } else {
                System.out.println("----------Dragon didn't drop Item");
            } */
            world.spawnEntity(new ItemEntity(world, dragon.getX(), dragon.getY(), dragon.getZ(), new ItemStack(ModItems.DRAGONS_CLAW)));
        }
    }
}
