package kr.cantua.fabric.event;

import kr.cantua.fabric.component.TraitComponents;
import kr.cantua.logic.ChargedJumpLogic;
import kr.cantua.logic.GaugeLogic;
import kr.cantua.logic.JumpBlockerServerLogic;
import kr.cantua.registries.Trait;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ChargedJumpHandler {
    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            GaugeLogic.onGauge(server);
            ChargedJumpLogic.onServerTick(server);
            // 점프 차단 로직 제거 (필요 없으므로)
        });
    }
}
