package kr.cantua.neoforge;

import net.neoforged.fml.common.Mod;

import kr.cantua.Bakdalmain;

@Mod(Bakdalmain.MOD_ID)
public final class NeoForgeMod {
    public NeoForgeMod() {
        // Run our common setup.
        Bakdalmain.init();
    }
}
