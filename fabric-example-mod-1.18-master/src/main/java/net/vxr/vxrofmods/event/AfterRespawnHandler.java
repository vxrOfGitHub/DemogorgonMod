package net.vxr.vxrofmods.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomUtils.nextInt;

public class AfterRespawnHandler implements ServerPlayerEvents.AfterRespawn{
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        giveNegativeEffects(newPlayer);
    }

    private void giveNegativeEffects(PlayerEntity newPlayer) {
        List<StatusEffect> negativeEffects = new ArrayList<>();
        negativeEffects.add(StatusEffects.BLINDNESS);
        negativeEffects.add(StatusEffects.MINING_FATIGUE);
        negativeEffects.add(StatusEffects.HUNGER);
        negativeEffects.add(StatusEffects.NAUSEA);
        negativeEffects.add(StatusEffects.POISON);
        negativeEffects.add(StatusEffects.SLOWNESS);
        negativeEffects.add(StatusEffects.WEAKNESS);
        negativeEffects.add(StatusEffects.WITHER);

        List<Integer> effectLenght = new ArrayList<>();
        effectLenght.add(nextInt(1000, 2401));
        effectLenght.add(nextInt(1000, 2401));
        effectLenght.add(nextInt(1000, 2401));
        effectLenght.add(nextInt(300, 801));
        effectLenght.add(nextInt(670, 1970));
        effectLenght.add(nextInt(1000, 2401));
        effectLenght.add(nextInt(1000, 2401));
        effectLenght.add(nextInt(300, 801));

        List<Integer> effectStrengh = new ArrayList<>();
        effectStrengh.add(0);
        effectStrengh.add(1);
        effectStrengh.add(4);
        effectStrengh.add(0);
        effectStrengh.add(0);
        effectStrengh.add(nextInt(1, 3));
        effectStrengh.add(nextInt(1, 3));
        effectStrengh.add(0);

        int randomEffect = nextInt(0,negativeEffects.size());

        newPlayer.addStatusEffect(new StatusEffectInstance(negativeEffects.get(randomEffect), effectLenght.get(randomEffect), effectStrengh.get(randomEffect)));

        System.out.println("You died and were effected with " + negativeEffects.get(randomEffect).getTranslationKey() + " " + effectStrengh.get(randomEffect) + " for " + effectLenght.get(randomEffect));
    }

}
