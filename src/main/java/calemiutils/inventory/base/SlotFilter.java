package calemiutils.inventory.base;

import calemiutils.tileentity.base.TileEntityInventoryBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlotFilter extends Slot {

    private final List<Item> itemFilters;
    //private List<Object> objFilters;

    public SlotFilter(TileEntityInventoryBase tileEntity, int id, int x, int y, Item... filters) {

        super(tileEntity, id, x, y);

        this.itemFilters = new ArrayList<>();

        this.itemFilters.addAll(Arrays.asList(filters));

        tileEntity.containerSlots[id] = this;
    }

    public SlotFilter(IInventory inv, int id, int x, int y, Item... filters) {

        super(inv, id, x, y);

        this.itemFilters = new ArrayList<>();

        this.itemFilters.addAll(Arrays.asList(filters));
    }

    /*public SlotFilter(IInventory inv, int id, int x, int y, Object... filters) {

        super(inv, id, x, y);

        this.objFilters = new ArrayList<>();

        this.objFilters.addAll(Arrays.asList(filters));
    }*/

    private boolean isFilter(ItemStack stack) {

        if (this.itemFilters != null) {

            for (Item itemFilter : this.itemFilters) {

                if (itemFilter == stack.getItem()) return true;
            }
        }

        /*if (this.objFilters != null) {

            for (Object objectFilter : this.objFilters) {

                if (stack.getItem() instanceof ItemBlock) {

                    ItemBlock item = (ItemBlock) stack.getItem();

                    if (item.getBlock().getClass().equals(objectFilter) || item.getBlock().getClass().isInstance(objectFilter)) return true;
                }

                if (stack.getItem().getClass().isInstance(objectFilter)) return true;
            }
        }*/

        return false;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {

        return isFilter(stack);
    }
}
