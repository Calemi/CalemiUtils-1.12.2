package calemiutils.tileentity.base;

public interface ICurrencyNetwork extends INetwork {

    int getStoredCurrency();

    int getMaxCurrency();

    void setCurrency(int amount);
}
