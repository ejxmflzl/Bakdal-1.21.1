package kr.cantua.network;

import dev.architectury.networking.NetworkManager;
import kr.cantua.packet.ChargeJumpStatePayload;
import kr.cantua.packet.ClimbStatePayload;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;

import java.util.List;

public class BakdalNetwork {
    public static void register() {
        // 점프+쉬프트 패킷 등록
        NetworkManager.registerS2CPayloadType(ChargeJumpStatePayload.ID, ChargeJumpStatePayload.CODEC);

        // 서버에서 수신 처리 (클라이언트 → 서버)
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ChargeJumpStatePayload.ID, ChargeJumpStatePayload.CODEC, (payload, ctx) -> {
            ServerPlayerEntity player = (ServerPlayerEntity) ctx.getPlayer();
            boolean jumping = payload.jumping();
            boolean sneaking = payload.sneaking();

            // 서버 검증 (예: 치트 방지)
            if (canAcceptJumpSneak(player, jumping, sneaking)) {
                // 상태 갱신 및 다른 플레이어들에게 동기화 (브로드캐스트)
                broadcastJumpSneakState(player, jumping, sneaking);
            }
        });
    }

    private static boolean canAcceptJumpSneak(ServerPlayerEntity player, boolean jumping, boolean sneaking) {
        // 실제 검증 로직 작성 (예: 플레이어 상태, 주변 환경, 서버 설정 등)
        return true;
    }

    private static void broadcastJumpSneakState(ServerPlayerEntity player, boolean jumping, boolean sneaking) {
        ChargeJumpStatePayload payload = new ChargeJumpStatePayload(jumping, sneaking);
        // 대상 플레이어 리스트 구하기 (서버 내 모든 플레이어나 특정 반경 내 플레이어 등)
        Iterable<ServerPlayerEntity> players = getNearbyPlayers(player);
        NetworkManager.sendToPlayers(players, payload);
    }

    private static Iterable<ServerPlayerEntity> getNearbyPlayers(ServerPlayerEntity player) {
        Box box = new Box(
                player.getX() - 50, player.getY() - 50, player.getZ() - 50,
                player.getX() + 50, player.getY() + 50, player.getZ() + 50
        );

        TargetPredicate predicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(50);

        List<PlayerEntity> players = player.getWorld().getPlayers(predicate, player, box);

        // 안전한 캐스팅 및 필터링
        return players.stream()
                .filter(p -> p instanceof ServerPlayerEntity)
                .map(p -> (ServerPlayerEntity) p)
                .toList();
    }
}
