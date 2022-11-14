package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class OnStopSleepingHandler implements EntitySleepEvents.StopSleeping {
    @Override
    public void onStopSleeping(LivingEntity entity, BlockPos sleepingPos) {
        World world = entity.getWorld();
        System.out.println("Is it Day? " + world.isDay());
        if(entity.getStatusEffects().size() > 0 && world.isDay()) {
            System.out.println("Has Effects and its Day");
            List<StatusEffect> negativeEffects = new ArrayList<>();
            negativeEffects.add(StatusEffects.BLINDNESS);
            negativeEffects.add(StatusEffects.MINING_FATIGUE);
            negativeEffects.add(StatusEffects.HUNGER);
            negativeEffects.add(StatusEffects.NAUSEA);
            negativeEffects.add(StatusEffects.POISON);
            negativeEffects.add(StatusEffects.SLOWNESS);
            negativeEffects.add(StatusEffects.WEAKNESS);
            negativeEffects.add(StatusEffects.WITHER);
            for(int i = 0; i < negativeEffects.size(); i++) {
                entity.removeStatusEffect(negativeEffects.get(i));
            }
        }
    }
}
