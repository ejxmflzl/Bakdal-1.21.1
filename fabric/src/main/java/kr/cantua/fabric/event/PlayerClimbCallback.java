package kr.cantua.fabric.event;

import kr.cantua.logic.ClimbLogic;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

@FunctionalInterface
public interface PlayerClimbCallback {
    void onClimbToggle(PlayerEntity player, boolean isClimbing);

    Event<PlayerClimbCallback> EVENT = EventFactory.createArrayBacked(PlayerClimbCallback.class,
            listeners -> (player,isClimb) -> {
        for (PlayerClimbCallback listener : listeners) {
            listener.onClimbToggle(player,isClimb);
        }
    });
}
