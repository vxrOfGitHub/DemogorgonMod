package net.vxr.vxrofmods.event;


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.vxr.vxrofmods.item.custom.DreamBootsItem;
import net.vxr.vxrofmods.networking.ModMessages;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_WW2MOD = "key.category.vxrofmods.ww2mod";
    public static final String KEY_DREAM_BOOST = "key.vxrofmods.dream_boost";

    public static final String KEY_DREAM_VISION = "key.vxrofmods.dream_vision";
    public static final String KEY_EXAMPLE = "key.vxrofmods.example";

    public static KeyBinding dreamBoostKey;
    public static KeyBinding dreamVisionKey;
    public static KeyBinding exampleKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dreamBoostKey.wasPressed()) {
                ClientPlayNetworking.send(ModMessages.DREAM_BOOST_ID, PacketByteBufs.create());
            }
            if (dreamVisionKey.wasPressed()) {
                //ClientPlayNetworking.send(ModMessages.DREAM_VISION_ID, PacketByteBufs.create());
            }
            if (exampleKey.wasPressed()) {
                //ClientPlayNetworking.send(ModMessages.EXAMPLE_ID, PacketByteBufs.create());
            }
        });
    }

    public static void register() {
        dreamBoostKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_DREAM_BOOST,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                KEY_CATEGORY_WW2MOD
        ));
        exampleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_EXAMPLE,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                KEY_CATEGORY_WW2MOD
        ));
        dreamVisionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_DREAM_VISION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KEY_CATEGORY_WW2MOD
        ));
        registerKeyInputs();
    }

    public static boolean UseDreamBoots;

}
