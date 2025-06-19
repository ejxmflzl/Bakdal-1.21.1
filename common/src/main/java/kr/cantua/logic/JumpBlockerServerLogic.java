package kr.cantua.logic;

import kr.cantua.api.TraitBridge;
import kr.cantua.registries.Trait;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class JumpBlockerServerLogic {
    private static final boolean DEBUG = true;

    public static void onTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            var trait = TraitBridge.get(player).get(Trait.loc.toString());

            if (player.isOnGround() && player.isSneaking() && trait.getBoolean("IS_GRAB_HELD")) {
                debug(player,"gigi");
            }
        }
    }

    private static void debug(ServerPlayerEntity player, String msg) {
        if (DEBUG) {
            player.sendMessage(Text.of("[JumpBlocker] " + msg), false);
        }
    }
}
