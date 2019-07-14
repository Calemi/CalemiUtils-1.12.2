package calemiutils.inventory;

import calemiutils.inventory.base.ContainerBase;
import calemiutils.inventory.base.SlotFilter;
import calemiutils.tileentity.base.TileEntityInventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerOneSlot extends ContainerBase {

    public ContainerOneSlot(EntityPlayer player, TileEntityInventoryBase te, Item... filters) {

        super(player, te);

        addPlayerInv(8, 41);
        addPlayerHotbar(8, 99);

        addSlotToContainer(new SlotFilter(te, 0, 80, 18, filters));
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {

        tileEntity.markForUpdate();
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
}
