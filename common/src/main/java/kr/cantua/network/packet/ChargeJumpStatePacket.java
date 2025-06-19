package kr.cantua.network.packet;

import dev.architectury.networking.NetworkManager;
import kr.cantua.Bakdalmain;
import kr.cantua.api.ITrait;
import kr.cantua.api.TraitBridge;
import kr.cantua.api.TraitData;
import kr.cantua.packet.ChargeJumpStatePayload;
import kr.cantua.registries.Trait;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.function.Supplier;

public class ChargeJumpStatePacket {

    public static final Identifier ID = Bakdalmain.loc("charge_jump_state");

    private final ChargeJumpStatePayload payload;

    public ChargeJumpStatePacket(ChargeJumpStatePayload payload) {
        this.payload = payload;
    }

    public static ChargeJumpStatePacket decode(RegistryByteBuf buf) {
        return new ChargeJumpStatePacket(ChargeJumpStatePayload.CODEC.decode(buf));
    }

    public void encode(RegistryByteBuf buf) {
        ChargeJumpStatePayload.CODEC.encode(buf, payload);
    }

    public static void handle(ChargeJumpStatePacket packet, Supplier<NetworkManager.PacketContext> ctxSupplier) {
        NetworkManager.PacketContext ctx = ctxSupplier.get();
        ctx.queue(() -> {
            ServerPlayerEntity player = (ServerPlayerEntity) ctx.getPlayer();
            if (player == null) return;

            boolean jumpHeld = packet.payload.jumping();
            boolean sneakHeld = packet.payload.sneaking();

            // 서버 검증 및 상태 저장 (예: Capability/Component에 기록)
            if (!canAcceptJumpSneak(player, jumpHeld, sneakHeld)) {
                return; // 치트 방지 또는 유효하지 않은 상태면 무시
            }
            TraitData trait = TraitBridge.get(player).get(Trait.loc.toString());
            trait.setBoolean(Trait.Bool.IS_JUMP_CHARGING.name(), true);
            trait.getBoolean(Trait.Bool.IS_JUMP_CHARGING.name());

            // 주변 플레이어 대상 (본인 제외) 리스트 구하기
            Iterable<ServerPlayerEntity> targets = getNearbyPlayersExcludingSelf(player, 50);

            // 상태 동기화 브로드캐스트
            NetworkManager.sendToPlayers(targets, new ChargeJumpStatePacket(packet.payload));
        });
        ctx.setPacketHandled(true);
    }

    private static boolean canAcceptJumpSneak(ServerPlayerEntity player, boolean jumping, boolean sneaking) {
        // TODO: 검증 로직 구현
        return true;
    }

    private static Iterable<ServerPlayerEntity> getNearbyPlayersExcludingSelf(ServerPlayerEntity player, double radius) {
        Box box = new Box(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius);

        TargetPredicate predicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(radius);
        List<PlayerEntity> players = player.getWorld().getPlayers(predicate, player, box);

        return players.stream()
                .filter(p -> p instanceof ServerPlayerEntity && !p.equals(player))
                .map(p -> (ServerPlayerEntity) p)
                .toList();
    }
}
