package kr.cantua.fabric.component;

import kr.cantua.Bakdalmain;
import kr.cantua.api.TraitData;
import kr.cantua.registries.Trait;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class TraitComponentInitializer implements EntityComponentInitializer {
    public static final ComponentKey<ITraitComponent> TRAIT = ComponentRegistry.getOrCreate(
            Bakdalmain.loc(Trait.name), ITraitComponent.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TRAIT, player -> {
            var component = new TraitComponentImpl();
            TraitData trait = Trait.create();
            component.set(Trait.loc.toString(), trait);
            return component;
        });
    }

    public static ITraitComponent get(PlayerEntity player) {
        return TRAIT.get(player);
    }
}
