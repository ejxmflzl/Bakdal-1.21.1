package kr.cantua.fabric;

import kr.cantua.api.TraitBridgeInitializer;
import kr.cantua.client.KeyBind;
import kr.cantua.fabric.component.TraitBridgeImpl;
import kr.cantua.fabric.component.TraitComponentInitializer;
import kr.cantua.fabric.event.ChargedJumpHandler;
import kr.cantua.fabric.event.GrabKeyHandler;
import kr.cantua.fabric.event.EventHandler;
import kr.cantua.logic.JumpBlockerServerLogic;
import kr.cantua.packet.CrawingActionPayload;
import kr.cantua.packet.GrabActionPayload;
import kr.cantua.packet.SetCrawingPayload;
import net.fabricmc.api.ModInitializer;

import kr.cantua.Bakdalmain;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        TraitBridgeInitializer.Holder.set(new TraitBridgeImpl());
        Bakdalmain.init();  // <-- 공통 모드 초기화, 패킷 등록 포함
        EventHandler.register();
        ChargedJumpHandler.register();
        ServerTickEvents.START_SERVER_TICK.register(JumpBlockerServerLogic::onTick);

    }

}
