package kr.cantua.packet;

import dev.architectury.networking.NetworkManager;
import kr.cantua.Bakdalmain;
import kr.cantua.api.TraitBridge;
import kr.cantua.registries.Trait;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record ChargeJumpStatePayload(boolean jumping, boolean sneaking) implements CustomPayload {
    public static final CustomPayload.Id<ChargeJumpStatePayload> ID = new CustomPayload.Id<>(
            Bakdalmain.loc("jump_sneak_state"));

    public static final PacketCodec<RegistryByteBuf, ChargeJumpStatePayload> CODEC = new PacketCodec<>() {
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
}
