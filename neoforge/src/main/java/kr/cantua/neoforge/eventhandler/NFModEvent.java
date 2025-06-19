package kr.cantua.neoforge.eventhandler;

import kr.cantua.Bakdalmain;
import kr.cantua.neoforge.api.TraitCapability;
import kr.cantua.neoforge.api.TraitRegistry;
import net.minecraft.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = Bakdalmain.MOD_ID,bus = EventBusSubscriber.Bus.MOD)
public class NFModEvent {
    @SubscribeEvent
    public static void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                TraitRegistry.TRAIT,EntityType.PLAYER,(player, ctx) -> new TraitCapability()
        );
    }
}
