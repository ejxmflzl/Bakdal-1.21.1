package kr.cantua.logic;

import kr.cantua.api.ITrait;
import kr.cantua.api.TraitBridge;
import kr.cantua.registries.Trait;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class GaugeLogic {
    private static final double MAX_GAUGE_TIME = 1.5;
    private static final double TICK_INCREMENT = 1.0 / 20.0;

    public static void onGauge(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            var trait = TraitBridge.get(player).get(Trait.loc.toString());
            boolean isSprinting = player.isSprinting();
            boolean isGrabHeld = trait.getBoolean("IS_GRAB_HELD");
            boolean isJumpHeld = trait.getBoolean("IS_JUMP_HELD");

            // 스프린트 + 그랩 키 누를 때 게이지 증가
            if (isSprinting && isGrabHeld) {
                double sprintGauge = trait.getDouble(Trait.Double.SPRINT_GAUGE.name());
                double sprintMax = trait.getDouble(Trait.Double.SPRINT_GAUGE_MAX.name());
                if (sprintMax <= 0) sprintMax = MAX_GAUGE_TIME;

                if (sprintGauge < sprintMax) {
                    sprintGauge = Math.min(sprintGauge + TICK_INCREMENT, sprintMax);
                    trait.setDouble(Trait.Double.SPRINT_GAUGE.name(), sprintGauge);
                    trait.setBoolean(Trait.Bool.IS_SPRINT_CHARGING.name(), true);
                }
            } else {
                trait.setDouble(Trait.Double.SPRINT_GAUGE.name(), 0);
                trait.setBoolean(Trait.Bool.IS_SPRINT_CHARGING.name(), false);
            }

            // 점프 게이지 충전 (그랩 + 점프키 누를 때)
            if (isGrabHeld && isJumpHeld) {
                double jumpGauge = trait.getDouble(Trait.Double.JUMP_GAUGE.name());
                double jumpMax = trait.getDouble(Trait.Double.JUMP_GAUGE_MAX.name());
                if (jumpMax <= 0) jumpMax = MAX_GAUGE_TIME;

                if (jumpGauge < jumpMax) {
                    jumpGauge = Math.min(jumpGauge + TICK_INCREMENT, jumpMax);
                    trait.setDouble(Trait.Double.JUMP_GAUGE.name(), jumpGauge);
                    trait.setBoolean(Trait.Bool.IS_JUMP_CHARGING.name(), true);
                }
            } else {
                trait.setDouble(Trait.Double.JUMP_GAUGE.name(), 0);
                trait.setBoolean(Trait.Bool.IS_JUMP_CHARGING.name(), false);
            }
        }
    }
}
