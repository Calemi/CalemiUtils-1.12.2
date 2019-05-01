package calemiutils.event;

import calemiutils.config.CUConfig;
import calemiutils.init.InitItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class JoinEvent {

    private static final String NBT_KEY = "cu.first-join";

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {

        if (CUConfig.wallet.startingWallet && CUConfig.itemUtils.wallet) {
            return;
        }

        //Code Credit (diesieben07)
        NBTTagCompound data = event.player.getEntityData();
        NBTTagCompound persistent;

        if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            data.setTag(EntityPlayer.PERSISTED_NBT_TAG, (persistent = new NBTTagCompound()));
        }

        else {
            persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        }

        if (!persistent.hasKey(NBT_KEY)) {

            persistent.setBoolean(NBT_KEY, true);
            event.player.inventory.addItemStackToInventory(new ItemStack(InitItems.WALLET));
        }
    }

}
