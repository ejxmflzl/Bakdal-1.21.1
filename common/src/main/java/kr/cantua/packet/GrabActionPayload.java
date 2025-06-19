package kr.cantua.packet;

import dev.architectury.networking.NetworkManager;
import kr.cantua.Bakdalmain;
import kr.cantua.api.ITrait;
import kr.cantua.api.TraitBridgeInitializer;
import kr.cantua.event.ClimbEvent;
import kr.cantua.logic.ClimbLogic;
import kr.cantua.registries.Trait;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;

public record GrabActionPayload() implements CustomPayload {
    public static final CustomPayload.Id<GrabActionPayload> ID = new CustomPayload.Id<>(Bakdalmain.loc("grab_action"));
    public static final PacketCodec<RegistryByteBuf,GrabActionPayload> CODEC = PacketCodec.unit(new GrabActionPayload());
    public GrabActionPayload(RegistryByteBuf buf) {
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
            GrabActionPayload.ID,
            GrabActionPayload.CODEC,
            (payload,context) -> {
                context.queue(() -> {
                    PlayerEntity player = context.getPlayer();
                    ITrait trait = TraitBridgeInitializer.Holder.get().get(player);
                    var traitData = trait.get(Trait.loc.toString());
                    double climb_height = traitData.getDouble(Trait.Double.CLIMB_HEIGHT.name());
                    double climb_range = traitData.getDouble(Trait.Double.CLIMB_RANGE.name());
                    if (ClimbLogic.canClimb(player,climb_range,climb_height) && !player.isSneaking()) {
                        ClimbEvent.client(player);
                    }
                });
            });
    }
}
