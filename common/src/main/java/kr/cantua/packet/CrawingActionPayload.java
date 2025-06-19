package kr.cantua.packet;

import dev.architectury.networking.NetworkManager;
import kr.cantua.Bakdalmain;
import kr.cantua.api.ITrait;
import kr.cantua.api.TraitBridgeInitializer;
import kr.cantua.event.ClimbEvent;
import kr.cantua.logic.ClimbLogic;
import kr.cantua.registries.Trait;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

public record CrawingActionPayload() implements CustomPayload {
    public static final CustomPayload.Id<CrawingActionPayload> ID = new CustomPayload.Id<>(Bakdalmain.loc("crawing_action"));
    public static final PacketCodec<RegistryByteBuf,CrawingActionPayload> CODEC = PacketCodec.unit(new CrawingActionPayload());
    public CrawingActionPayload(RegistryByteBuf buf) {
        this();
    }

    public void write(RegistryByteBuf buf) {}
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void register() {
        NetworkManager.registerReceiver(
                NetworkManager.Side.C2S,
                CrawingActionPayload.ID,
                CrawingActionPayload.CODEC,
                (payload,context) -> {
                    context.queue(() -> {
                        PlayerEntity player = context.getPlayer();
                        player.recalculateDimensions(EntityType.PLAYER.getDimensions().scaled(0.5f));
                        player.calculateDimensions();
                        NetworkManager.sendToPlayer((ServerPlayerEntity) player,new SetCrawingPayload());
                    });
                });
    }
}
