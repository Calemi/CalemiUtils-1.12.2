package calemiutils.util.helper;

import calemiutils.util.Location;
import net.minecraft.world.World;

public class PacketHelper {

    public static String sendLocation(Location location) {

        return location.x + "%" + location.y + "%" + location.z + "%";
    }

    public static Location getLocation(World world, String[] array, int index) {

        return new Location(world, Integer.parseInt(array[index]), Integer.parseInt(array[index + 1]), Integer.parseInt(array[index + 2]));
    }
}
