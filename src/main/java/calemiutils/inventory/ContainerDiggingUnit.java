package calemiutils.inventory;

import calemiutils.init.InitItems;
import calemiutils.inventory.base.ContainerBase;
import calemiutils.inventory.base.SlotFilter;
import calemiutils.tileentity.base.TileEntityDiggingUnitBase;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerDiggingUnit extends ContainerBase {

    public ContainerDiggingUnit(EntityPlayer player, TileEntityDiggingUnitBase te) {

        super(player, te);

        addPlayerInv(8, 143);
        addPlayerHotbar(8, 201);

        addSlotToContainer(new SlotFilter(te, 0, 179, 7, InitItems.SPEED_UPGRADE));
        addSlotToContainer(new SlotFilter(te, 1, 179, 31, InitItems.RANGE_UPGRADE));
        addTileEntityStorageInv(te, 2, 8, 85, 3);
    }
}
