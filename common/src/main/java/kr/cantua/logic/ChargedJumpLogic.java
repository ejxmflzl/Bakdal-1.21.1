package kr.cantua.logic;

import kr.cantua.api.ITrait;
import kr.cantua.api.TraitBridge;
import kr.cantua.client.KeyBind;
import kr.cantua.registries.Trait;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ChargedJumpLogic {

    private static final double MAX_JUMP_POWER = 0.6;
    private static final double MAX_FORWARD_DISTANCE = 0.0;

    public static void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            processPlayerJump(player);
        }
    }

    private static void processPlayerJump(ServerPlayerEntity player) {
        ITrait trait = TraitBridge.get(player);

        double gauge = trait.get(Trait.loc.toString()).getDouble(Trait.Double.JUMP_GAUGE.name());
        double gaugeMax = trait.get(Trait.loc.toString()).getDouble(Trait.Double.JUMP_GAUGE_MAX.name());
        boolean jumpHeld = trait.get(Trait.loc.toString()).getBoolean(Trait.Bool.IS_JUMP_HELD.name());
        boolean isJumping = trait.get(Trait.loc.toString()).getBoolean(Trait.Bool.IS_CHARGED_JUMPING.name());

        // 점프 발동 조건: 게이지 가득 + 점프키 놓음 + 착지 중 + 점프 중 아님
        if (!jumpHeld && gauge >= gaugeMax && player.isOnGround() && !isJumping) {
            triggerChargedJump(player, trait);
        }

        // 점프 후 착지 시 상태 초기화
        if (player.isOnGround() && isJumping) {
            trait.get(Trait.loc.toString()).setBoolean(Trait.Bool.IS_CHARGED_JUMPING.name(), false);
        }
    }

    private static void triggerChargedJump(ServerPlayerEntity player, ITrait trait) {
        Vec3d look = player.getRotationVec(1.0F);
        Vec3d forward = new Vec3d(look.x, 0, look.z).normalize();

        Vec3d velocity = new Vec3d(
                forward.x * MAX_FORWARD_DISTANCE,
                MAX_JUMP_POWER,
                forward.z * MAX_FORWARD_DISTANCE
        );
        player.setVelocity(velocity);
        player.velocityModified = true;

        // 상태 변경
        trait.get(Trait.loc.toString()).setBoolean(Trait.Bool.IS_CHARGED_JUMPING.name(), true);
        trait.get(Trait.loc.toString()).setBoolean(Trait.Bool.IS_JUMP_CHARGING.name(), false);
        trait.get(Trait.loc.toString()).setDouble(Trait.Double.JUMP_GAUGE.name(), 0);

        // TODO: 클라이언트 애니메이션 동기화를 위한 패킷 전송 Hook 가능
        // NetworkHandler.sendToClient(new ChargedJumpActivatedPacket(), player);
        player.sendMessage(Text.of("Hi"));
        // 로그 출력 (디버깅 전용)
    }
}
