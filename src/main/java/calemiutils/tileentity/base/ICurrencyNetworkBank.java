package calemiutils.tileentity.base;

public interface ICurrencyNetworkBank extends INetwork {

    int getStoredCurrency();

    int getMaxCurrency();

    void setCurrency(int amount);
}
