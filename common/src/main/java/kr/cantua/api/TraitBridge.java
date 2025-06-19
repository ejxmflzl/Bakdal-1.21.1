package kr.cantua.api;

import net.minecraft.entity.Entity;

public class TraitBridge {
    public static ITrait get(Entity entity) {
        return TraitBridgeInitializer.Holder.get().get(entity);
    }
}
