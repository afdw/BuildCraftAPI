/* Copyright (c) 2011-2018, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License. Please check the contents of the license, which
 * should be located as "LICENSE.API" in the BuildCraft source code distribution. */

package buildcraft.api.core;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * A cuboid volume. One of BuildCraft's implementations is mutable, so you should not cache instances that you do not
 * own as-is, without making an immutable copy first.
 */
public interface IBox extends IZone {
    BlockPos min();

    BlockPos max();

    IBox setMin(BlockPos min);

    IBox setMax(BlockPos max);

    default IBox expand(int amount) {
        Vec3i amountVector = new BlockPos(amount, amount, amount);
        return setMin(min().subtract(amountVector)).setMax(max().add(amountVector));
    }

    default IBox contract(int amount) {
        return expand(-amount);
    }

    default BlockPos size() {
        return max().subtract(min()).add(1, 1, 1);
    }

    @Override
    default boolean contains(Vec3d point) {
        AxisAlignedBB aabb = getBoundingBox();
        return point.x >= aabb.minX && point.x < aabb.maxX &&
            point.y >= aabb.minY && point.y < aabb.maxY &&
            point.z >= aabb.minZ && point.z < aabb.maxZ;
    }

    default boolean contains(BlockPos i) {
        return contains(new Vec3d(i));
    }

    default Vec3d centerExact() {
        return new Vec3d(size()).scale(0.5).add(new Vec3d(min()));
    }

    default BlockPos center() {
        return new BlockPos(centerExact());
    }

    default BlockPos closestInsideTo(BlockPos pos) {
        return new BlockPos(
            MathHelper.clamp(pos.getX(), min().getX(), max().getX()),
            MathHelper.clamp(pos.getY(), min().getY(), max().getY()),
            MathHelper.clamp(pos.getZ(), min().getZ(), max().getZ())
        );
    }

    @Override
    default double distanceToSquared(BlockPos pos) {
        return closestInsideTo(pos).distanceSq(pos);
    }

    @Override
    default BlockPos getRandomBlockPos(Random rand) {
        BlockPos min = min();
        BlockPos max = max().add(1, 1, 1);
        return new BlockPos(
            min.getX() + rand.nextInt(max.getX() - min.getX()),
            min.getY() + rand.nextInt(max.getY() - min.getY()),
            min.getZ() + rand.nextInt(max.getZ() - min.getZ())
        );
    }

    /**
     * IMPORTANT: Use {@link #contains(Vec3d)} instead of the returned {@link AxisAlignedBB#contains(Vec3d)} as the
     * logic is different!
     */
    default AxisAlignedBB getBoundingBox() {
        return new AxisAlignedBB(min(), max().add(1, 1, 1));
    }

    default boolean doesIntersectWith(IBox box) {
        return min().getX() <= box.max().getX() && max().getX() >= box.min().getX() &&
            min().getY() <= box.max().getY() && max().getY() >= box.min().getY() &&
            min().getZ() <= box.max().getZ() && max().getZ() >= box.min().getZ();
    }

    /**
     * @return The intersection box (if these two boxes are intersecting) or null if they were not.
     */
    @Nullable
    default IBox intersect(IBox box) {
        if (doesIntersectWith(box)) {
            return setMin(new BlockPos(
                Math.max(min().getX(), box.min().getX()),
                Math.max(min().getY(), box.min().getY()),
                Math.max(min().getZ(), box.min().getZ())
            )).setMax(new BlockPos(
                Math.min(max().getX(), box.max().getX()),
                Math.min(max().getY(), box.max().getY()),
                Math.min(max().getZ(), box.max().getZ())
            ));
        }
        return null;
    }

    default IBox extendToEncompass(IBox toBeContained) {
        return extendToEncompassBoth(toBeContained.min(), toBeContained.max());
    }

    default IBox extendToEncompass(BlockPos toBeContained) {
        return setMin(new BlockPos(
            Math.min(min().getX(), toBeContained.getX()),
            Math.min(min().getY(), toBeContained.getY()),
            Math.min(min().getZ(), toBeContained.getZ())
        )).setMax(new BlockPos(
            Math.max(max().getX(), toBeContained.getX()),
            Math.max(max().getY(), toBeContained.getY()),
            Math.max(max().getZ(), toBeContained.getZ())
        ));
    }

    default IBox extendToEncompassBoth(BlockPos newMin, BlockPos newMax) {
        return setMin(new BlockPos(
            Math.min(Math.min(min().getX(), newMin.getX()), newMax.getX()),
            Math.min(Math.min(min().getY(), newMin.getY()), newMax.getY()),
            Math.min(Math.min(min().getZ(), newMin.getZ()), newMax.getZ())
        )).setMax(new BlockPos(
            Math.max(Math.max(max().getX(), newMin.getX()), newMax.getX()),
            Math.max(Math.max(max().getY(), newMin.getY()), newMax.getY()),
            Math.max(Math.max(max().getZ(), newMin.getZ()), newMax.getZ())
        ));
    }
}
