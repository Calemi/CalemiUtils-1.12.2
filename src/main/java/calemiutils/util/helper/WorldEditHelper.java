package calemiutils.util.helper;

import calemiutils.config.CUConfig;
import calemiutils.util.Location;
import calemiutils.util.UnitChatMessage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;

public class WorldEditHelper {

    public static void generateCommand(ArrayList<Location> list, IBlockState block, Block mask, boolean isAirMask, EntityPlayer player, UnitChatMessage message) {

        if (CUConfig.blockScans.worldEditMaxSize == 0) {

            if (!player.world.isRemote) {

                message.printMessage(TextFormatting.RED, "The Brush is disabled by config!");
            }

            return;
        }

        if (list.size() > CUConfig.blockScans.worldEditMaxSize) {

            if (!player.world.isRemote) {

                message.printMessage(TextFormatting.RED, "Too many blocks to fill!");
                message.printMessage(TextFormatting.RED, "You are " + StringHelper.printCommas(list.size() - CUConfig.blockScans.worldEditMaxSize) + " blocks over!");
            }

            return;
        }

        int count = 0;

        for (Location nextLocation : list) {

            if (isAirMask && nextLocation.isBlockValidForPlacing(mask)) {

                count++;
                nextLocation.setBlock(block, player);
            }

            else if (nextLocation.getBlock() == mask) {

                count++;
                nextLocation.setBlock(block, player);
            }
        }

        if (!player.world.isRemote) {

            message.printMessage(TextFormatting.GREEN, "Placed " + ItemHelper.countByStacks(count));
        }

        if (count > 0) {

            if (player.world.isRemote) {
                SoundHelper.playDing(player.world, player);
            }
        }

    }

    public static ArrayList<Location> selectCubeFromRadius(Location location, int xRad, int yRad, int zRad) {

        return selectCubeFromRadius(location, xRad, -yRad, yRad, zRad);
    }

    private static ArrayList<Location> selectCubeFromRadius(Location location, int xRad, int yRadMin, int yRadMax, int zRad) {

        ArrayList<Location> list = new ArrayList<>();

        for (int x = -xRad; x <= xRad; x++) {

            for (int y = yRadMin; y <= yRadMax; y++) {

                for (int z = -zRad; z <= zRad; z++) {

                    Location nextLocation = new Location(location.world, location.x + x, location.y + y, location.z + z);
                    list.add(nextLocation);
                }
            }
        }

        return list;
    }

    public static ArrayList<Location> selectCubeFromTwoPoints(Location loc1, Location loc2) {

        ArrayList<Location> list = new ArrayList<>();

        for (int x = Math.min(loc1.x, loc2.x); x <= Math.max(loc1.x, loc2.x); x++) {

            for (int y = Math.min(loc1.y, loc2.y); y <= Math.max(loc1.y, loc2.y); y++) {

                for (int z = Math.min(loc1.z, loc2.z); z <= Math.max(loc1.z, loc2.z); z++) {

                    list.add(new Location(loc1.world, x, y, z));
                }
            }
        }

        return list;
    }

    public static ArrayList<Location> selectWallsFromRadius(Location location, int xzRad, int yRad) {

        return selectWallsFromRadius(location, xzRad, -yRad, yRad);
    }

    public static ArrayList<Location> selectWallsFromRadius(Location location, int xzRad, int yRadMin, int yRadMax) {

        ArrayList<Location> list = new ArrayList<>();

        if (xzRad < 0) {
            return list;
        }

        else if (xzRad == 0) {

            for (int y = yRadMin; y <= yRadMax; y++) {
                list.add(new Location(location.world, location.x, location.y + y, location.z));
            }
        }

        else {

            for (int i = -xzRad; i <= xzRad; i++) {

                for (int y = yRadMin; y <= yRadMax; y++) {
                    list.add(new Location(location.world, location.x + xzRad, location.y + y, location.z + i));
                    list.add(new Location(location.world, location.x - xzRad, location.y + y, location.z + i));
                }
            }

            for (int i = -xzRad + 1; i <= xzRad - 1; i++) {

                for (int y = yRadMin; y <= yRadMax; y++) {
                    list.add(new Location(location.world, location.x + i, location.y + y, location.z + xzRad));
                    list.add(new Location(location.world, location.x + i, location.y + y, location.z - xzRad));
                }
            }
        }

        return list;
    }

    public static ArrayList<Location> selectCircleFromTwoPoints(Location loc1, Location loc2) {

        ArrayList<Location> list = new ArrayList<>();

        int radius = (int) loc1.getDistance(loc2);

        for (int x = -radius; x <= radius; x++) {

            for (int z = -radius; z <= radius; z++) {

                int rx = loc1.x + x;
                int rz = loc1.z + z;

                Location nextLoc = new Location(loc1.world, rx, loc1.y, rz);

                if (loc1.getDistance(nextLoc) <= radius) {

                    list.add(nextLoc);
                }
            }
        }

        return list;
    }
}
