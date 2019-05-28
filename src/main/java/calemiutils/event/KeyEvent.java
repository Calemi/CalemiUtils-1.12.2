package calemiutils.event;

import calemiutils.CalemiUtils;
import calemiutils.key.KeyBindings;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.util.helper.CurrencyHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyEvent {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {

        if (KeyBindings.openWalletButton.isPressed()) {

            EntityPlayer player = Minecraft.getMinecraft().player;

            ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player);

            if (!walletStack.isEmpty()) {

                CalemiUtils.network.sendToServer(new ServerPacketHandler("gui-open%" + CalemiUtils.guiIdWallet));
            }
        }
    }
}
