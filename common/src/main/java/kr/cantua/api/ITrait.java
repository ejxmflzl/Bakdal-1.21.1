package kr.cantua.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

import java.util.Map;

public interface ITrait {
    TraitData get(String traitName);
    void set(String traitName, TraitData data);
    Map<String, TraitData> getAllTraits();

    void readFromNbt(NbtCompound tag);
    void writeToNbt(NbtCompound tag);
}
