package net.vxr.vxrofmods.entity.ai.goal.custom;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.ZombieEntity;
import net.vxr.vxrofmods.entity.custom.DemogorgonEntity;

public class DemogorgonAttackGoal extends MeleeAttackGoal {
    private final DemogorgonEntity demogorgon;
    private int ticks;

    public DemogorgonAttackGoal(DemogorgonEntity demogorgon, double speed, boolean pauseWhenMobIdle) {
        super(demogorgon, speed, pauseWhenMobIdle);
        this.demogorgon = demogorgon;
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

}
