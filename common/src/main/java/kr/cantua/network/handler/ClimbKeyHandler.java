package kr.cantua.network.handler;

import dev.architectury.networking.NetworkManager;
import kr.cantua.packet.ClimbStatePayload;

public class ClimbKeyHandler {
    public static void sendClimbState(boolean climbing) {
        ClimbStatePayload payload = new ClimbStatePayload(climbing);
        NetworkManager.sendToServer(payload);
    }
}
