package net.vxr.vxrofmods.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
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
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.vxr.vxrofmods.item.ModItems;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class MichelWolfAvatarEntity extends TameableEntity implements IAnimatable {

    private static final Item TamingItem = Items.BONE;
    private static final Item ArmorItem = ModItems.MICHEL_WOLF_HELMET;

    private static final boolean hasDanceAnimation = true;
    private static final String dancingAnimation = "animation.michel_wolf.dance";
    private static final String sittingAnimation = "animation.michel_wolf.sitting";
    private static final String walkAnimation = "animation.michel_wolf.walking";
    private static final String idleAnimation = "animation.michel_wolf.standing";

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public MichelWolfAvatarEntity(EntityType<? extends TameableEntity> entityType, World world) {
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

        if(hasDanceAnimation && this.isDancing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(dancingAnimation, ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(walkAnimation, ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(sittingAnimation, ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation(idleAnimation, ILoopType.EDefaultLoopTypes.LOOP));
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
        return SoundEvents.ENTITY_WOLF_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    // Tameable Entity
    private static final TrackedData<Boolean> SITTING =
            DataTracker.registerData(MichelWolfAvatarEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Boolean> DANCING =
            DataTracker.registerData(MichelWolfAvatarEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    private static boolean musicIsPlayingNear(List<BlockState> nearJukeboxes, MichelWolfAvatarEntity entity) {
        boolean musicIsPlayingNear = false;
        if(!entity.world.isClient() && nearJukeboxes.size() > 0) {
            for(int i = 0; i < nearJukeboxes.size(); i++) {
                musicIsPlayingNear = ((Boolean) nearJukeboxes.get(i).get(JukeboxBlock.HAS_RECORD));
            }
        }
        return musicIsPlayingNear;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getStackInHand(hand);
        Item item = itemstack.getItem();
        ItemStack armorItemStack = new ItemStack(ArmorItem);

        if (item == TamingItem && !isTamed()) {
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
            if(hasDanceAnimation) {
                if(isSitting() && !isDancing()) {
                    setDancing(true);
                } else if (isDancing()) {
                    setSit(false);
                    setDancing(false);
                } else if (!isSitting()) {
                    setSit(true);
                }
                return ActionResult.SUCCESS;
            } else {
                setSit(!this.isSitting());
            }
        }



        if (isTamed() && !this.world.isClient() && player.isInSneakingPose()) {
            if(this.getCustomName() != null){
                System.out.println("----------- Penguin Name = " + this.getName().getString() + "------------");
                armorItemStack = this.addNbtToHelmet(player, this.getName().getString(), armorItemStack);
            } else {
                System.out.println("-------- Hatte keinen Namen!!! -------------");
            }
            player.giveItemStack(armorItemStack);
            this.discard();
            return ActionResult.SUCCESS;
        }

        if (itemstack.getItem() == TamingItem) {
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }

    private ItemStack addNbtToHelmet(PlayerEntity player, String nameOfAvatar, ItemStack avatarHelmet) {
        if(!avatarHelmet.isEmpty()) {
            NbtCompound nbtData = new NbtCompound();
            nbtData.putString("vxrofmods.avatar_name", nameOfAvatar);
            if(avatarHelmet.getNbt() != null) {
                System.out.println("----- Name des Avatars nach NBT: " + avatarHelmet.getNbt().getString("vxrofmods.avatar_name") + " ------------");
                System.out.println("----- Der Avatar Helm ist ein: " + avatarHelmet + " ------------");
            }

            avatarHelmet.setNbt(nbtData);
            avatarHelmet.setCustomName(Text.literal(avatarHelmet.getNbt().getString("vxrofmods.avatar_name")));
            return avatarHelmet;
        }
        return avatarHelmet;
    }

    public void setSit(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
        super.setSitting(sitting);
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    public void setDancing(boolean dancing) {
        this.dataTracker.set(DANCING, dancing);
    }

    public boolean isDancing() {

        return this.dataTracker.get(DANCING) && hasDanceAnimation;
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(999999999.999999999999999999D * 99999999);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(10f);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue((double)0.4f);
            this.setInvulnerable(true);
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
        nbt.putBoolean("isDancing", this.dataTracker.get(DANCING));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(SITTING, nbt.getBoolean("isSitting"));
        this.dataTracker.set(DANCING, nbt.getBoolean("isDancing"));
    }

    @Override
    public AbstractTeam getScoreboardTeam() {
        return super.getScoreboardTeam();
    }

    public boolean canBeLeashedBy(PlayerEntity player) {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SITTING,false);
        this.dataTracker.startTracking(DANCING,false);
    }
}
