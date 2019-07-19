package calemiutils.item.base;

import calemiutils.CalemiUtils;
import calemiutils.init.InitItems;
import calemiutils.registry.IHasModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBase extends Item implements IHasModel {

    private boolean hasEffect = false;

    public ItemBase(String name) {

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CalemiUtils.TAB);
    }

    protected ItemBase(String name, int stackSize) {

        this(name);
        setMaxStackSize(stackSize);
    }

    public ItemBase(String name, boolean shouldRegister) {

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CalemiUtils.TAB);

        if (shouldRegister) addItem();
    }

    public ItemBase addItem() {

        InitItems.ITEMS.add(this);
        return this;
    }

    public ItemBase setEffect() {
        hasEffect = true;
        return this;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {

        return hasEffect;
    }

    @Override
    public void registerModels() {

        CalemiUtils.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
