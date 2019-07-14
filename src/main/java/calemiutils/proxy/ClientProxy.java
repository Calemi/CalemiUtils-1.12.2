package calemiutils.proxy;

import calemiutils.event.KeyEvent;
import calemiutils.event.OverlayEvent;
import calemiutils.init.InitItems;
import calemiutils.item.base.ItemCoinColored;
import calemiutils.item.base.ItemPencilColored;
import calemiutils.key.KeyBindings;
import calemiutils.render.RenderBookStand;
import calemiutils.render.RenderBuildingUnit;
import calemiutils.render.RenderDiggingUnit;
import calemiutils.render.RenderTradingPost;
import calemiutils.tileentity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenders() {

        ClientRegistry.registerKeyBinding(KeyBindings.openWalletButton);
        FMLCommonHandler.instance().bus().register(new KeyEvent());

        FMLCommonHandler.instance().bus().register(new OverlayEvent());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTradingPost.class, new RenderTradingPost());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBookStand.class, new RenderBookStand());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBuildingUnit.class, new RenderBuildingUnit());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMiningUnit.class, new RenderDiggingUnit());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityQuarryUnit.class, new RenderDiggingUnit());

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemCoinColored(), InitItems.COIN_PENNY);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemCoinColored(), InitItems.COIN_NICKEL);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemCoinColored(), InitItems.COIN_QUARTER);
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemCoinColored(), InitItems.COIN_DOLLAR);

        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemPencilColored(), InitItems.PENCIL);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {

        if (item.getRegistryName() != null) ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }
}