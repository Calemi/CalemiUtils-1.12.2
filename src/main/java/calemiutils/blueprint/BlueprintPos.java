package calemiutils.blueprint;

import calemiutils.tileentity.TileEntityBuildingUnit;
import calemiutils.util.Location;

public class BlueprintPos {

    public final int x;
    public final int y;
    public final int z;
    public final int meta;

    public BlueprintPos(int x, int y, int z, int meta) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.meta = meta;
    }

    public static BlueprintPos fromLocation(Location origin, Location loc, int meta) {

        return new BlueprintPos(loc.x - origin.x, loc.y - origin.y, loc.z - origin.z, meta);
    }

    public Location toLocation(TileEntityBuildingUnit te) {

        return new Location(te.getWorld(), x, y, z).translate(te.getLocation());
    }
}
