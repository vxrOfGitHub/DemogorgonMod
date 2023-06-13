package net.vxrofmods.demogorgonmod.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.vxrofmods.demogorgonmod.networking.ModMessages;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_DEMOGORGON_MOD = "key.category.demogorgon_mod";
    public static final String KEY_USE_ABILITY_DEMOGORGON_HEAD = "key.demogorgon_mod.use_ability.demogorgon_head";

    public static KeyBinding useAbilityDemogorgonHeadKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(useAbilityDemogorgonHeadKey.wasPressed()) {
                // This happens when our custom key is pressed
                client.player.sendMessage(Text.literal("Pressed Ability Key"), true);
                ClientPlayNetworking.send(ModMessages.DEMOGORGON_HEAD_KEYBIND_SYNC_ID, PacketByteBufs.create());
            }
        });
    }

    public static void register() {
        useAbilityDemogorgonHeadKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_USE_ABILITY_DEMOGORGON_HEAD,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                KEY_CATEGORY_DEMOGORGON_MOD
        ));

        registerKeyInputs();
    }

}
