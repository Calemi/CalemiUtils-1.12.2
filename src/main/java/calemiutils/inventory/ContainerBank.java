package calemiutils.inventory;

import calemiutils.init.InitItems;
import calemiutils.inventory.base.ContainerBase;
import calemiutils.inventory.base.SlotFilter;
import calemiutils.tileentity.TileEntityBank;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBank extends ContainerBase {

    public ContainerBank(EntityPlayer player, TileEntityBank tileEntity) {

        super(player, tileEntity);

        addPlayerInv(8, 62);
        addPlayerHotbar(8, 120);

        addSlotToContainer(new SlotFilter(tileEntity, 0, 62, 18, InitItems.COIN_PENNY, InitItems.COIN_NICKEL, InitItems.COIN_QUARTER, InitItems.COIN_DOLLAR));
        addSlotToContainer(new SlotFilter(tileEntity, 1, 98, 18, InitItems.WALLET));
    }
}
