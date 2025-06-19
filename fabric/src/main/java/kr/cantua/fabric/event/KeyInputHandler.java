package kr.cantua.fabric.event;

import dev.architectury.networking.NetworkManager;
import kr.cantua.network.packet.ChargeJumpStatePacket;
import kr.cantua.packet.ChargeJumpStatePayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;

public class KeyInputHandler {
    private static boolean lastJumping = false;
    private static boolean lastSneaking = false;

    public static void onClientTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        boolean jumping = client.options.jumpKey.isPressed();
        boolean sneaking = client.options.sneakKey.isPressed();

        // 상태 변화가 있을 때만 서버로 전송 (불필요한 네트워크 트래픽 방지)
        if (jumping != lastJumping || sneaking != lastSneaking) {
            lastJumping = jumping;
            lastSneaking = sneaking;

            ChargeJumpStatePayload payload = new ChargeJumpStatePayload(jumping, sneaking);
            NetworkManager.sendToServer(payload);
        }
    }
}
