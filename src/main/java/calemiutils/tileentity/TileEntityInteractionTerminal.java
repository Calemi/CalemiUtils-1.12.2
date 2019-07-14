package calemiutils.tileentity;

import calemiutils.item.ItemInteractionInterfaceFilter;
import calemiutils.security.ISecurity;
import calemiutils.security.SecurityProfile;
import calemiutils.tileentity.base.INetwork;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.VeinScan;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class TileEntityInteractionTerminal extends TileEntityBase implements INetwork, ISecurity {

    private final SecurityProfile profile = new SecurityProfile();

    private VeinScan scan;
    public final List<Location> blocksToInteract = new ArrayList<>();

    @Override
    public void update() {

        if (getLocation() != null && scan == null) {
            scan = new VeinScan(getLocation());
        }
    }

    public void huntForInterfaces() {

        blocksToInteract.clear();
        scan.reset();
        scan.startNetworkScan(getConnectedDirections());

        for (Location location : scan.buffer) {

            if (location.getTileEntity() instanceof TileEntityInteractionInterface) {

                TileEntityInteractionInterface tileInterface = ((TileEntityInteractionInterface) location.getTileEntity());

                Location locationBlock = new Location(tileInterface.getLocation(), EnumFacing.UP);

                if (tileInterface.getSecurityProfile().isOwner(profile.getOwnerName())) {

                    if (!locationBlock.isAirBlock()) {

                        blocksToInteract.add(locationBlock);
                    }
                }
            }
        }
    }

    public ItemStack getFilterStack(Location location) {

        TileEntityInteractionInterface tileInterface = ((TileEntityInteractionInterface) location.getTileEntity());
        ItemStack stack = tileInterface.tabIconStack;

        if (!stack.isEmpty() && stack.getItem() instanceof ItemInteractionInterfaceFilter) {
            return stack;
        }

        return null;
    }

    private ItemInteractionInterfaceFilter getFilter(Location location) {

        TileEntityInteractionInterface tileInterface = ((TileEntityInteractionInterface) location.getTileEntity());
        ItemStack stack = tileInterface.tabIconStack;

        if (getFilterStack(location) != null) {
            return (ItemInteractionInterfaceFilter) stack.getItem();
        }

        return null;
    }

    private ItemStack getFilterTabIcon(Location location) {

        if (getFilter(location) != null) {
            return ItemInteractionInterfaceFilter.getFilterIcon(getFilterStack(location));
        }

        return null;
    }

    private String getFilterName(Location location) {

        if (getFilter(location) != null) {
            return ItemInteractionInterfaceFilter.getFilterName(getFilterStack(location));
        }

        return "";
    }

    public boolean isValidFilter(Location location) {

        ItemStack filterIcon = getFilterTabIcon(location);

        if (location != null && getFilter(location) != null && filterIcon != null) {
            return !filterIcon.isEmpty() && !getFilterName(location).isEmpty();
        }

        return false;
    }

    @Override
    public SecurityProfile getSecurityProfile() {

        return profile;
    }

    @Override
    public EnumFacing[] getConnectedDirections() {

        return new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
    }
}
