package net.vxrofmods.demogorgonmod.mixin;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.vxrofmods.demogorgonmod.util.DemogorgonData;
import net.vxrofmods.demogorgonmod.util.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class DemogorgonDecreaseTargetsJumpHeightMixin {

    @Shadow protected abstract void playBlockFallSound();

    @Shadow protected abstract void playEquipmentBreakEffects(ItemStack stack);

    @Inject(method = "getJumpVelocity", at = @At("HEAD"), cancellable = true)
    // Define a private method with a CallbackInfoReturnable parameter that matches the return type of the target method
    private void injectJumpVelocity(CallbackInfoReturnable<Float> info) {
        // Check if this entity is a player
        if(((Object) this) instanceof PlayerEntity player) {
            System.out.println("World before: " + player.getWorld() + " " + DemogorgonData.getDDTargetinNBT(((IEntityDataSaver) player)));
            if(DemogorgonData.getDDTargetinNBT(((IEntityDataSaver) player))) {
                System.out.println("Is Targetted Player " + player.getWorld());
                info.setReturnValue(0.01F);
                info.cancel();
            }
        }
        /*if (((Object) this) instanceof LivingEntity living && DemogorgonData.getDDTargetinNBT((IEntityDataSaver) living)) {
            System.out.println("Jumper is a Target");
            // Set the return value to 0.5F (half of the normal jump power) and cancel the original method
            info.setReturnValue(0.1F);
            info.cancel();
        }*/
    }
}
