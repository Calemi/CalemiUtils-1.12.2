package calemiutils.event;

import calemiutils.config.CUConfig;
import calemiutils.item.ItemWallet;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.StringHelper;
import calemiutils.util.helper.CurrencyHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OverlayEvent {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Post event) {

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;

        ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player);

        //Wallet Currency
        if (CUConfig.wallet.walletOverlay && !walletStack.isEmpty() && Minecraft.getMinecraft().currentScreen == null) {

            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {

                int currency = ItemWallet.getBalance(walletStack);

                Minecraft.getMinecraft().fontRenderer.drawString(StringHelper.printCurrency(currency), 21, 4, 0xFFFFFFFF, true);
            }

            if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
                GuiHelper.drawItemStack(mc.getRenderItem(), walletStack, 2, 0);
            }
        }
    }
}
