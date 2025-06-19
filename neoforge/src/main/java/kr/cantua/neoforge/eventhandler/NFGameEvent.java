package kr.cantua.neoforge.eventhandler;

import kr.cantua.Bakdalmain;
import kr.cantua.neoforge.api.TraitRegistry;
import kr.cantua.registries.Trait;
import net.minecraft.text.Text;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Bakdalmain.MOD_ID,bus = EventBusSubscriber.Bus.GAME)
public class NFGameEvent {
    @SubscribeEvent
    public static void registerCapability(PlayerEvent.PlayerLoggedInEvent event) {
        int a = TraitRegistry.TRAIT.getCapability(event.getEntity(),null).get(Trait.name).getInt(Trait.Int.VAULT_LEVEL.name());
        event.getEntity().sendMessage(Text.of(String.valueOf(a)),false);
    }
}
