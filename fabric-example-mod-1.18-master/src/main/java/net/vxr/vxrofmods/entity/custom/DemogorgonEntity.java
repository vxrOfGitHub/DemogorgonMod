package net.vxr.vxrofmods.entity.custom;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.vxr.vxrofmods.entity.ai.goal.custom.DemogorgonAttackGoal;
import net.vxr.vxrofmods.entity.variant.DemogorgonVariant;
import net.vxr.vxrofmods.sound.ModSounds;
import net.vxr.vxrofmods.util.EntityUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomUtils.*;

public class DemogorgonEntity extends HostileEntity implements IAnimatable{

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private int dimensionDriftCooldown = 0;
    private final int dimensionDriftMaxCooldown = 100;
    private final int dimensionDriftRadius = 10;
    private final int dimensionDriftDamage = 10;
    private int targetDamageDelayTick = 0;
    private final int targetDamageMaxDelayTick = 60;
    private int dimensionDriftTargetID;
    private boolean hasDimensionDriftTarget = false;
    private boolean shouldSpawnDimensionDriftParticles = false;

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
        //if(event.getController().getAnimationState().equals(AnimationState.Stopped)) {
        //System.out.println("AnimationState: "  + event.getController().getAnimationState());
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.demogorgon.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
    //System.out.println("AnimationState: "  + event.getController().getAnimationState());
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.demogorgon.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
        //}
    }
    private PlayState attackPredicate(AnimationEvent event) {
        if(this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            //System.out.println("Is Handswinging!");
            event.getController().markNeedsReload();
            // Randomly choosing 1 animation of list
            List<String> possibleAttackAnimations = new ArrayList<>();
            possibleAttackAnimations.add("animation.demogorgon.attack2");
            possibleAttackAnimations.add("animation.demogorgon.attack1");

            event.getController().setAnimation(new AnimationBuilder().addAnimation(
                    possibleAttackAnimations.get(random.nextBetween(0,possibleAttackAnimations.size()-1)), ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.handSwinging = false;

            //System.out.println("AnimationState: "  + event.getController().getAnimationState());
        }

        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        animationData.addAnimationController(new AnimationController(this, "attackController",
              0, this::attackPredicate));
    }



    /*private PlayState attackPredicate(AnimationEvent<> animationEvent) {
            if (this.handSwinging && animationEvent.getController().getAnimationState().equals(AnimationState.Stopped)) {
                animationEvent.getController().markNeedsReload();
                List<String> attackAnimationsList = new ArrayList<>();
                attackAnimationsList.add("animation.demogorgon.attack1");
                attackAnimationsList.add("animation.demogorgon.attack2");
                animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation(
                        attackAnimationsList.get(random.nextInt(attackAnimationsList.size())), ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            }
        return PlayState.CONTINUE;
    } */


    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", this.getTypeVariant());
        nbt.putInt("demogorgon.dimension_drift.cooldown", dimensionDriftCooldown);
        nbt.putInt("demogorgon.dimension_drift.targetDamageDelayTick", targetDamageDelayTick);
        nbt.putInt("demogorgon.dimension_drift.target_id", dimensionDriftTargetID);
        nbt.putBoolean("demogorgon.dimension_drift.has_target", hasDimensionDriftTarget);
        nbt.putBoolean("demogorgon.dimension_drift.spawn_particles", shouldSpawnDimensionDriftParticles);
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

    /*private void attackEntitiesInRadius(Entity source, double radius, double damage) {
        // Get the world object for the source entity
        World world = source.getEntityWorld();

        // Get the position of the source entity
        double x = source.getX();
        double y = source.getY();
        double z = source.getZ();

        // Get all entities within the specified radius of the source entity
        List<Entity> entities = world.getOtherEntities(null, new Box(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));

        // Loop through each entity and apply the attack
        for (Entity target : entities) {
            // Check if the target entity is the same as the source entity
            if (target == source) {
                continue;
            }

            // Calculate the distance between the source and target entities
            double distance = source.squaredDistanceTo(target);

            // Check if the target entity is within the specified radius
            if (distance <= radius * radius && target.isAttackable() && target.isAlive() && !target.isInvulnerable()
                && target instanceof LivingEntity livingTarget && livingTarget.getHealth() <= livingTarget.getMaxHealth() / 5) {
                // Apply the attack to the target entity
                target.damage(DamageSource.GENERIC, (float) damage);
                System.out.println("This Target: " + target.getType().getName().getString() + " took " + damage + " Damage at this Position: " + target.getBlockPos().toString());
            }
        }
    }*/

    @Override
    public void tick() {
        //if(!world.isClient()) {
            if (EntityUtil.areLivingEntitiesInRadiusOfEntity(this, dimensionDriftRadius)) {
                this.dimensionDriftCooldown++;
                //Get Dimension Drift Target & save it's ID
                if(this.dimensionDriftCooldown >= dimensionDriftMaxCooldown) {
                    Entity target = getTargetForDimensionDrift(this, dimensionDriftRadius);
                    if(target != null) {
                        this.dimensionDriftTargetID = target.getId();
                        this.hasDimensionDriftTarget = true;
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
            ParticleEffect particle = ParticleTypes.EFFECT;

            DustParticleEffect dustParticleEffect = new DustParticleEffect(new Vec3f(255,0,0), 1f);

            double increment = (2 * Math.PI) / particleCount;
            for(int o = 0; o < 5; o++) {
                for (double i = 0; i < particleCount; i++) {
                    double angle = i * increment * o;
                    double x = centerX + (radius * Math.cos(angle)) * nextDouble(0.0,1.0);
                    double y = centerY + i / 20;
                    double z = centerZ + (radius * Math.sin(angle))* nextDouble(0.0,1.0);
                    tWorld.addParticle(dustParticleEffect, x, y, z, 0, nextFloat(0.1F, 1F), 0);
                }
            }

            this.shouldSpawnDimensionDriftParticles = false;
        }
        super.tick();
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
        return possibleTargets.get(nextInt(0,possibleTargets.size()));
    }

    public void attackEntitiesInRadius(/*double radius,*/ float damage) {

        if(this.hasDimensionDriftTarget) {
            Entity target = world.getEntityById(this.dimensionDriftTargetID);
            this.targetDamageDelayTick++;
            if(this.targetDamageDelayTick <= targetDamageMaxDelayTick && target != null) {
                //target.world.addParticle(ParticleTypes.FLAME, target.getX(), target.getY(), target.getZ(), 0.0, 0.1, 0.0);
                if(this.targetDamageDelayTick % 4 == 0) {
                    this.shouldSpawnDimensionDriftParticles = true;
                }
            } else {
                this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                if(target != null)
                    target.damage(DamageSource.mob(this), damage);
                this.targetDamageDelayTick = 0;
                this.dimensionDriftTargetID = 0;
                this.hasDimensionDriftTarget = false;
            }
        }

        /*
        if(!world.isClient() && EntityUtil.areLivingEntitiesInRadiusOfEntity(this, radius)) {
            this.dimensionDriftCooldown++;
            if(this.dimensionDriftCooldown % 50 == 0) {
                System.out.println("Demo Cooldown: " + this.dimensionDriftCooldown);
            }
            List<Entity> entitiesBelowCertainHealth = EntityUtil.getEntitiesBelowCertainPercentHealth(getLivingEntitiesInRadius(this, radius),
                    5);
            if(this.dimensionDriftCooldown >= dimensionDriftMaxCooldown
                    && !entitiesBelowCertainHealth.isEmpty()) {


                boolean hasPlayerEntity = false;
                Entity target = null;

                for (Entity e : entitiesBelowCertainHealth) {
                    if (e instanceof PlayerEntity ptarget && !ptarget.getAbilities().creativeMode) {
                        hasPlayerEntity = true;
                        target = e;
                    }
                } if (!hasPlayerEntity) {
                    target = entitiesBelowCertainHealth.get(nextInt(0, entitiesBelowCertainHealth.size()));
                }

                if(this.targetDamageDelayTick < targetDamageMaxDelayTick) {
                    target.world.addParticle(ParticleTypes.FLAME, target.getX(), target.getY(), target.getZ(), 0.0, 0.1, 0.0);
                    this.targetDamageDelayTick++;
                }
                else {
                    this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    target.damage(DamageSource.mob(this), damage);
                    this.targetDamageDelayTick = 0;
                    this.dimensionDriftCooldown = 0;
                }
            }
        }*/
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

    /*@Override
    protected void mobTick() {

        this.getBoundingBox().expand(30).

        super.mobTick();
    } */
}
