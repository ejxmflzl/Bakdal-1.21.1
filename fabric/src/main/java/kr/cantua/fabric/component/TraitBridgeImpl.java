package kr.cantua.fabric.component;

import kr.cantua.api.ITrait;
import kr.cantua.api.ITraitImpl;
import kr.cantua.api.TraitBridgeInitializer;
import kr.cantua.registries.Trait;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.*;

public class TraitBridgeImpl implements TraitBridgeInitializer {

    @Override
    public ITrait get(Entity entity) {
        if (entity instanceof PlayerEntity player) {
            return TraitComponents.get(player); // 또는 패브릭 컴포넌트 시스템을 통해 가져오기
        }
        return new ITraitImpl(); // fallback
    }
}
