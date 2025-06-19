package kr.cantua;

import kr.cantua.packet.ChargeJumpStatePayload;
import kr.cantua.packet.CrawingActionPayload;
import kr.cantua.packet.GrabActionPayload;
import kr.cantua.packet.SetCrawingPayload;
import net.minecraft.util.Identifier;

import java.nio.channels.NetworkChannel;

public final class Bakdalmain {
    public static final String MOD_ID = "bakdal";

    public static void init() {
        GrabActionPayload.register();
        CrawingActionPayload.register();
        SetCrawingPayload.registerClient();

    }

    public static Identifier loc(String path) {
        return Identifier.of(MOD_ID,path);
    }

    public static String id(String path) {
        return String.valueOf(MOD_ID+":"+path);
    }



}
