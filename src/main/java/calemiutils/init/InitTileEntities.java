package calemiutils.init;

import calemiutils.CUReference;
import calemiutils.tileentity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class InitTileEntities {

    public static void init() {

        GameRegistry.registerTileEntity(TileEntityTorchPlacer.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityTorchPlacer"));
        GameRegistry.registerTileEntity(TileEntityBank.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityBank"));
        GameRegistry.registerTileEntity(TileEntityNetworkCable.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityNetworkCable"));
        GameRegistry.registerTileEntity(TileEntityNetworkGate.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityNetworkGate"));
        GameRegistry.registerTileEntity(TileEntityInteractionInterface.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityInteractionInterface"));
        GameRegistry.registerTileEntity(TileEntityInteractionTerminal.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityInteractionTerminal"));
        GameRegistry.registerTileEntity(TileEntityMiningUnit.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityMiningUnit"));
        GameRegistry.registerTileEntity(TileEntityQuarryUnit.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityQuarryUnit"));
        GameRegistry.registerTileEntity(TileEntityBuildingUnit.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityBuildingUnit"));
        GameRegistry.registerTileEntity(TileEntityTradingPost.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityTradingPost"));
        GameRegistry.registerTileEntity(TileEntityMarket.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityMarket"));
        GameRegistry.registerTileEntity(TileEntityBookStand.class, new ResourceLocation(CUReference.MOD_ID + ":tileEntityBookStand"));
    }
}
