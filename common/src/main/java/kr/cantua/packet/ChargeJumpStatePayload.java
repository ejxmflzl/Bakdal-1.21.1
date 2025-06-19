package kr.cantua.packet;

import dev.architectury.networking.NetworkManager;
import kr.cantua.Bakdalmain;
import kr.cantua.api.TraitBridge;
import kr.cantua.api.TraitData;
import kr.cantua.registries.Trait;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

import java.util.List;

public record ChargeJumpStatePayload(boolean jumping, boolean sneaking) implements CustomPayload {
    public static final CustomPayload.Id<ChargeJumpStatePayload> ID =
            new CustomPayload.Id<>(Bakdalmain.loc("charge_jump_state"));

    public static final PacketCodec<RegistryByteBuf, ChargeJumpStatePayload> CODEC =
            new PacketCodec<>() {
                @Override
                public ChargeJumpStatePayload decode(RegistryByteBuf buf) {
                    boolean jumping = buf.readBoolean();
                    boolean sneaking = buf.readBoolean();
                    return new ChargeJumpStatePayload(jumping, sneaking);
                }

                @Override
                public void encode(RegistryByteBuf buf, ChargeJumpStatePayload payload) {
                    buf.writeBoolean(payload.jumping());
                    buf.writeBoolean(payload.sneaking());
                }
            };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void register() {
        NetworkManager.registerReceiver(
                NetworkManager.Side.C2S,
                ChargeJumpStatePayload.ID,
                ChargeJumpStatePayload.CODEC,
                (payload, context) -> {
                    context.queue(() -> {
                        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();

                        // 치트 방지 / 입력 검증
                        if (!canAcceptJumpSneak(player, payload.jumping(), payload.sneaking())) {
                            return;
                        }

                        // Trait 동기화
                        TraitData trait = TraitBridge.get(player).get(Trait.loc.toString());
                        trait.setBoolean(Trait.Bool.IS_JUMP_HELD.name(), payload.jumping());
                        trait.setBoolean(Trait.Bool.IS_SNEAK_HELD.name(), payload.sneaking());

                        // 브로드캐스트
                        Iterable<ServerPlayerEntity> targets = getNearbyPlayersExcludingSelf(player, 50);
                        NetworkManager.sendToPlayers(targets, payload);
                    });
                }
        );
    }

    private static boolean canAcceptJumpSneak(ServerPlayerEntity player, boolean jumping, boolean sneaking) {
        // TODO: 검증 로직 구체화
        return true;
    }

    private static Iterable<ServerPlayerEntity> getNearbyPlayersExcludingSelf(ServerPlayerEntity player, double radius) {
        Box box = new Box(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        TargetPredicate predicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(radius);
        List<PlayerEntity> players = player.getWorld().getPlayers(predicate, player, box);

        return players.stream()
                .filter(p -> p instanceof ServerPlayerEntity && !p.equals(player))
                .map(p -> (ServerPlayerEntity) p)
                .toList();
    }
}
