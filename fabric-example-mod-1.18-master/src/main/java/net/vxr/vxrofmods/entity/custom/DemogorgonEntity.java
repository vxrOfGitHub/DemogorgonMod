package net.vxr.vxrofmods.entity.custom;

import com.eliotlash.mclib.math.functions.classic.Mod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.vxr.vxrofmods.entity.ai.goal.custom.DemogorgonAttackGoal;
import net.vxr.vxrofmods.entity.variant.DemogorgonVariant;
import net.vxr.vxrofmods.sound.ModSounds;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DemogorgonEntity extends HostileEntity implements IAnimatable{
    private AnimationFactory factory = new AnimationFactory(this);

    private int dimensionDriftCooldown = 0;
    private int dimensionDriftMaxCooldown = 400;

    public DemogorgonEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    // Normal
    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 3.2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f);
    }
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(8, new WanderAroundPointOfInterestGoal(this, 0.6d, false));
        this.initCustomGoals();
    }
    protected void initCustomGoals() {
        this.goalSelector.add(2, new DemogorgonAttackGoal(this, 1.8, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(new Class[]{DemogorgonEntity.class}));
        this.targetSelector.add(2, new ActiveTargetGoal(this, PlayerEntity.class, false, false));
        this.targetSelector.add(3, new ActiveTargetGoal(this, MerchantEntity.class, true));
        this.targetSelector.add( 4, new ActiveTargetGoal(this, LivingEntity.class, true, true));
    }
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.demogorgon.walk", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.demogorgon.idle", true));
        return PlayState.CONTINUE;

    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        animationData.addAnimationController(new AnimationController(this, "attackController",
                0, this::attackPredicate));
    }

    private PlayState attackPredicate(AnimationEvent animationEvent) {
            if (this.handSwinging && animationEvent.getController().getAnimationState().equals(AnimationState.Stopped)) {
                animationEvent.getController().markNeedsReload();
                List<String> attackAnimationsList = new ArrayList<>();
                attackAnimationsList.add("animation.demogorgon.attack1");
                attackAnimationsList.add("animation.demogorgon.attack2");
                animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation(
                        attackAnimationsList.get(random.nextInt(attackAnimationsList.size())), false));
            }
        return PlayState.CONTINUE;
    }


    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", this.getTypeVariant());
        nbt.putInt("demogorgon.dimension_drift.cooldown", dimensionDriftCooldown);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
        this.dimensionDriftCooldown = nbt.getInt("demogorgon.dimension_drift.cooldown");
    }
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.DEMOGORGON_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.DEMOGORGON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.DEMOGORGON_DEATH;
    }
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
    }


    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DATA_ID_TYPE_VARIANT, 0);
    }

    // VARIANTS
    private static final TrackedData<Integer> DATA_ID_TYPE_VARIANT =
            DataTracker.registerData(DemogorgonEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty,
                                 SpawnReason spawnReason, @Nullable EntityData entityData,
                                 @Nullable NbtCompound entityNbt) {
        DemogorgonVariant variant = Util.getRandom(DemogorgonVariant.values(), this.random);
        setVariant(variant);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    public DemogorgonVariant getVariant() {
        return DemogorgonVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.dataTracker.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(DemogorgonVariant variant) {
        this.dataTracker.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    /*@Override
    public void tick() {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            double distanceToPlayer = player.getBlockPos().getSquaredDistance(this.getPos());
            if(distanceToPlayer <= 40) {
                this.dimensionDriftCooldown++;
                System.out.println("Du bei einem Demogorgon");
                if(this.dimensionDriftCooldown >= dimensionDriftMaxCooldown) {
                    this.dimensionDriftCooldown = 0;
                }
            }
        }
    } */
 }
