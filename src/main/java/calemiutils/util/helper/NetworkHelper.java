package calemiutils.util.helper;

import calemiutils.config.CUConfig;
import calemiutils.security.ISecurity;
import calemiutils.tileentity.TileEntityBank;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.VeinScan;
import net.minecraft.util.EnumFacing;

public class NetworkHelper {

    public static Location getConnectedBank(TileEntityBase teBase) {

        if (teBase != null && teBase.getLocation() != null) {

            VeinScan scan = new VeinScan(teBase.getLocation());
            scan.startNetworkScan(EnumFacing.VALUES);

            TileEntityBank returnBank = null;
            Location bankLocation = null;

            for (Location location : scan.buffer) {

                if (location.getTileEntity() instanceof TileEntityBank) {

                    TileEntityBank bank = (TileEntityBank) location.getTileEntity();

                    if (teBase instanceof ISecurity && CUConfig.misc.useSecurity) {

                        ISecurity security = (ISecurity) teBase;

                        if (bank.getSecurityProfile().isOwner(security.getSecurityProfile().getOwnerName())) {

                            if (returnBank == null || returnBank.getStoredCurrency() < bank.getStoredCurrency()) {
                                returnBank = bank;
                            }
                        }
                    }

                    else if (returnBank == null || returnBank.getStoredCurrency() < bank.getStoredCurrency()) {
                        returnBank = bank;
                    }
                }
            }

            if (returnBank != null) {
                bankLocation = returnBank.getLocation();
            }

            return bankLocation;
        }

        return null;
    }
}
