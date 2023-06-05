package net.vxrofmods.demogorgonmod.entity.custom;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.vxrofmods.demogorgonmod.DemogorgonMod;
import net.vxrofmods.demogorgonmod.entity.ModEntities;
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
import java.util.UUID;

import static net.vxrofmods.demogorgonmod.util.DemogorgonData.*;
import static org.apache.commons.lang3.RandomUtils.*;

public class DemogorgonEntity extends HostileEntity implements GeoEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private int customAttackCooldown = 0;
    private final int maxCustomAttackCooldown = 100;
    private final int customAttackRadius = 20;
    private final int dimensionDriftDamage = 10;
    private final double syncToClientsRadius = 256;
    private int targetDamageDelayTick = 0;

    private int targetDamageDelayTickSinceEmerge = 0;
    private final int targetDamageMaxDelayTick = 200;
    private int dimensionDriftTargetID;
    private boolean hasDimensionDriftTarget = false;
    private boolean shouldSpawnDimensionDriftParticles = false;
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
    private final int emergeAnimation = 7;

    public static final int timedSoundsCount = 1;
    private static final int ddAttack1AnimationDuration = 127;

    private final ServerBossBar bossBar = (ServerBossBar)new ServerBossBar(this.getDisplayName(), BossBar.Color.RED, BossBar.Style.PROGRESS).setDarkenSky(true);
    private int nextPerformAttack = 0;
    private boolean doneSwitch2Stage2 = false;
    private int switch2Stage2Ticks;
    private boolean switch2Stage2Began;

    private static final UUID MAX_HEALTH_MODIFIER_ID = UUID.randomUUID();
    private static final double maxHealth = 200D;
    private static final float attackDamage = 20f;
    private static final float attackSpeed = 2f;
    private static final float knockbackResistance = 0.5f;
    private static final float attackKnockback = 1f;
    private static final float movementSpeed = 0.2f;

    public DemogorgonEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
    // Normal
    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, maxHealth)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, attackDamage)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, attackSpeed)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, knockbackResistance)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, movementSpeed);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        if(!this.isInDDAnimation()) {
            this.goalSelector.add(1, new RevengeGoal(this));
            this.goalSelector.add(2, new MeleeAttackGoal(this, 2d, false));
            this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
            this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
            this.goalSelector.add(8, new LookAroundGoal(this));
            this.goalSelector.add(8, new WanderAroundPointOfInterestGoal(this, movementSpeed, false));
            this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, false, false));
            this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
            //this.targetSelector.add( 4, new ActiveTargetGoal<>(this, LivingEntity.class, true, true));
        }



    }


    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", this.getTypeVariant());
        nbt.putInt("demogorgon.animation", this.currentAnimation);
        nbt.putInt("demogorgon.custom_attack.cooldown", customAttackCooldown);
        nbt.putInt("demogorgon.dimension_drift.target_damage_delay_tick", targetDamageDelayTick);
        nbt.putInt("demogorgon.dimension_drift.target_damage_delay_tick_since_emerge", targetDamageDelayTickSinceEmerge);
        nbt.putInt("demogorgon.dimension_drift.target_id", dimensionDriftTargetID);
        nbt.putInt("demogorgon.ticks_since_last_attack", ticksSinceLastAttack);
        nbt.putInt("demogorgon.next_perform_attack", nextPerformAttack);
        nbt.putInt("demogorgon.switch_to_stage_2_ticks", switch2Stage2Ticks);
        nbt.putDouble("demogorgon.slowness_per_tick_to_dd_target", slownessPerTickToDDTarget);
        nbt.putBoolean("demogorgon.dimension_drift.has_target", hasDimensionDriftTarget);
        nbt.putBoolean("demogorgon.dimension_drift.spawn_particles", shouldSpawnDimensionDriftParticles);
        nbt.putBoolean("demogorgon.done_switch_to_stage_2", doneSwitch2Stage2);
        nbt.putBoolean("demogorgon.switch_to_stage_2_began", switch2Stage2Began);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
        this.currentAnimation = nbt.getInt("demogorgon.animation");
        this.customAttackCooldown = nbt.getInt("demogorgon.custom_attack.cooldown");
        this.targetDamageDelayTick = nbt.getInt("demogorgon.dimension_drift.target_damage_delay_tick");
        this.targetDamageDelayTickSinceEmerge = nbt.getInt("demogorgon.dimension_drift.target_damage_delay_tick_since_emerge");
        this.dimensionDriftTargetID = nbt.getInt("demogorgon.dimension_drift.target_id");
        this.ticksSinceLastAttack = nbt.getInt("demogorgon.ticks_since_last_attack");
        this.nextPerformAttack = nbt.getInt("demogorgon.next_perform_attack");
        this.switch2Stage2Ticks = nbt.getInt("demogorgon.switch_to_stage_2_ticks");
        this.slownessPerTickToDDTarget = nbt.getDouble("demogorgon.slowness_per_tick_to_dd_target");
        this.hasDimensionDriftTarget = nbt.getBoolean("demogorgon.dimension_drift.has_target");
        this.shouldSpawnDimensionDriftParticles = nbt.getBoolean("demogorgon.dimension_drift.spawn_particles");
        this.doneSwitch2Stage2 = nbt.getBoolean("demogorgon.done_switch_to_stage_2");
        this.switch2Stage2Began = nbt.getBoolean("demogorgon.switch_to_stage_2_began");
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


    private Box getNewBoundingBox(float width, float height) {

        // Calculate the half-width and half-height of the hitbox
        float halfWidth = width / 2.0f;
        float halfHeight = height / 2.0f;

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
        if(!this.isInDDAnimation()) {
            return ModSounds.DEMOGORGON_IDLE;
        }
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if(!this.isInDDAnimation()) {
            return ModSounds.DEMOGORGON_HURT_SOUND;
        }
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        if(!this.isInDDAnimation()) {
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
        if(!this.isInDDAnimation()) {
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

        if(!this.isInDDAnimation()) {
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

    @Override
    protected void attackLivingEntity(LivingEntity target) {

        if(!this.isInDDAnimation()) {

            super.attackLivingEntity(target);
        }
    }

    private void syncNbtToDDTarget(ServerPlayerEntity player , boolean isTarget) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(player.getId());
        buf.writeBoolean(isTarget);

        ServerPlayNetworking.send(player, ModMessages.GET_DD_TARGET_IN_NBT_SYNC_ID, buf);
    }



    @Override
    public void onPlayerCollision(PlayerEntity player) {

        if(!this.isInDDAnimation()) {
            super.onPlayerCollision(player);
        }
    }

    @Override
    public void tick() {
        super.tick();

        updateBossbar();
        customAttackControl();
        setInvulnerability();
        playTimedSounds();
        applyTimedDamage();
        rotationControl();
    }


    private void customAttackControl() {

        if(!world.isClient()) {

            checkForDDTarget();

            // Decreasing Cooldown if living entities are in range
            if (EntityUtil.areLivingEntitiesInRadiusOfEntity(this, customAttackRadius) && this.nextPerformAttack == 0) {
                this.customAttackCooldown++;
            }
            if(this.switch2Stage2Began || this.getHealth() <= this.getMaxHealth() * 0.4 && !this.doneSwitch2Stage2) {
                switch2Stage2();
                this.customAttackCooldown = 0;

            }
            if(this.customAttackCooldown >= maxCustomAttackCooldown) {
                int a = nextInt(0, 11);
                nextPerformAttack = 1;
            }
        }
    }

    private void switch2Stage2() {

        switch2Stage2Ticks++;

        // Should happen ones
        if(!switch2Stage2Began) {
            this.setAnimation(ddSubmergeAnimation);

            this.modifyStage2Attributes();

            // Spawning Demodogs
            for(int i = 0; i < nextInt(8, 13); i++) {
                // Zombie as placeholder for DemoDog
                ModEntities.DEMO_DOG.spawn(((ServerWorld) world), this.getBlockPos(), SpawnReason.REINFORCEMENT);
            }

            switch2Stage2Began = true;
        }

        // should happen repeatedly

        List<Entity> entities = EntityUtil.getLivingEntitiesInRadius(this, 100);
        boolean areDemoDogsAlive = false;
        for (Entity e : entities) {
            if(e.isAlive() && e.getType().equals(ModEntities.DEMO_DOG)) { // Zombie as placeholder for DemoDog
                areDemoDogsAlive = true;
                //System.out.println("Zombies Alive");
            }
        }

        if(areDemoDogsAlive) {
            // happens as long as near Demodogs are alive
            if(switch2Stage2Ticks % 20 == 0) {
                this.heal(this.getMaxHealth() / 50);
            }
        } else {
            // at the End
            this.doneSwitch2Stage2 = true;
            switch2Stage2Began = false;
            this.setAnimation(emergeAnimation);
            switch2Stage2Ticks = 0;
        }
    }

    private void modifyStage2Attributes() {
        EntityAttributeInstance attackDamageAttribute = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (attackDamageAttribute != null) {
            attackDamageAttribute.setBaseValue(attackDamage * 1.5); // Set the attack damage value to 10.0
        }

        EntityAttributeInstance movementSpeedAttribute = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (movementSpeedAttribute != null) {
            movementSpeedAttribute.setBaseValue(movementSpeed * 3); // Set the movement speed value to 0.3
        }

        EntityAttributeInstance attackSpeedAttribute = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED);
        if (attackSpeedAttribute != null) {
            attackSpeedAttribute.setBaseValue(attackSpeed * 1.2); // Set the attack speed value to 20.0
        }

        EntityAttributeInstance knockbackResistanceAttribute = this.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
        if (knockbackResistanceAttribute != null) {
            knockbackResistanceAttribute.setBaseValue(knockbackResistance * 3); // Set the knockback resistance value to 0.5
        }

        EntityAttributeInstance knockbackAttribute = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
        if (knockbackAttribute != null) {
            knockbackAttribute.setBaseValue(attackKnockback * 3); // Set the knockback value to 2.0
        }

        // Doubling max Health
        EntityAttributeInstance maxHealthAttribute = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute != null) {
            maxHealthAttribute.addTemporaryModifier(new EntityAttributeModifier(MAX_HEALTH_MODIFIER_ID,
                "Max Health Modifier",
                maxHealth, // Adjust the modifier value to double the maximum health
                EntityAttributeModifier.Operation.ADDITION));
            //setHealth(getHealth() * 2); // Increase the current health accordingly
        }


    }

    private void checkForDDTarget() {

        // Checking if DD Target still exists
        if(this.hasDimensionDriftTarget) {
            Entity target = world.getEntityById(this.dimensionDriftTargetID);
            if(target == null || !target.isAlive()) {
                this.customAttackCooldown = 0;
                this.targetDamageDelayTick = 0;
                this.dimensionDriftTargetID = 0;
                this.hasDimensionDriftTarget = false;
                nextPerformAttack = 0;

            }
        }
        // If Cooldown is ready and if last attack was at least before 2s
        if(this.customAttackCooldown >= maxCustomAttackCooldown && this.ticksSinceLastAttack >= 40 && nextPerformAttack == 1) {
            // Get Dimension Drift Target & save it's ID
            Entity target = getTargetForDimensionDrift(this, customAttackRadius);
            if(target == null) {
                this.nextPerformAttack = 0;
            }
            if(target != null && target.isAlive() && target instanceof LivingEntity livingTarget) {
                this.dimensionDriftTargetID = target.getId();
                this.hasDimensionDriftTarget = true;
                // Playing Submerge Animation
                DemogorgonData.setPlayDimensionDriftSubmergeAnimation(((IEntityDataSaver) this), true);
                this.setAnimation(ddSubmergeAnimation);
                // Giving the Darkness Effect to the Target
                livingTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 460));
                // Let the target (if player) know that he's the target
                if(target instanceof  ServerPlayerEntity serverPlayer) {
                    syncNbtToDDTarget(serverPlayer, true);
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

    private void rotationControl() {

        if(!world.isClient()) {

            if(this.getAnimation() == ddAttack1Animation && this.hasDimensionDriftTarget) {

                Entity target = world.getEntityById(this.dimensionDriftTargetID);
                if(target != null) {
                    Vec3d targetPos = target.getPos();

                    double dx = targetPos.x - getX();
                    double dy = targetPos.y - getEyeY();
                    double dz = targetPos.z - getZ();

                    double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

                    float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
                    float pitch = (float) -Math.toDegrees(Math.atan2(dy, horizontalDistance));

                    this.setRotation(yaw, pitch);
                }
            }
        }

    }

    private void applyTimedDamage() {

        if(!world.isClient()) {

            applyDDAttack1Damage();

        }

    }

    private void applyDDAttack1Damage() {

        IEntityDataSaver saver = ((IEntityDataSaver) this);

        int currentTick = readDDAttack1CurrentTick(saver);

        if(this.getAnimation() == ddAttack1Animation && currentTick <= ddAttack1AnimationDuration
        && this.hasDimensionDriftTarget) {

            Entity target = world.getEntityById(this.dimensionDriftTargetID);

            if(target instanceof LivingEntity living) {

                // Counting up ticks for the timing
                writeDDAttack1CurrentTick(saver, currentTick + 1);

                // Making sure the target does not walk
                Vec3d destination = readDDTargetsPositionFromTarget(((IEntityDataSaver) target));
                living.teleport(destination.x, destination.y, destination.z);

                // Timing Damage to Animation
                if(currentTick == 62) {
                    performSweepAttack(30f, 3.0, 0);
                }
                if(currentTick == 77) {
                    performSweepAttack(30f, 3.0, 0);
                }
                if(currentTick == 93) {
                    performSweepAttack(40f, 3.0, 0);
                }
                if(currentTick == 102) {
                    performSweepAttack(40f, 3.0, 0);
                }
                if(currentTick == 112) {
                    performSweepAttack(60f, 3.0, 0);
                }
                if(currentTick == 122) {
                    performSweepAttack(100f, 3.0, 0);
                }
            }
        } else {
            writeDDAttack1CurrentTick(saver, 0);
        }
    }

    private void performSweepAttack(float damage, double range, double knockbackAmount) {
        Box attackArea = this.getBoundingBox().expand(range);

        List<Entity> entities = world.getOtherEntities(this, attackArea);

        // Iterate through the nearby entities
        for (Entity entity : entities) {
            if(entity instanceof LivingEntity living) {
                // Apply damage and knockback to the entity
                living.damage(world.getDamageSources().mobAttack(this), damage);

                // Calculate knockback vector
                Vec3d knockbackVec = living.getPos().subtract(getPos()).normalize().multiply(knockbackAmount, 0.5, knockbackAmount);

                // Apply knockback to the entity
                living.addVelocity(knockbackVec.x, knockbackVec.y, knockbackVec.z);

                // Breaking Shield
                if(entity instanceof PlayerEntity player && player.blockedByShield(world.getDamageSources().mobAttack(this))) {
                    player.disableShield(true);
                }
            }
        }
    }

    private Box getSweepAttackArea(double range) {
        Vec3d mobPos = this.getPos(); // Replace with the actual method to get the mob's position
        float mobYaw = this.getYaw(); // Replace with the actual method to get the mob's yaw (rotation around the Y-axis)

        double halfWidth = 1.0; // Adjust this to your desired attack area width

        double dx = Math.cos(Math.toRadians(-mobYaw)) * range;
        double dz = Math.sin(Math.toRadians(-mobYaw)) * range;

        // Calculate the corners of the attack area rectangle
        Vec3d corner1 = new Vec3d(mobPos.x - dx - halfWidth, mobPos.y - 0.5, mobPos.z - dz - halfWidth);
        Vec3d corner2 = new Vec3d(mobPos.x - dx + halfWidth, mobPos.y + 0.5, mobPos.z - dz + halfWidth);

        return new Box(corner1, corner2);
    }

    private void playTimedSounds() {

        IEntityDataSaver saver = ((IEntityDataSaver) this);

        if(world.isClient()) {

            playDDAttack1Sound(saver);
        }

    }

    private void playDDAttack1Sound(IEntityDataSaver saver) {
        if(this.getAnimation() == ddAttack1Animation) {
            if(!DemogorgonData.getPlayedDDAttack1Sound(saver)) {
                world.playSound(this.getX(), this.getY(), this.getZ(), ModSounds.DEMOGORGON_DD_ATTACK_1_SOUND, SoundCategory.HOSTILE, 1f, 1f, true);
                DemogorgonData.writePlayedDDAttack1Sound(saver, true);
            }
        } else {
            DemogorgonData.writePlayedDDAttack1Sound(saver, false);
        }
    }

    private void setInvulnerability() {
        if(!world.isClient()) {
            this.setInvulnerable(isInDDAnimation());
        }
    }

    private boolean isInDDAnimation() {
        return this.getAnimation() == ddSubmergeAnimation || this.getAnimation() == ddEmergeAnimation || this.getAnimation() == ddAttack1Animation
                || this.getAnimation() == emergeAnimation;
    }

    @Override
    public boolean tryAttack(Entity target) {
        if(!isInDDAnimation()) {
            return false;
        }
        return super.tryAttack(target);
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
        List<Entity> possibleTargets = EntityUtil.getEntitiesBelowCertainPercentHealth(EntityUtil.getLivingEntitiesInRadius(e, radius), 5);
        if(possibleTargets.isEmpty()) {
            return null;
        }
        for (Entity target : possibleTargets) {
            if(target instanceof PlayerEntity) {
                return target;
            }
        }
        return null;
        //possibleTargets.get(nextInt(0,possibleTargets.size()));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {

        if(isInDDAnimation()) {
            target = null;
        }

        super.setTarget(target);
    }

    private void teleportInFrontOfTarget(Entity entity, double dinstanceToEntity) {
        Vec3d playerPos = entity.getPos();
        Vec3d playerLookVec = entity.getRotationVector();

        // Calculate the teleport position in front of the player
        Vec3d teleportPos = playerPos.add(playerLookVec.multiply(dinstanceToEntity, 0, dinstanceToEntity));

        // Adjust the teleport position to the ground level
        teleportPos = adjustTeleportPositionToGround(teleportPos);

        // Teleport the hostile mob to the calculated position
        this.setPosition(teleportPos);
    }

    private Vec3d adjustTeleportPositionToGround(Vec3d position) {
        int maxIterations = 16; // Adjust this to your desired maximum number of iterations
        double yOffset = 1.0; // Adjust this to the desired initial offset from the ground

        int blockPosX = (int) position.x;
        int blockPosY = (int) position.y;
        int blockPosZ = (int) position.z;

        BlockPos blockPos = new BlockPos(blockPosX, blockPosY, blockPosZ);

        // Iterate downward to find the ground level
        for (int i = 0; i < maxIterations; i++) {
            double posY = position.y - yOffset;

            if (world.isAir(blockPos) && world.isAir(blockPos.down()) && world.isAir(blockPos.up())) {
                return new Vec3d(position.x, posY, position.z);
            }

            yOffset += 1.0;
        }

        return position; // Return the original position if no suitable ground level is found
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
                    }
                }

                if(this.targetDamageDelayTick % 4 == 0) {
                    DemogorgonData.setSpawnDimensionDriftParticles(((IEntityDataSaver) this), true);
                    //this.spawnDDParticlesOnClient(true, syncToClientsRadius);
                }
            } else if(this.getAnimation() == ddSubmergeAnimation) {
                DemogorgonData.setPlayDimensionDriftSubmergeAnimation(((IEntityDataSaver) this), false);
                this.setAnimation(ddEmergeAnimation);
                targetDamageDelayTickSinceEmerge = targetDamageDelayTick;
                this.nextPerformAttack = 0;

                if(target != null && this.hasDimensionDriftTarget && target.isAlive()) {
                    //this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, 1.0f);

                    writeDDTargetsPositionToTarget(((IEntityDataSaver) target), target.getPos());

                    this.teleportInFrontOfTarget(target, 2);
                }

            }
            if (this.getAnimation() == ddEmergeAnimation && this.targetDamageDelayTick - targetDamageDelayTickSinceEmerge >= 60) {
                this.setAnimation(ddAttack1Animation);
                this.targetDamageDelayTickSinceEmerge = targetDamageDelayTick;

            } else if (this.getAnimation() == ddAttack1Animation && this.targetDamageDelayTick - targetDamageDelayTickSinceEmerge >= 133) {
                this.setAnimation(idleAnimation);
                targetDamageDelayTickSinceEmerge = 0;
            }
            if(!this.isInDDAnimation()) {

                if(target != null && this.hasDimensionDriftTarget && target.isAlive() && target instanceof LivingEntity living) {

                    EntityAttributeInstance movementSpeed = living.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

                    DemogorgonData.writeTargetToDDTargetNBT(((IEntityDataSaver) target), false);

                    if(movementSpeed != null) {
                        movementSpeed.setBaseValue(((IEntityDataSaver) target).getPersistentData().getDouble("normal_movement_speed"));
                    }

                    if (target instanceof ServerPlayerEntity serverPlayer) {
                        syncNbtToDDTarget(serverPlayer, false);
                    }
                }
                this.customAttackCooldown = 0;
                this.targetDamageDelayTick = 0;
                this.dimensionDriftTargetID = 0;
                this.hasDimensionDriftTarget = false;
                nextPerformAttack = 0;
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

        if(this.getAnimation() == emergeAnimation) {
            //System.out.println("DDAttackAnimation");
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon.emerge", Animation.LoopType.PLAY_ONCE));
            if(tAnimationState.getController().hasAnimationFinished()) {
                updateAnimationStateC2S(idleAnimation);
                this.setAnimation(idleAnimation);
            }
        } else if(this.getAnimation() == ddAttack1Animation) {
            //System.out.println("DDAttackAnimation");
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon.dimension_drift_attack_1", Animation.LoopType.PLAY_ONCE));
            if(tAnimationState.getController().hasAnimationFinished()) {
                updateAnimationStateC2S(idleAnimation);
                this.setAnimation(idleAnimation);
            }
        }
        else if(this.getAnimation() == ddEmergeAnimation) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.demogorgon.emerge", Animation.LoopType.PLAY_ONCE));
            if(tAnimationState.getController().hasAnimationFinished()) {
                updateAnimationStateC2S(ddAttack1Animation);
                this.setAnimation(ddAttack1Animation);
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
