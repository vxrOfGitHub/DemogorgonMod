package net.vxrofmods.demogorgonmod.entity.custom;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.variant.DemogorgonVariant;
import net.vxrofmods.demogorgonmod.networking.ModMessages;
import net.vxrofmods.demogorgonmod.sound.ModSounds;
import net.vxrofmods.demogorgonmod.util.DemogorgonData;
import net.vxrofmods.demogorgonmod.util.EntityUtil;
import net.vxrofmods.demogorgonmod.util.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
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
    private final int dimensionDriftMaxCooldown = 250;
    private final int dimensionDriftRadius = 20;
    private final int dimensionDriftDamage = 10;
    private final double syncToClientsRadius = 256;
    private int targetDamageDelayTick = 0;
    private final int targetDamageMaxDelayTick = 200;
    private int dimensionDriftTargetID;
    private boolean hasDimensionDriftTarget = false;
    private boolean shouldSpawnDimensionDriftParticles = false;
    private static final float movementSpeed = 0.2f;
    private int ticksSinceLastAttack = 0;
    private double slownessPerTickToDDTarget = 0;

    private int currentAnimation;
    private final int idleAnimation = 0;
    private final int walkAnimation = 1;
    private final int attack1Animation = 2;
    private final int attack2Animation = 3;
    private final int ddSubmergeAnimation = 4;
    private final int ddEmergeAnimation = 5;
    private final int ddAttack1Animation = 6;

    public static final int timedSoundsCount = 1;

    private final ServerBossBar bossBar = (ServerBossBar)new ServerBossBar(this.getDisplayName(), BossBar.Color.RED, BossBar.Style.PROGRESS).setDarkenSky(true);

    public DemogorgonEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
    // Normal
    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, movementSpeed);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new RevengeGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 2d, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        if(this.isInDDAnimation()) {
            this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
            this.goalSelector.add(8, new LookAroundGoal(this));
        }
        this.goalSelector.add(8, new WanderAroundPointOfInterestGoal(this, movementSpeed, false));

        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, false, false));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add( 4, new ActiveTargetGoal<>(this, LivingEntity.class, true, true));
    }


    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", this.getTypeVariant());
        nbt.putInt("demogorgon.animation", this.currentAnimation);
        nbt.putInt("demogorgon.dimension_drift.cooldown", dimensionDriftCooldown);
        nbt.putInt("demogorgon.dimension_drift.targetDamageDelayTick", targetDamageDelayTick);
        nbt.putInt("demogorgon.dimension_drift.target_id", dimensionDriftTargetID);
        nbt.putInt("demogorgon.ticks_since_last_attack", ticksSinceLastAttack);
        nbt.putDouble("demogorgon.slowness_per_tick_to_dd_target", slownessPerTickToDDTarget);
        nbt.putBoolean("demogorgon.dimension_drift.has_target", hasDimensionDriftTarget);
        nbt.putBoolean("demogorgon.dimension_drift.spawn_particles", shouldSpawnDimensionDriftParticles);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
        this.currentAnimation = nbt.getInt("demogorgon.animation");
        this.dimensionDriftCooldown = nbt.getInt("demogorgon.dimension_drift.cooldown");
        this.targetDamageDelayTick = nbt.getInt("demogorgon.dimension_drift.targetDamageDelayTick");
        this.dimensionDriftTargetID = nbt.getInt("demogorgon.dimension_drift.target_id");
        this.ticksSinceLastAttack = nbt.getInt("demogorgon.ticks_since_last_attack");
        this.slownessPerTickToDDTarget = nbt.getDouble("demogorgon.slowness_per_tick_to_dd_target");
        this.hasDimensionDriftTarget = nbt.getBoolean("demogorgon.dimension_drift.has_target");
        this.shouldSpawnDimensionDriftParticles = nbt.getBoolean("demogorgon.dimension_drift.spawn_particles");
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
    public void onDeath(DamageSource damageSource) {
        Entity target = world.getEntityById(this.dimensionDriftTargetID);
        if(!world.isClient()) {
            if(target instanceof ServerPlayerEntity serverPlayer) {
                DemogorgonData.writeTargetToDDTargetNBT(((IEntityDataSaver) target), false);
                syncNbtToDDTarget(serverPlayer, false);
            }
            if(target instanceof LivingEntity living) {
                EntityAttributeInstance movementSpeed = living.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if(movementSpeed != null) {
                    movementSpeed.setBaseValue(((IEntityDataSaver) target).getPersistentData().getDouble("normal_movement_speed"));
                }
            }
        }
        super.onDeath(damageSource);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        this.ticksSinceLastAttack++;
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

    @Override
    protected SoundEvent getAmbientSound() {
        if(this.isInDDAnimation()) {
            return ModSounds.DEMOGORGON_IDLE;
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if(this.isInDDAnimation()) {
            return ModSounds.DEMOGORGON_HURT_SOUND;
        }
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        if(this.isInDDAnimation()) {
            return ModSounds.DEMOGORGON_DEATH;
        }
        return null;
    }


    @Override
    public void onAttacking(Entity target) {

        this.ticksSinceLastAttack = 0;

        super.onAttacking(target);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if(this.isInDDAnimation()) {
            this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
        }
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DATA_ID_TYPE_VARIANT, 0);
        this.dataTracker.startTracking(ANIMATION, idleAnimation);
    }

    public void setAnimation(int animation) {
        this.currentAnimation = animation;
        System.out.println("Animation: " + animation);
        System.out.println("currentAnimation: " + this.currentAnimation);
        this.dataTracker.set(ANIMATION, currentAnimation);
    }

    public int getAnimation() {
        return this.dataTracker.get(ANIMATION);
    }

    // VARIANTS Datatracker
    private static final TrackedData<Integer> DATA_ID_TYPE_VARIANT =
            DataTracker.registerData(DemogorgonEntity.class, TrackedDataHandlerRegistry.INTEGER);

    // Animation Datatracker
    public static final TrackedData<Integer> ANIMATION = DataTracker.registerData(DemogorgonEntity.class, TrackedDataHandlerRegistry.INTEGER);


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
    public void move(MovementType movementType, Vec3d movement) {

        if(this.isInDDAnimation()) {
            super.move(movementType, movement);
        }
    }

    /*private void syncPlayDimensionDriftSubmergeAnimation(boolean shouldPlay, double radiusOfPlayerSyncedTo) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(this.getId());
        buf.writeBoolean(shouldPlay);

        List<Entity> entitiesInRadius = EntityUtil.getLivingEntitiesInRadius(this, radiusOfPlayerSyncedTo);
        for(Entity entity : entitiesInRadius) {
            if(entity instanceof ServerPlayerEntity player) {
                ServerPlayNetworking.send(player, ModMessages.DEMOGORGON_PLAY_ANIMATION_SYNC_ID, buf);
            }
        }
    }*/

    private void syncNbtToDDTarget(ServerPlayerEntity player , boolean isTarget) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(player.getId());
        buf.writeBoolean(isTarget);

        ServerPlayNetworking.send(player, ModMessages.GET_DD_TARGET_IN_NBT_SYNC_ID, buf);
    }


    @Override
    public void onPlayerCollision(PlayerEntity player) {

        if(this.isInDDAnimation()) {
            super.onPlayerCollision(player);
        }
    }

    @Override
    public void tick() {
        super.tick();

        updateBossbar();
        checkForDDTarget();
        setInvulnerability();
        playTimedSounds();

    }

    private void playTimedSounds() {

        IEntityDataSaver saver = ((IEntityDataSaver) this);

        if(world.isClient()) {

            if(this.getAnimation() == ddAttack1Animation) {
                if(!DemogorgonData.getPlayedDDAttack1Sound(saver)) {
                    world.playSound(this.getX(), this.getY(), this.getZ(), ModSounds.DEMOGORGON_SCREAM_1_SOUND, SoundCategory.HOSTILE, 1f, 1f, true);
                    DemogorgonData.writePlayedDDAttack1Sound(saver, true);
                }
            } else {
                DemogorgonData.writePlayedDDAttack1Sound(saver, false);
            }


        }

    }

    private void setInvulnerability() {
        if(!world.isClient()) {
            if(isInDDAnimation()) {
                this.setInvulnerable(true);
            }
        }
    }

    private boolean isInDDAnimation() {
        return this.getAnimation() == ddSubmergeAnimation || this.getAnimation() == ddEmergeAnimation || this.getAnimation() == ddAttack1Animation;
    }

    private void checkForDDTarget() {
        //if(!world.isClient()) {
        if (EntityUtil.areLivingEntitiesInRadiusOfEntity(this, dimensionDriftRadius) && !this.hasDimensionDriftTarget) {
            this.dimensionDriftCooldown++;
            //Get Dimension Drift Target & save it's ID
            if(this.dimensionDriftCooldown >= dimensionDriftMaxCooldown && this.ticksSinceLastAttack >= 40) {
                Entity target = getTargetForDimensionDrift(this, dimensionDriftRadius);
                if(target != null && target.isAlive() && target instanceof LivingEntity livingTarget) {
                    this.dimensionDriftTargetID = target.getId();
                    this.hasDimensionDriftTarget = true;
                    DemogorgonData.setPlayDimensionDriftSubmergeAnimation(((IEntityDataSaver) this), true);
                    this.setAnimation(ddSubmergeAnimation);
                    livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 100));
                    if(target instanceof  ServerPlayerEntity serverPlayer) {
                        syncNbtToDDTarget(serverPlayer, true);
                    }
                }
            }
        }

        this.attackEntitiesInRadius(/*dimensionDriftRadius,*/ dimensionDriftDamage);
        //}


        //Spawn DD Particles
        if(DemogorgonData.getSpawnDimensionDriftParticles(((IEntityDataSaver) this)) && this.hasDimensionDriftTarget) {
            Entity target = world.getEntityById(this.dimensionDriftTargetID);
            int particleCount = 20;
            double centerX = target.getX();
            double centerY = target.getY();
            double centerZ = target.getZ();
            double radius = 1;

            double increment = (2 * Math.PI) / particleCount;
            for(int o = 0; o < 5; o++) {
                for (double i = 0; i < particleCount; i++) {
                    double angle = i * increment * o;
                    double x = centerX + (radius * Math.cos(angle)) * nextDouble(0.0,1.0);
                    double y = centerY + ((float) this.targetDamageDelayTick) / 20;
                    double z = centerZ + (radius * Math.sin(angle))* nextDouble(0.0,1.0);

                    spawnDDParticlesOnClient(x,y,z,0, nextFloat(0.1F, 1F), 0, syncToClientsRadius);
                }
            }

            DemogorgonData.setSpawnDimensionDriftParticles(((IEntityDataSaver) this), false);
            //this.spawnDDParticlesOnClient(false, syncToClientsRadius);
        }
    }

    private void updateBossbar() {
        MinecraftServer server = world.getServer();
        if(server != null) {
            BossBar bossBar = server.getBossBarManager().add(new Identifier(DemogorgonMod.MOD_ID  + "demogorgon_bossbar"),
                    Text.literal("Demogorgon"));
            bossBar.setColor(BossBar.Color.RED);
            bossBar.setPercent(this.getMaxHealth() / this.getHealth());
        }
    }

    private void spawnDDParticlesOnClient(double x, double y, double z,
                                          double velocityX, double velocityY, double velocityZ, double radiusOfPlayerSyncedTo) {

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(velocityX);
        buf.writeDouble(velocityY);
        buf.writeDouble(velocityZ);

        List<Entity> entitiesInRadius = EntityUtil.getLivingEntitiesInRadius(this, radiusOfPlayerSyncedTo);
        for(Entity entity : entitiesInRadius) {
            if(entity instanceof ServerPlayerEntity player) {
                ServerPlayNetworking.send(player, ModMessages.DEMOGORGON_SPAWN_DD_PARTICLES_ID, buf);
            }
        }
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
            if(this.targetDamageDelayTick <= targetDamageMaxDelayTick && target != null && target.isAlive() && target instanceof LivingEntity living) {

                DemogorgonData.writeTargetToDDTargetNBT(((IEntityDataSaver) target), true);

                // Apply increasing Slowness to Target
                EntityAttributeInstance movementSpeed = living.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

                if(movementSpeed != null) {
                    if(this.slownessPerTickToDDTarget == 0) {
                        this.slownessPerTickToDDTarget = movementSpeed.getBaseValue() / targetDamageMaxDelayTick;
                        ((IEntityDataSaver) target).getPersistentData().putDouble("normal_movement_speed", movementSpeed.getBaseValue());
                    }
                    movementSpeed.setBaseValue(movementSpeed.getBaseValue() - this.slownessPerTickToDDTarget);
                    if(movementSpeed.getBaseValue() <= 0) {
                        this.slownessPerTickToDDTarget = 0;
                        movementSpeed.setBaseValue(((IEntityDataSaver) target).getPersistentData().getDouble("normal_movement_speed"));
                    }
                }

                if(this.targetDamageDelayTick % 4 == 0) {
                    DemogorgonData.setSpawnDimensionDriftParticles(((IEntityDataSaver) this), true);
                    //this.spawnDDParticlesOnClient(true, syncToClientsRadius);
                }
            } else {
                DemogorgonData.setPlayDimensionDriftSubmergeAnimation(((IEntityDataSaver) this), false);
                this.setAnimation(ddEmergeAnimation);
                if(target != null && this.hasDimensionDriftTarget && target.isAlive()) {
                    this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    this.teleport(target.getX(), target.getY(), target.getZ());
                    DemogorgonData.writeTargetToDDTargetNBT(((IEntityDataSaver) target), false);
                    if(target instanceof  ServerPlayerEntity serverPlayer) {
                        syncNbtToDDTarget(serverPlayer, false);
                    }
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

    private void updateAnimationStateC2S(int currentAnimationState) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(this.getId());
        buf.writeInt(currentAnimationState);
        ClientPlayNetworking.send(ModMessages.DEMOGORGON_ANIMATION_SYNC_ID, buf);

    }

    private <T extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<T> tAnimationState) {

        //System.out.println("Animation in predicate: " + this.getAnimation());

        if(this.getAnimation() == ddAttack1Animation) {
            //System.out.println("DDAttackAnimation");
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon.dimension_drift_attack_1", Animation.LoopType.PLAY_ONCE));
            if(tAnimationState.getController().hasAnimationFinished()) {
                System.out.println("Finished Animation");
                this.setAnimation(idleAnimation);
                this.updateAnimationStateC2S(idleAnimation);

            }
        }
        else if(this.getAnimation() == ddEmergeAnimation) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon.emerge", Animation.LoopType.PLAY_ONCE));
            if(tAnimationState.getController().hasAnimationFinished()) {
                System.out.println("Finished Animation");
                this.setAnimation(ddAttack1Animation);
                this.updateAnimationStateC2S(ddAttack1Animation);
            }
        }
        else if(this.getAnimation() == ddSubmergeAnimation) {
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
