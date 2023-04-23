package net.vxrofmods.demogorgonmod.mixin;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.vxrofmods.demogorgonmod.util.DemogorgonData;
import net.vxrofmods.demogorgonmod.util.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class DemogorgonDecreaseTargetsJumpHeightMixin {
    @Inject(method = "getJumpVelocity", at = @At("HEAD"), cancellable = true)
    // Define a private method with a CallbackInfoReturnable parameter that matches the return type of the target method
    private void injectJumpVelocity(CallbackInfoReturnable<Float> info) {
        // Check if this entity is a player
        if(((Object) this) instanceof PlayerEntity player) {
            if(DemogorgonData.getDDTargetinNBT(((IEntityDataSaver) player))) {
                info.setReturnValue(0.01F);
                info.cancel();
            }
        }
    }
}
