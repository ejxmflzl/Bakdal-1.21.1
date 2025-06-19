package kr.cantua.registries;

import kr.cantua.Bakdalmain;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class KeyMap {
    public static KeyBinding climbKey = new KeyBinding("key.movement.grab", GLFW.GLFW_KEY_R, Bakdalmain.MOD_ID+".movement");

    public static String translate(Object name) {
        String value = name.toString().toLowerCase();
        return "key.movement."+value;
    }
}
