package kr.cantua.neoforge.api;

import kr.cantua.api.ITrait;
import net.minecraft.entity.player.PlayerEntity;

public interface ITraitCapability extends ITrait {
    void sync(PlayerEntity player);
}
