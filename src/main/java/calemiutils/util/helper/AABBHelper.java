package calemiutils.util.helper;

import net.minecraft.util.math.AxisAlignedBB;

public class AABBHelper {

    public static AxisAlignedBB addTwoBoundingBoxes(AxisAlignedBB box1, AxisAlignedBB box2) {

        double minX = Math.min(box1.minX, box2.minX);
        double minY = Math.min(box1.minY, box2.minY);
        double minZ = Math.min(box1.minZ, box2.minZ);

        double maxX = Math.max(box1.maxX, box2.maxX);
        double maxY = Math.max(box1.maxY, box2.maxY);
        double maxZ = Math.max(box1.maxZ, box2.maxZ);

        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
