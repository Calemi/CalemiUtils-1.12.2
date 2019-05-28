package calemiutils.registry;

import calemiutils.init.InitBlocks;
import calemiutils.init.InitItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
class RegistryHandler {

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {

        event.getRegistry().registerAll(InitBlocks.BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {

        event.getRegistry().registerAll(InitItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {

        for (Block block : InitBlocks.BLOCKS) {

            if (block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
        }

        for (Item item : InitItems.ITEMS) {

            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }
    }
}
