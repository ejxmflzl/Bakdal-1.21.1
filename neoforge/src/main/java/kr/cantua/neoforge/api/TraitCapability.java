package kr.cantua.neoforge.api;

import kr.cantua.api.TraitData;
import kr.cantua.registries.Trait;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

import java.util.HashMap;
import java.util.Map;

public class TraitCapability implements ITraitCapability {
    private final Map<String, TraitData> traits = new HashMap<>();
    public TraitCapability() {
        traits.put(Trait.name, Trait.create());
    }

    @Override
    public void sync(PlayerEntity player) {

    }

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
        NbtCompound nbt = new NbtCompound();
        // write traits to NBT
        var list = new net.minecraft.nbt.NbtList();
        for (var entry : traits.entrySet()) {
            NbtCompound t = new NbtCompound();
            t.putString("Name", entry.getKey());
            t.put("Data", entry.getValue().toNbt());
            list.add(t);
        }
        nbt.put("Traits", list);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        traits.clear();
        var list = tag.getList("Traits", 10); // 10 = CompoundTag
        for (var nbtBase : list) {
            NbtCompound compound = (NbtCompound) nbtBase;
            String name = compound.getString("Name");
            TraitData data = TraitData.fromNbt(compound.getCompound("Data"));
            traits.put(name, data);
        }

    }
}
