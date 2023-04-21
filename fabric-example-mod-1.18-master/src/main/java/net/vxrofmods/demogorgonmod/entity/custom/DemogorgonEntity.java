package net.vxrofmods.demogorgonmod.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.variant.DemogorgonVariant;
import net.vxrofmods.demogorgonmod.util.EntityUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

import static org.apache.commons.lang3.RandomUtils.*;

public class DemogorgonEntity extends HostileEntity implements GeoEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private int dimensionDriftCooldown = 0;
    private final int dimensionDriftMaxCooldown = 100;
    private final int dimensionDriftRadius = 20;
    private final int dimensionDriftDamage = 10;
    private int targetDamageDelayTick = 0;
    private boolean shoulPlayDimensionDriftSubmergeAnimation = false;
    private final int targetDamageMaxDelayTick = 60;
    private int dimensionDriftTargetID;
    private boolean hasDimensionDriftTarget = false;
    private boolean shouldSpawnDimensionDriftParticles = false;
    private static final float movementSpeed = 0.2f;
    private final ServerBossBar bossBar = (ServerBossBar)new ServerBossBar(this.getDisplayName(), BossBar.Color.RED, BossBar.Style.PROGRESS).setDarkenSky(true);

    public DemogorgonEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    // Normal
    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 6f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, movementSpeed);
    }
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 2D, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(8, new WanderAroundPointOfInterestGoal(this, 0.6d, false));

        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, false, false));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add( 4, new ActiveTargetGoal<>(this, LivingEntity.class, true, true));
    }


    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", this.getTypeVariant());
        nbt.putInt("demogorgon.dimension_drift.cooldown", dimensionDriftCooldown);
        nbt.putInt("demogorgon.dimension_drift.targetDamageDelayTick", targetDamageDelayTick);
        nbt.putInt("demogorgon.dimension_drift.target_id", dimensionDriftTargetID);
        nbt.putBoolean("demogorgon.dimension_drift.has_target", hasDimensionDriftTarget);
        nbt.putBoolean("demogorgon.dimension_drift.spawn_particles", shouldSpawnDimensionDriftParticles);
        nbt.putBoolean("demogorgon.dimension_drift.play_submerge_animation", shoulPlayDimensionDriftSubmergeAnimation);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
        this.dimensionDriftCooldown = nbt.getInt("demogorgon.dimension_drift.cooldown");
        this.targetDamageDelayTick = nbt.getInt("demogorgon.dimension_drift.targetDamageDelayTick");
        this.dimensionDriftTargetID = nbt.getInt("demogorgon.dimension_drift.target_id");
        this.hasDimensionDriftTarget = nbt.getBoolean("demogorgon.dimension_drift.has_target");
        this.shouldSpawnDimensionDriftParticles = nbt.getBoolean("demogorgon.dimension_drift.spawn_particles");
        this.shoulPlayDimensionDriftSubmergeAnimation = nbt.getBoolean("demogorgon.dimension_drift.play_submerge_animation");
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    /* @Override
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
    }*/

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

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {

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

    @Override
    public void tick() {

        MinecraftServer server = world.getServer();

        if(server != null) {
            BossBar bossBar = server.getBossBarManager().add(new Identifier(DemogorgonMod.MOD_ID  + "demogorgon_bossbar"),
                    Text.literal("Demogorgon"));
            bossBar.setColor(BossBar.Color.RED);
            bossBar.setPercent(this.getMaxHealth() / this.getHealth());
        }
        //if(!world.isClient()) {
            if (EntityUtil.areLivingEntitiesInRadiusOfEntity(this, dimensionDriftRadius) && !this.hasDimensionDriftTarget) {
                this.dimensionDriftCooldown++;
                //Get Dimension Drift Target & save it's ID
                if(this.dimensionDriftCooldown >= dimensionDriftMaxCooldown) {
                    Entity target = getTargetForDimensionDrift(this, dimensionDriftRadius);
                    if(target != null && target.isAlive()) {
                        this.dimensionDriftTargetID = target.getId();
                        this.hasDimensionDriftTarget = true;
                        System.out.println("Setting Animation to TRUE");
                        this.shoulPlayDimensionDriftSubmergeAnimation = true;
                        this.setMovementSpeed(0);
                    }
                }
            }
            this.attackEntitiesInRadius(/*dimensionDriftRadius,*/ dimensionDriftDamage);
        //}
        boolean shouldSpawnDDParticles = this.shouldSpawnDimensionDriftParticles;
        boolean hasDDTarget = this.hasDimensionDriftTarget;
        //System.out.println("Is Client World: " + world.isClient());
        //Spawn Particles
        if(shouldSpawnDDParticles && hasDDTarget) {
            //System.out.println("Is Client World: " + world.isClient());
            Entity target = world.getEntityById(this.dimensionDriftTargetID);
            World tWorld = target.getWorld();
            int particleCount = 20;
            double centerX = target.getX();
            double centerY = target.getY();
            double centerZ = target.getZ();
            double radius = 1;

            DustParticleEffect dustParticleEffect = new DustParticleEffect(new Vector3f(255,0,0), 1f);

            double increment = (2 * Math.PI) / particleCount;
            for(int o = 0; o < 5; o++) {
                for (double i = 0; i < particleCount; i++) {
                    double angle = i * increment * o;
                    double x = centerX + (radius * Math.cos(angle)) * nextDouble(0.0,1.0);
                    double y = centerY + ((float) this.targetDamageDelayTick) / 20;
                    double z = centerZ + (radius * Math.sin(angle))* nextDouble(0.0,1.0);
                    tWorld.addParticle(dustParticleEffect, x, y, z, 0, nextFloat(0.1F, 1F), 0);
                }
            }

            this.shouldSpawnDimensionDriftParticles = false;
        }
        super.tick();
    }

    private static Entity getTargetForDimensionDrift(Entity e, double radius) {
        List<Entity> possibleTargets = EntityUtil.getEntitiesBelowCertainPercentHealth(EntityUtil.getLivingEntitiesInRadius(e, radius), 2);
        if(possibleTargets.isEmpty()) {
            return null;
        }
        for (Entity target : possibleTargets) {
            if(target instanceof PlayerEntity) {
                return target;
            }
        }
        return possibleTargets.get(nextInt(0,possibleTargets.size()));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {

        if(this.hasDimensionDriftTarget) {
            target = null;
        }

        super.setTarget(target);
    }

    public void attackEntitiesInRadius(/*double radius,*/ float damage) {

        if(this.hasDimensionDriftTarget) {
            Entity target = world.getEntityById(this.dimensionDriftTargetID);
            this.targetDamageDelayTick++;
            if(this.targetDamageDelayTick <= targetDamageMaxDelayTick && target != null && target.isAlive()) {
                //target.world.addParticle(ParticleTypes.FLAME, target.getX(), target.getY(), target.getZ(), 0.0, 0.1, 0.0);
                if(this.targetDamageDelayTick % 4 == 0) {
                    this.shouldSpawnDimensionDriftParticles = true;
                }
            } else {
                this.shoulPlayDimensionDriftSubmergeAnimation = false;
                this.setMovementSpeed(movementSpeed);
                if(target != null && this.hasDimensionDriftTarget && target.isAlive()) {
                    this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    this.teleport(target.getX(), target.getY(), target.getZ());
                }
                this.dimensionDriftCooldown = 0;
                this.targetDamageDelayTick = 0;
                this.dimensionDriftTargetID = 0;
                this.hasDimensionDriftTarget = false;
            }
        }
    }


    @Override
    public boolean onKilledOther(ServerWorld world, LivingEntity other) {
        this.heal(this.getMaxHealth() / 20);
        return super.onKilledOther(world, other);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<T> tAnimationState) {

        if(this.shoulPlayDimensionDriftSubmergeAnimation) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon.dimension_drift_submerge", Animation.LoopType.HOLD_ON_LAST_FRAME));
        }
        else if(this.handSwinging) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon.attack1", Animation.LoopType.LOOP));
        }
        else if(tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon.walk", Animation.LoopType.LOOP));
        }
        else {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon.idle", Animation.LoopType.LOOP));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
