package calemiutils.item.base;

import calemiutils.CalemiUtils;
import calemiutils.init.InitItems;
import calemiutils.registry.IHasModel;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name) {

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CalemiUtils.TAB);
    }

    protected ItemBase(String name, int stackSize) {

        this(name);
        setMaxStackSize(stackSize);
    }

    public ItemBase addItem() {

        InitItems.ITEMS.add(this);
        return this;
    }

    @Override
    public void registerModels() {

        CalemiUtils.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
