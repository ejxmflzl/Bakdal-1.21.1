package kr.cantua.fabric.client;

import kr.cantua.client.KeyBind;
import kr.cantua.fabric.event.GrabKeyHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;

public final class FabricModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 클라이언트 전용 키 바인딩 등록
        KeyBindingRegistryImpl.addCategory(KeyBind.grab.getCategory());
        KeyBindingRegistryImpl.registerKeyBinding(KeyBind.grab);

        // 클라이언트 전용 이벤트 핸들러 등록
        GrabKeyHandler.register();
        JumpChargeHudRenderer.register();
    }
}
