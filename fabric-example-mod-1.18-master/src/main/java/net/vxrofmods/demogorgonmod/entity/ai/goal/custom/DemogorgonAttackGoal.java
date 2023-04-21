package net.vxrofmods.demogorgonmod.entity.ai.goal.custom;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
//import net.vxrofmods.demogorgonmod.entity.custom.DemogorgonEntity;

public class DemogorgonAttackGoal /* extends MeleeAttackGoal */{
   /* private final DemogorgonEntity demogorgon;
    private int ticks;

    public DemogorgonAttackGoal(PathAwareEntity pathAwareEntity, double speed, boolean pauseWhenMobIdle) {
        super(pathAwareEntity, speed, pauseWhenMobIdle);
        this.demogorgon = ((DemogorgonEntity) pathAwareEntity);
    }

    public void start() {
        super.start();
        this.ticks = 0;
    }

    public void stop() {
        super.stop();
        this.demogorgon.setAttacking(false);
    }

    public void tick() {
        super.tick();
        ++this.ticks;
        if (this.ticks >= 5 && this.getCooldown() < this.getMaxCooldown() / 2) {
            this.demogorgon.setAttacking(true);
        } else {
            this.demogorgon.setAttacking(false);
        }

    }
*/
}
