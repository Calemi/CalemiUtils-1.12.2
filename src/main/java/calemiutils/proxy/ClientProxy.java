package calemiutils.proxy;

import calemiutils.init.InitItems;
import calemiutils.item.base.ItemPencilColored;
import calemiutils.render.RenderBuildingUnit;
import calemiutils.render.RenderDiggingUnit;
import calemiutils.render.RenderTradingPost;
import calemiutils.tileentity.TileEntityBuildingUnit;
import calemiutils.tileentity.TileEntityMiningUnit;
import calemiutils.tileentity.TileEntityQuarryUnit;
import calemiutils.tileentity.TileEntityTradingPost;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenders() {

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTradingPost.class, new RenderTradingPost());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBuildingUnit.class, new RenderBuildingUnit());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMiningUnit.class, new RenderDiggingUnit());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuarryUnit.class, new RenderDiggingUnit());

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemPencilColored(), InitItems.PENCIL);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {

        if (item.getRegistryName() != null) ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}