package kr.cantua.api;

import net.minecraft.entity.Entity;

public interface TraitBridgeInitializer {
    class Holder {
        private static TraitBridgeInitializer INSTANCE;

        public static TraitBridgeInitializer get() {
            if (INSTANCE == null) throw new IllegalStateException("TraitBridgeInitializer not initialized!");
            return INSTANCE;
        }

        public static void set(TraitBridgeInitializer instance) {
            INSTANCE = instance;
        }
    }

    ITrait get(Entity entity);
}
