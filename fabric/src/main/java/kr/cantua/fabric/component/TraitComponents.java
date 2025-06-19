package kr.cantua.fabric.component;

import kr.cantua.registries.Trait;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class TraitComponents {
    private static final ComponentKey<ITraitComponent> PLAYER_TRAIT = ComponentRegistry.getOrCreate(
            Trait.loc, ITraitComponent.class
    );
    public static ITraitComponent get(PlayerEntity player) {
        return PLAYER_TRAIT.get(player);
    }
}
