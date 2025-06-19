package kr.cantua.fabric.component;

import kr.cantua.api.ITrait;
import kr.cantua.api.TraitData;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.Map;

public interface ITraitComponent extends ITrait, Component {
    @Override
    default TraitData get(String traitName) {
        return null;
    }

    @Override
    default void set(String traitName, TraitData data) {

    }

    @Override
    default Map<String, TraitData> getAllTraits() {
        return null;
    }

    @Override
    default void readFromNbt(NbtCompound tag) {

    }

    @Override
    default void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {

    }
}
