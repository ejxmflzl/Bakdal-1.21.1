package kr.cantua.fabric.event;

import kr.cantua.api.ITrait;
import kr.cantua.client.KeyBind;
import kr.cantua.event.ClimbEvent;
import kr.cantua.fabric.component.TraitComponents;
import kr.cantua.logic.ClimbLogic;
import kr.cantua.registries.Trait;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;


public class EventHandler {
    public static void register() {

        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()){
                boolean current = TraitComponents.get(player).get(Trait.loc.toString()).getBoolean(Trait.Bool.IS_CLIMBING.name());

                if (player.isClimbing()) {
                    //boolean isClimbing = TraitComponents.get(server.getCommandSource().getPlayer()).get(Trait.loc.toString()).getBoolean(Trait.Bool.IS_CLIMBING.name());
                    if (!ClimbLogic.canClimb(player,0.03,2)) {
                        TraitComponents.get(player).get(Trait.loc.toString()).setBoolean(Trait.Bool.IS_CLIMBING.name(),false);
                        return;
                    }

                    ClimbEvent.server(player);
                }
            }
        });
    }
}
