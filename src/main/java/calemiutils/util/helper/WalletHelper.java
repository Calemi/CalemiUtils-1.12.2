package calemiutils.util.helper;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.Baubles;
import calemiutils.item.ItemWallet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class WalletHelper {

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
}
