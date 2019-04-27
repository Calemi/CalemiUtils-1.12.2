package calemiutils.proxy;

import net.minecraft.item.Item;

public interface IProxy {

    void registerRenders();

    void registerItemRenderer(Item item, int meta, String id);
}
