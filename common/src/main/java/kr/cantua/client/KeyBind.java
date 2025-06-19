package kr.cantua.client;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.List;

public class KeyBind {
    public static KeyBinding grab;
    public static List<KeyBinding> keys;

    static {
        grab = new KeyBinding(
                "keybind.bakdal.grab",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                "bakdal"
        );

        keys = List.of(grab);
    }
}
