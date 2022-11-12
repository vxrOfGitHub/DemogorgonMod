package net.vxr.vxrofmods.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.vxr.vxrofmods.item.ModItems;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MoritzDragonAvatarEntity2 extends TameableEntity implements IAnimatable {
    private boolean Dancing = false;

    public boolean isDancing() {
        return Dancing;
    }

    public void setDancing(boolean dancing) {
        Dancing = dancing;
    }

    private AnimationFactory factory = new AnimationFactory(this);

    public MoritzDragonAvatarEntity2(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onTamedChanged() {
        if(isTamed()) {
            this.setInvulnerable(true);
        } else {
            this.setInvulnerable(false);
        }
        super.onTamedChanged();
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return TameableEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 999999999.999999999999999999D * 99999999)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(2, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(3, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 10));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(this.isDancing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.moritz_dragon.flying", true));
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.moritz_dragon.flying", true));
            return PlayState.CONTINUE;
        }
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.moritz_dragon.flying", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.moritz_dragon.flying", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDER_DRAGON_FLAP;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ENDER_DRAGON_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDER_DRAGON_DEATH;
    }

    // Tameable Entity
    private static final TrackedData<Boolean> SITTING =
            DataTracker.registerData(MoritzDragonAvatarEntity2.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getStackInHand(hand);
        Item item = itemstack.getItem();
        Item thisEntityInInventory = ModItems.MORITZ_DRAGON_HELMET;
        ItemStack thisEntityInInventoryStack = new ItemStack(thisEntityInInventory);

        Item itemForTaming = Items.COD;


        if (item == itemForTaming && !isTamed()) {
            if (this.world.isClient()) {
                return ActionResult.CONSUME;
            } else {
                if (!player.getAbilities().creativeMode) {
                    itemstack.decrement(1);
                }

                if (!this.world.isClient()) {
                    super.setOwner(player);
                    this.navigation.recalculatePath();
                    this.setTarget(null);
                    this.world.sendEntityStatus(this, (byte)7);
                    setSit(true);
                }

                return ActionResult.SUCCESS;
            }
        }

        if(isTamed() && !this.world.isClient() && hand == Hand.MAIN_HAND) {
            setSit(!isSitting());
            return ActionResult.SUCCESS;
        }

        if(isTamed() && !this.world.isClient() && itemstack.getItem() == Items.CARROT && player.isInSneakingPose() && !this.isDancing()) {
            this.setDancing(true);
            return ActionResult.SUCCESS;
        }
        if(isTamed() && !this.world.isClient() && player.isInSneakingPose() && this.isDancing()) {
            this.setDancing(false);
            return ActionResult.SUCCESS;
        }

        if (isTamed() && !this.world.isClient() && player.isInSneakingPose() && itemstack.getItem() != Items.CARROT && !this.isDancing()) {
            player.giveItemStack(thisEntityInInventoryStack);
            this.discard();
            return ActionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(999999999.999999999999999999D * 99999999);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(10f);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue((double)0.4f);
        } else {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(30.0D);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2D);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue((double)0.3f);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("isSitting", this.dataTracker.get(SITTING));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(SITTING, nbt.getBoolean("isSitting"));
    }

    @Override
    public AbstractTeam getScoreboardTeam() {
        return super.getScoreboardTeam();
    }

    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING,false);
    }
}
