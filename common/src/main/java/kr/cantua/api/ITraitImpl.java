package kr.cantua.api;

import kr.cantua.registries.Trait;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;

import java.util.HashMap;
import java.util.Map;

public class ITraitImpl implements ITrait {
    private final Map<String, TraitData> traits = new HashMap<>();

    @Override
    public TraitData get(String traitName) {
        return traits.getOrDefault(traitName, new TraitData());
    }

    @Override
    public void set(String traitName, TraitData data) {
        traits.put(traitName, data);
    }

    @Override
    public Map<String, TraitData> getAllTraits() {
        return traits;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        traits.clear();
        NbtList list = tag.getList(Trait.loc.toString(), NbtElement.COMPOUND_TYPE);
        for (NbtElement element : list) {
            NbtCompound compound = (NbtCompound) element;
            String name = compound.getString("Name");
            NbtCompound dataNbt = compound.getCompound("Data");
            traits.put(name, TraitData.fromNbt(dataNbt));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList list = new NbtList();
        for (Map.Entry<String, TraitData> entry : traits.entrySet()) {
            NbtCompound compound = new NbtCompound();
            compound.putString("Name", entry.getKey());
            compound.put("Data", entry.getValue().toNbt());
            list.add(compound);
        }
        tag.put(Trait.loc.toString(), list);
    }
}
