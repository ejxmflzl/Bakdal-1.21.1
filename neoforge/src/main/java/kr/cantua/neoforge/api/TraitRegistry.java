package kr.cantua.neoforge.api;

import kr.cantua.Bakdalmain;
import kr.cantua.registries.Trait;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.neoforged.neoforge.capabilities.EntityCapability;
import org.jetbrains.annotations.Nullable;

public class TraitRegistry {
    public static final Identifier ID = Bakdalmain.loc(Trait.name);

    public static final EntityCapability<ITraitCapability,@Nullable Direction> TRAIT =
            EntityCapability.create(
            ID,
            ITraitCapability.class,
            Direction.class
    );

}
