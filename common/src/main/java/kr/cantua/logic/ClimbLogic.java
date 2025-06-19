package kr.cantua.logic;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public class ClimbLogic {
    private static final boolean DEBUG = false;

    /**
     * 플레이어가 해당 거리 앞 벽을 최대 maxClimbHeight 까지 오를 수 있는지 판단
     */
    public static boolean canClimb(PlayerEntity player, double distance, double maxClimbHeight) {
        if (isHeadBlocked(player)) {
            sendDebug(player, "Cannot climb: head is blocked");
            return false;
        }

        double climbHeight = getFrontWallClimbHeight(player, distance, maxClimbHeight);
        sendDebug(player, "Final climb height = " + climbHeight);

        return climbHeight > 0 && climbHeight <= maxClimbHeight;
    }

    /**
     * 플레이어 머리 위 3칸에 블럭이 존재하는지 확인
     */
    private static boolean isHeadBlocked(PlayerEntity player) {
        World world = player.getWorld();
        int x = MathHelper.floor(player.getX());
        int z = MathHelper.floor(player.getZ());
        int headY = MathHelper.floor(player.getBoundingBox().maxY);

        for (int y = headY; y <= headY + 2; y++) {
            BlockPos pos = new BlockPos(x, y, z);
            if (!world.getBlockState(pos).isAir()) {
                sendDebug(player, "HeadBlockCheck at " + pos.toShortString() + ": blocked");
                return true;
            }
        }
        return false;
    }

    /**
     * 플레이어가 바라보는 방향 앞에 있는 벽의 등반 가능 최대 높이 반환
     */
    public static double getFrontWallClimbHeight(PlayerEntity player, double distance, double maxClimbHeight) {
        World world = player.getWorld();
        Vec3d flatLook = getFlatLookVector(player);

        if (flatLook.lengthSquared() == 0) {
            sendDebug(player, "Look vector is zero");
            return 0;
        }

        Box bb = player.getBoundingBox();
        BlockPos[] targetPositions = getTargetBlockPositions(bb, flatLook, distance);

        BlockPos firstBlockPos = targetPositions[0];
        BlockPos secondBlockPos = targetPositions[1];

        BlockState firstBlockState = world.getBlockState(firstBlockPos);
        BlockState secondBlockState = world.getBlockState(secondBlockPos);

        // 첫 번째 블럭이 공기거나 비솔리드일 때 두 번째 블럭 체크
        if (firstBlockState.isAir() || !firstBlockState.isSolid()) {
            if (secondBlockState.isSolid()) {
                sendDebug(player, "First block air/non-solid but second block solid, climbing from second block at " + secondBlockPos.toShortString());
                return getWallHeightFrom(world, player, secondBlockPos, bb.minY, maxClimbHeight);
            } else {
                sendDebug(player, "First and second blocks are air/non-solid, climb not possible");
                return 0;
            }
        }

        // 첫 번째 블럭이 솔리드인 경우
        sendDebug(player, "Climbing from first block at " + firstBlockPos.toShortString());
        return getWallHeightFrom(world, player, firstBlockPos, bb.minY, maxClimbHeight);
    }

    /**
     * 시작 위치부터 maxClimbHeight 범위 내에서 등반 가능한 벽의 높이를 탐색
     */
    private static double getWallHeightFrom(World world, PlayerEntity player, BlockPos startPos, double feetY, double maxClimbHeight) {
        int baseX = startPos.getX();
        int baseZ = startPos.getZ();
        int startY = startPos.getY();
        int endY = startY + (int) maxClimbHeight;

        double maxTopY = 0;
        boolean foundWall = false;

        for (int y = startY; y <= endY; y++) {
            BlockPos pos = new BlockPos(baseX, y, baseZ);
            BlockState state = world.getBlockState(pos);

            if (state.isAir() || !state.isSolid()) continue;

            VoxelShape shape = getEffectiveShape(state, world, pos);
            if (shape.isEmpty()) continue;

            for (Box box : shape.getBoundingBoxes()) {
                double boxMaxY = y + box.maxY;
                if (boxMaxY <= feetY) continue;

                if (boxMaxY > maxTopY) {
                    maxTopY = boxMaxY;
                    foundWall = true;
                }

                sendDebug(player, String.format("Box at Y=%d maxY=%.3f", y, boxMaxY));
            }
        }

        if (!foundWall) return 0;

        double climbHeight = maxTopY - feetY;
        return (climbHeight <= maxClimbHeight) ? climbHeight : 0;
    }

    /**
     * 블럭 상태로부터 충돌 혹은 외곽 모양 VoxelShape 반환
     */
    private static VoxelShape getEffectiveShape(BlockState state, World world, BlockPos pos) {
        VoxelShape shape = state.getOutlineShape(world, pos);
        return shape.isEmpty() ? state.getCollisionShape(world, pos) : shape;
    }

    /**
     * 플레이어의 바닥 바운딩 박스와 평면 바라보는 방향을 기반으로,
     * 체크할 첫 번째, 두 번째 블럭 위치 반환 (BlockPos[2])
     */
    private static BlockPos[] getTargetBlockPositions(Box bb, Vec3d flatLook, double distance) {
        double offsetX = (flatLook.x > 0) ? bb.maxX : bb.minX;
        double offsetZ = (flatLook.z > 0) ? bb.maxZ : bb.minZ;

        double checkX = offsetX + flatLook.x * distance;
        double checkZ = offsetZ + flatLook.z * distance;

        int baseX = MathHelper.floor(checkX);
        int baseZ = MathHelper.floor(checkZ);
        int baseY = MathHelper.floor(bb.minY);

        BlockPos firstBlockPos = new BlockPos(baseX, baseY, baseZ);
        BlockPos secondBlockPos = firstBlockPos.up();

        return new BlockPos[]{firstBlockPos, secondBlockPos};
    }

    /**
     * 플레이어의 바라보는 방향에서 Y축을 제거하여 평면 벡터로 반환
     */
    private static Vec3d getFlatLookVector(PlayerEntity player) {
        Vec3d lookVec = player.getRotationVec(1.0F);
        Vec3d flatLook = new Vec3d(lookVec.x, 0, lookVec.z);
        return flatLook.lengthSquared() == 0 ? Vec3d.ZERO : flatLook.normalize();
    }

    /**
     * 디버그 메시지 출력 (DEBUG 활성화 시)
     */
    private static void sendDebug(PlayerEntity player, String message) {
        if (DEBUG) {
            player.sendMessage(Text.of("[ClimbDebug] " + message), false);
        }
    }
}
