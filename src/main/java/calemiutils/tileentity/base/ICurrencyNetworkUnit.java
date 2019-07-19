package calemiutils.tileentity.base;

import calemiutils.util.Location;

public interface ICurrencyNetworkUnit extends INetwork {

    Location getBankLocation();
    void setBankLocation(Location location);
}
