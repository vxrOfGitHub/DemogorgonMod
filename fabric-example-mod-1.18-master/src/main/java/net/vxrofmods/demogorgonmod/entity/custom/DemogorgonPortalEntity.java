package net.vxrofmods.demogorgonmod.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.vxrofmods.demogorgonmod.util.DemogorgonData;
import net.vxrofmods.demogorgonmod.util.IEntityDataSaver;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class DemogorgonPortalEntity extends Entity implements GeoEntity {

    public DemogorgonPortalEntity(EntityType<?> type, World world) {
        super(type, world);
    }


    private int livedTicks = 0;
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final int idleAnimation = 0;
    private static final int openAnimation = 1;
    private static final int closeAnimation = 2;

    private boolean smallSizeFactor = false;

    @Override
    public void tick() {
        super.tick();

        this.livedTicks++;

        if(DemogorgonData.getPortalLivingTime(((IEntityDataSaver) this)) <= 0) {
            DemogorgonData.setPortalLivingTime(((IEntityDataSaver) this), 100);
        }

        if(this.livedTicks >= DemogorgonData.getPortalLivingTime(((IEntityDataSaver) this)) && this.getCurrentAnimation() != closeAnimation) {
            this.setCurrentAnimation(closeAnimation);
        }
        if(this.livedTicks >= DemogorgonData.getPortalLivingTime(((IEntityDataSaver) this)) + 30) {
            this.kill();
        }
        if(this.livedTicks >= 30 && this.getCurrentAnimation() == openAnimation) {
            this.setCurrentAnimation(idleAnimation);
        }
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ANIMATION_DATA_TRACKER, idleAnimation);
        this.dataTracker.startTracking(SMALL_SIZE_FACTOR_TRACKER, false);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(ANIMATION_DATA_TRACKER, nbt.getInt("current_animation"));
        this.dataTracker.set(SMALL_SIZE_FACTOR_TRACKER, nbt.getBoolean("small_size_factor"));
        this.livedTicks = nbt.getInt("demogorgon_portal.lived_ticks");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("current_animation", this.getCurrentAnimation());
        nbt.putBoolean("small_size_factor", this.getSmallSizeFactor());
        nbt.putInt("demogorgon_portal.lived_ticks", this.livedTicks);
    }

    private int getCurrentAnimation() {
        return this.dataTracker.get(ANIMATION_DATA_TRACKER);
    }

    private void setCurrentAnimation(int animation) {
        this.dataTracker.set(ANIMATION_DATA_TRACKER, animation);
    }

    public void setSmallSizeFactor(boolean smallSizeFactor) {
        this.dataTracker.set(SMALL_SIZE_FACTOR_TRACKER, smallSizeFactor);
    }
    public boolean getSmallSizeFactor() {
        return this.dataTracker.get(SMALL_SIZE_FACTOR_TRACKER);
    }

    private static final TrackedData<Integer> ANIMATION_DATA_TRACKER =
            DataTracker.registerData(DemogorgonPortalEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> SMALL_SIZE_FACTOR_TRACKER =
            DataTracker.registerData(DemogorgonPortalEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.setCurrentAnimation(openAnimation);
    }

    private Box getNewBoundingBox(float sizeFactor) {

        // Calculate the half-width and half-height of the hitbox
        float halfWidth = this.getWidth() * sizeFactor / 2.0f;
        float height = this.getHeight() * sizeFactor;

        // Calculate the position of the hitbox bounds based on the entity's position
        double minX = getX() - halfWidth;
        double minY = getY();
        double minZ = getZ() - halfWidth;
        double maxX = getX() + halfWidth;
        double maxY = getY() + height;
        double maxZ = getZ() + halfWidth;

        // Create and return the hitbox bounds
        return new Box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {

        if(this.getCurrentAnimation() == openAnimation) {
            if(this.getSmallSizeFactor()) {
                tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon_portal.open_small", Animation.LoopType.LOOP));
            }
            else {
                tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon_portal.open", Animation.LoopType.LOOP));
            }
        } else if (this.getCurrentAnimation() == closeAnimation) {
            if(this.getSmallSizeFactor()) {
                tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon_portal.close_small", Animation.LoopType.LOOP));
            } else {
                tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon_portal.close", Animation.LoopType.LOOP));
            }
        } else {
            if(this.getSmallSizeFactor()) {
                tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon_portal.idle_small", Animation.LoopType.LOOP));
            } else {
                tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon_portal.idle", Animation.LoopType.LOOP));
            }
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
