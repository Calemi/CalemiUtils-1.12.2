package calemiutils.util.helper;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.Baubles;
import calemiutils.config.CUConfig;
import calemiutils.item.ItemWallet;
import calemiutils.tileentity.base.ICurrencyNetworkBank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

public class CurrencyHelper {

    public static ItemStack getCurrentWalletStack(EntityPlayer player) {
        return getCurrentWalletStack(player, true);
    }

    public static ItemStack getCurrentWalletStack(EntityPlayer player, boolean checkForActivity) {

        if (checkForActivity) checkForActiveWallets(player);

        if (player.getHeldItemMainhand().getItem() instanceof ItemWallet && (!checkForActivity || ItemWallet.isActive(player.getHeldItemMainhand()))) {
            return player.getHeldItemMainhand();
        }

        if (player.getHeldItemOffhand().getItem() instanceof ItemWallet && (!checkForActivity || ItemWallet.isActive(player.getHeldItemOffhand()))) {
            return player.getHeldItemOffhand();
        }

        if (Loader.isModLoaded("baubles")) {

            IBaublesItemHandler container = BaublesApi.getBaublesHandler(player);

            for (int i = 0; i < container.getSlots(); i++) {

                ItemStack stack = container.getStackInSlot(i);
                
                if (stack.getItem() instanceof ItemWallet && (!checkForActivity || ItemWallet.isActive(stack))) {
                    return stack;
                }
            }
        }

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {

            ItemStack stack = player.inventory.getStackInSlot(i);

            if (stack.getItem() instanceof ItemWallet && (!checkForActivity || ItemWallet.isActive(stack))) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    public static List<ItemStack> checkForActiveWallets(EntityPlayer player) {

        List<ItemStack> walletList = new ArrayList<>();

        if (Loader.isModLoaded("baubles")) {

            IBaublesItemHandler container = BaublesApi.getBaublesHandler(player);

            for (int i = 0; i < container.getSlots(); i++) {

                ItemStack stack = container.getStackInSlot(i);

                if (stack.getItem() instanceof ItemWallet) {

                    if (ItemWallet.isActive(stack)) {
                        walletList.add(stack);
                    }
                }
            }
        }

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {

            ItemStack stack = player.inventory.getStackInSlot(i);

            if (stack.getItem() instanceof ItemWallet) {

                if (ItemWallet.isActive(stack)) {
                    walletList.add(stack);
                }
            }
        }

        if (walletList.size() > 1) {

            for (ItemStack stack : walletList) {
                ItemHelper.getNBT(stack).setBoolean("active", false);
                System.out.println("Set Inactive");
            }
        }

        return walletList;
    }

    private static void addWallet(List<ItemStack> walletList, ItemStack stackToAdd) {

        for (ItemStack stackInList : walletList) {

            if (!ItemStack.areItemStacksEqual(stackToAdd, stackToAdd)) {
                walletList.add(stackToAdd);
            }
        }
    }

    public static boolean canFitAddedCurrencyToNetwork(ICurrencyNetworkBank network, int addAmount) {

        return network.getStoredCurrency() + addAmount <= network.getMaxCurrency();
    }

    public static boolean canFitAddedCurrencyToWallet(ItemStack walletStack, int addAmount) {

        if (!walletStack.isEmpty() && walletStack.getItem() instanceof ItemWallet) {

            return ItemWallet.getBalance(walletStack) + addAmount <= CUConfig.wallet.walletCurrencyCapacity;
        }

        return false;
    }
}
