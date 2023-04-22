package net.vxrofmods.demogorgonmod.mixin;


import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.vxrofmods.demogorgonmod.util.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {

        if(this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }

        return persistentData;
    }


    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if(persistentData != null) {
            nbt.put("demogorgonmod.vxr_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if(nbt.contains("demogorgonmod.vxr_data", 10)) {
            persistentData = nbt.getCompound("demogorgonmod.vxr_data");
        }
    }

}
