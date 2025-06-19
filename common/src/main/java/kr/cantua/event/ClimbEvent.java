package kr.cantua.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ClimbEvent {
    public static void server(ServerPlayerEntity player) {
        double climbSpeed = 0.06;
        Vec3d motion = player.getVelocity();
        player.setVelocity(motion.x,climbSpeed,motion.z);
        player.velocityModified = true;
        player.fallDistance = 0;
    }
    public static void client(PlayerEntity player) {
        double climbSpeed = 0.06;
        Vec3d motion = player.getVelocity();
        player.setVelocity(motion.x,climbSpeed,motion.z);
        player.velocityModified = true;
        player.fallDistance = 0;
    }
}
