package net.vxr.vxrofmods.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.vxr.vxrofmods.util.IEntityDataSaver;

public class NBTItem extends Item {

    public NBTItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(!user.getWorld().isClient() && stack.hasCustomName()) {
            NbtCompound nbt = ((IEntityDataSaver) entity).getPersistentData();
            String name = stack.getName().getString();
            nbt.putString("vxrofmods.custom_textures", name);
            user.sendMessage(Text.literal("You right clicked a " + entity.getType().getName().getString() + " with the name " + name));
            NbtCompound compound = ((IEntityDataSaver) entity).getPersistentData();
            user.sendMessage(Text.literal("He has the NBT: " + compound.getString("vxrofmods.custom_textures")));
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

}
