package kr.cantua.fabric.event;

import dev.architectury.networking.NetworkManager;
import kr.cantua.api.ITrait;
import kr.cantua.api.TraitBridgeInitializer;
import kr.cantua.client.KeyBind;
import kr.cantua.event.ClimbEvent;
import kr.cantua.fabric.component.TraitComponents;
import kr.cantua.logic.ClimbLogic;
import kr.cantua.packet.ChargeJumpStatePayload;
import kr.cantua.packet.CrawingActionPayload;
import kr.cantua.packet.GrabActionPayload;
import kr.cantua.registries.Trait;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityPose;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class GrabKeyHandler {
    private static boolean crawlingTriggered = false;
    private static final double JUMP_CHARGE_RATE = 0.02; // 1초에 20틱 기준 0.4씩 증가 예시

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            var trait = TraitComponents.get(client.player);
            var traitInst = trait.get(Trait.loc.toString());

            boolean grabPressed = KeyBind.grab.isPressed();
            boolean sneaking = client.player.isSneaking();
            boolean jumping = client.player.input.jumping; // 점프 입력 감지

            traitInst.setBoolean("IS_GRAB_HELD", grabPressed);

            // 쉬프트 + 그랩 처리 기존 코드
            if (grabPressed && sneaking) {
                handleShiftAndGrab(client, trait);
            } else if (grabPressed) {
                handleGrabAction(client, trait);
            } else {
                crawlingTriggered = false;
            }

            // --- 새로 추가: 쉬프트 + 점프 감지 및 점프 게이지 증가 ---
            if (sneaking && jumping) {
                double currentCharge = traitInst.getDouble("JUMP_CHARGE");
                currentCharge = Math.min(1.0, currentCharge + JUMP_CHARGE_RATE);
                traitInst.setDouble("JUMP_CHARGE", currentCharge);

                // 서버에 상태 동기화 (jumpHeld = true, sneakHeld = true)
                NetworkManager.sendToServer(new ChargeJumpStatePayload(true, true));
            } else {
                // 점프나 쉬프트가 아니면 점프 게이지 감소 혹은 초기화
                double currentCharge = traitInst.getDouble("JUMP_CHARGE");
                currentCharge = Math.max(0.0, currentCharge - JUMP_CHARGE_RATE);
                traitInst.setDouble("JUMP_CHARGE", currentCharge);

                // 서버에 상태 동기화 (jumpHeld = false, sneakHeld = client.player.isSneaking())
                NetworkManager.sendToServer(new ChargeJumpStatePayload(false, sneaking));
            }
        });
    }

    private static void handleShiftAndGrab(MinecraftClient client, ITrait trait) {
        if (!crawlingTriggered) {
            NetworkManager.sendToServer(new CrawingActionPayload());
            crawlingTriggered = true;
        }
    }

    private static void handleGrabAction(MinecraftClient client, ITrait trait) {
        double range = trait.get(Trait.loc.toString()).getDouble(Trait.Double.CLIMB_RANGE.name());
        double height = trait.get(Trait.loc.toString()).getDouble(Trait.Double.CLIMB_HEIGHT.name());

        if (ClimbLogic.canClimb(client.player, range, height)) {
            NetworkManager.sendToServer(new GrabActionPayload());
        }
    }
}
