package calemiutils.inventory;

import calemiutils.init.InitItems;
import calemiutils.inventory.base.ContainerBase;
import calemiutils.inventory.base.SlotFilter;
import calemiutils.tileentity.TileEntityTorchPlacer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerTorchPlacer extends ContainerBase {

    public ContainerTorchPlacer(EntityPlayer player, TileEntityTorchPlacer te) {

        super(player, te);

        addPlayerInv(8, 110);
        addPlayerHotbar(8, 168);

        addSlotToContainer(new SlotFilter(tileEntity, 0, 179, 7, InitItems.SPEED_UPGRADE));
        addSlotToContainer(new SlotFilter(tileEntity, 1, 179, 31, InitItems.RANGE_UPGRADE));
        addTileEntityStorageInv(tileEntity, 2, 8, 52, 3);
    }
}
