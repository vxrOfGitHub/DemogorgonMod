package net.vxrofmods.demogorgonmod.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil {

    public static boolean areLivingEntitiesInRadiusOfEntity(Entity entity, double radius) {
        World world = entity.getEntityWorld();

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        List<Entity> entities = world.getOtherEntities(null, new Box(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));

        for (Entity e : entities) {
            if (e == entity) {
                continue;
            }
            double distance = entity.squaredDistanceTo(e);
            if (distance <= radius * radius && e instanceof LivingEntity) {
                return true;
            }
        }

        return false;
    }

    public static List<Entity> getLivingEntitiesInRadius(Entity entity, double radius) {
        World world = entity.getEntityWorld();

        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        List<Entity> entities = world.getOtherEntities(null, new Box(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));

        List<Entity> entitiesInRadius = new ArrayList<>();

        for (Entity e : entities) {
            if (e == entity) {
                continue;
            }
            double distance = entity.squaredDistanceTo(e);
            if (distance <= radius * radius && e instanceof LivingEntity) {
                entitiesInRadius.add(e);
            }
        }

        return entitiesInRadius;
    }

    public static List<Entity> getEntitiesBelowCertainPercentHealth(List<Entity> entities, double healthDivider) {

        List<Entity> entitiesBelowCertainHealth = new ArrayList<>();

        for (Entity e : entities) {
            if(e instanceof LivingEntity le && le.getHealth() <= le.getMaxHealth() / healthDivider) {
                entitiesBelowCertainHealth.add(e);
            }
        }

        return entitiesBelowCertainHealth;
    }

}
