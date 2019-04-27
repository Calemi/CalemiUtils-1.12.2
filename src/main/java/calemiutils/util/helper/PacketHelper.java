package calemiutils.util.helper;

import calemiutils.util.Location;

public class PacketHelper {

    public static String sendLocation(Location location) {

        return location.x + "%" + location.y + "%" + location.z + "%";
    }
}
