package kr.cantua.packet;

import kr.cantua.Bakdalmain;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ClimbStatePayload(boolean climbing) implements CustomPayload {
    public static final CustomPayload.Id<ClimbStatePayload> ID = new CustomPayload.Id<>(Bakdalmain.loc("climb_state"));

    public static final PacketCodec<RegistryByteBuf, ClimbStatePayload> CODEC = new PacketCodec<>() {
        @Override
        public ClimbStatePayload decode(RegistryByteBuf buf) {
            return new ClimbStatePayload(buf.readBoolean());
        }

        @Override
        public void encode(RegistryByteBuf buf, ClimbStatePayload payload) {
            buf.writeBoolean(payload.climbing());
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}