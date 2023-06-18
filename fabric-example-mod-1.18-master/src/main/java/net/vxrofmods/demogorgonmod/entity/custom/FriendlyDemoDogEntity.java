package net.vxrofmods.demogorgonmod.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import net.vxrofmods.demogorgonmod.sound.ModSounds;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class FriendlyDemoDogEntity extends WolfEntity implements GeoEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final TrackedData<Boolean> SITTING =
            DataTracker.registerData(FriendlyDemoDogEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> TICKS_SINCE_LAST_ATTACK =
            DataTracker.registerData(FriendlyDemoDogEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public FriendlyDemoDogEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6f);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("isSitting", this.dataTracker.get(SITTING));
        nbt.putInt("ticks_since_last_attack", this.dataTracker.get(TICKS_SINCE_LAST_ATTACK));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(SITTING, nbt.getBoolean("isSitting"));
        this.dataTracker.set(TICKS_SINCE_LAST_ATTACK, nbt.getInt("ticks_since_last_attack"));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING,false);
        this.dataTracker.startTracking(TICKS_SINCE_LAST_ATTACK,0);
    }

    public void setTicksSinceLastAttack(int ticksSinceLastAttack) {
        this.dataTracker.set(TICKS_SINCE_LAST_ATTACK, ticksSinceLastAttack);
    }

    private int getTicksSinceLastAttack() {
        return this.dataTracker.get(TICKS_SINCE_LAST_ATTACK);
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    @Override
    public void tick() {
        super.tick();
        this.setTicksSinceLastAttack(this.getTicksSinceLastAttack() + 1);
        if(this.getTicksSinceLastAttack() >= 40) {
            this.setTicksSinceLastAttack(39);
        }
        if(this.getOwner() == null || !this.getOwner().isAlive()) {
            System.out.println("No Owner");
            //this.kill();
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {

        ItemStack stack = player.getStackInHand(hand);

        if(!this.isTamed() || this.getOwner() == null) {
            if(stack.isOf(Items.BONE)) {
                this.setTamed(true);
                this.setOwner(player);
                if(!player.isCreative()) {
                    stack.decrement(1);
                    return ActionResult.CONSUME;
                }
            }
        }
        if(this.getOwner() == player) {
            this.setSit(!this.isSitting());
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean canBreedWith(AnimalEntity other) {

        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public void onAttacking(Entity target) {
        super.onAttacking(target);
        this.setTicksSinceLastAttack(0);
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {

        if(this.isSitting()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demodog.sit", Animation.LoopType.LOOP));
        }
        else if(this.getTicksSinceLastAttack() <= 14) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demodog.attack", Animation.LoopType.LOOP));
        }
        else if(tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demodog.walk", Animation.LoopType.LOOP));
        }
        else {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demodog.idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 1.0F, 1.0F);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.DEMOGORGON_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {

        return ModSounds.DEMOGORGON_HURT_SOUND;
    }

    @Override
    protected SoundEvent getDeathSound() {

        return SoundEvents.ENTITY_WOLF_DEATH;

    }

    @Override
    public EntityView method_48926() {
        return super.getWorld();
    }
}
