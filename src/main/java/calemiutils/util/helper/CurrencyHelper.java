package calemiutils.util.helper;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.Baubles;
import calemiutils.config.CUConfig;
import calemiutils.item.ItemWallet;
import calemiutils.tileentity.base.ICurrencyNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class CurrencyHelper {

    public static ItemStack getCurrentWalletStack(EntityPlayer player) {

        if (player.getHeldItemMainhand().getItem() instanceof ItemWallet) {
            return player.getHeldItemMainhand();
        }

        if (player.getHeldItemOffhand().getItem() instanceof ItemWallet) {
            return player.getHeldItemOffhand();
        }

        if (Loader.isModLoaded(Baubles.MODID)) {

            IBaublesItemHandler container = BaublesApi.getBaublesHandler(player);

            for (int i = 0; i < container.getSlots(); i++) {

                ItemStack stack = container.getStackInSlot(i);
                
                if (stack.getItem() instanceof ItemWallet) {
                    return stack;
                }
            }
        }

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {

            ItemStack stack = player.inventory.getStackInSlot(i);

            if (stack.getItem() instanceof ItemWallet) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    public static boolean canFitAddedCurrencyToNetwork(ICurrencyNetwork network, int addAmount) {

        return network.getStoredCurrency() + addAmount <= network.getMaxCurrency();
    }

    public static boolean canFitAddedCurrencyToWallet(ItemStack walletStack, int addAmount) {

        if (!walletStack.isEmpty() && walletStack.getItem() instanceof ItemWallet) {

            return ItemWallet.getBalance(walletStack) + addAmount <= CUConfig.wallet.walletCurrencyCapacity;
        }

        return false;
    }
}
