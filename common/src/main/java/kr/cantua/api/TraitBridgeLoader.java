package kr.cantua.api;

public class TraitBridgeLoader {
    public static TraitBridgeInitializer load() {
        // 이 메소드는 runtime에서 플랫폼 구현체를 반환해야 합니다.
        // 실제 구현체는 각 플랫폼에서 제공
        throw new IllegalStateException("No TraitBridgeInitializer implementation found");
    }
}
