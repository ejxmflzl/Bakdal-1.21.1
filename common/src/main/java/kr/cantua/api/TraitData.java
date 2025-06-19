package kr.cantua.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;

public class TraitData {
    private final NbtCompound data;

    public TraitData() {
        this.data = new NbtCompound();
    }
    public TraitData(NbtCompound nbt) {
        this.data = nbt;
    }
    public void setInt(String key, int value) { data.putInt(key, value); }
    public int getInt(String key) { return data.getInt(key); }

    public void setString(String key, String value) { data.putString(key, value); }
    public String getString(String key) { return data.getString(key); }

    public void setDouble(String key, double value) { data.putDouble(key, value); }
    public double getDouble(String key) { return data.getDouble(key); }

    public void setFloat(String key, float value) { data.putFloat(key, value); }
    public float getFloat(String key) { return data.getFloat(key); }

    public void setBoolean(String key, boolean value) { data.putBoolean(key, value); }
    public boolean getBoolean(String key) { return data.getBoolean(key); }

    public NbtCompound toNbt() {
        return data.copy(); // 방어적 복사
    }

    public static TraitData fromNbt(NbtCompound tag) {
        return new TraitData(tag);
    }

}
