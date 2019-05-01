package calemiutils.block;

import calemiutils.config.CUConfig;
import calemiutils.init.InitBlocks;
import calemiutils.tileentity.TileEntityNetworkGate;
import calemiutils.util.Location;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockNetworkGate extends BlockNetworkCableOpaque {

    public final boolean isConnected;

    public BlockNetworkGate(boolean isConnected) {

        super("network_gate_" + (isConnected ? "connected" : "disabled"), isConnected);
        this.isConnected = isConnected;
    }

    public static void setState(boolean value, World worldIn, BlockPos pos) {

        TileEntity tileentity = worldIn.getTileEntity(pos);
        Location location = new Location(worldIn, pos);

        if (value) {
            location.setBlock(InitBlocks.NETWORK_GATE);
        }

        else location.setBlock(InitBlocks.NETWORK_GATE_DISABLED);

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Used to enable and disable networks branches by redstone signal.");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileEntityNetworkGate();
    }
}
