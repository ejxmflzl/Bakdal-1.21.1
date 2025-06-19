package kr.cantua.registries;

import kr.cantua.Bakdalmain;
import kr.cantua.api.TraitData;
import net.minecraft.util.Identifier;

public class Trait {
    public static final String name = "trait";
    public static final Identifier loc = Bakdalmain.loc(name);
    public enum Bool {
        IS_CLIMBING,
        IS_CRAWING,
        IS_HANGING,
        IS_SLIDING,
        IS_DOG_MOVING,
        IS_FOX_MOVING,
        IS_SPRINT_CHARGING,
        IS_JUMP_CHARGING,
        IS_GRAB_HELD,
        IS_JUMP_HELD,
        IS_CHARGED_JUMPING,
        IS_SNEAK_HELD
    }
    public enum Int {
        VAULT_LEVEL
    }
    public enum Float {
    }
    public enum Double {
        CLIMB_HEIGHT,
        CLIMB_RANGE,
        SPRINT_GAUGE,
        JUMP_GAUGE,
        SPRINT_GAUGE_MAX,
        JUMP_GAUGE_MAX
    }
    public enum Str {
    }

    public static TraitData create() {
        TraitData data = new TraitData();
        for (Bool trait : Bool.values()) {
            data.setBoolean(trait.name(), false);
        }
        for (Int trait : Int.values()) {
            data.setInt(trait.name(), 0);
        }
        data.setDouble(Double.CLIMB_RANGE.name(), 0.3);
        data.setDouble(Double.CLIMB_HEIGHT.name(), 2.0);
        data.setDouble(Double.SPRINT_GAUGE.name(), 0);
        data.setDouble(Double.SPRINT_GAUGE_MAX.name(), 1.5);

        data.setDouble(Double.JUMP_GAUGE.name(), 0);
        data.setDouble(Double.JUMP_GAUGE_MAX.name(), 1.5);
        return data;
    }

}
