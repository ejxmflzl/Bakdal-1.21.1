package kr.cantua.packet;

import dev.architectury.networking.NetworkManager;
import kr.cantua.Bakdalmain;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SetCrawingPayload() implements CustomPayload {
    public static final Id<SetCrawingPayload> ID = new Id<>(Bakdalmain.loc("set_crawling_pose"));
    public static final PacketCodec<RegistryByteBuf, SetCrawingPayload> CODEC = PacketCodec.unit(new SetCrawingPayload());

    public SetCrawingPayload(RegistryByteBuf buf) {
        this();
    }

    public void write(RegistryByteBuf buf) {}
    public Id<? extends CustomPayload> getId() { return ID; }

    public static void registerClient() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, ID, CODEC, (payload, context) -> {
            context.queue(() -> {
                if (context.getPlayer() != null) {
                    context.getPlayer().recalculateDimensions(EntityType.PLAYER.getDimensions().scaled(0.5f));
                    context.getPlayer().calculateDimensions();
                }
            });
        });
    }
}
