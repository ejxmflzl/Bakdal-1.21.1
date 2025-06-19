package kr.cantua.fabric.component;

import kr.cantua.api.TraitData;
import kr.cantua.registries.Trait;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.RespawnableComponent;

import java.util.HashMap;
import java.util.Map;

public class TraitComponentImpl implements ITraitComponent, AutoSyncedComponent,RespawnableComponent<ITraitComponent> {
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
            TraitData data = TraitData.fromNbt(compound.getCompound("Data"));
            traits.put(name, data);
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

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();
        for (Map.Entry<String, TraitData> entry : traits.entrySet()) {
            NbtCompound compound = new NbtCompound();
            compound.putString("Name", entry.getKey());
            compound.put("Data", entry.getValue().toNbt());
            list.add(compound);
        }
        tag.put(Trait.loc.toString(), list);
    }


    @Override
    public void copyForRespawn(ITraitComponent original, RegistryWrapper.WrapperLookup registryLookup, boolean lossless, boolean keepInventory, boolean sameCharacter) {
        if (original instanceof TraitComponentImpl other) {
            this.traits.clear();
            for (Map.Entry<String, TraitData> entry : other.traits.entrySet()) {
                this.traits.put(entry.getKey(), entry.getValue()); // 깊은 복사 필요시 수정
            }
        }
    }
}
