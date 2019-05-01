package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.config.CUConfig;
import calemiutils.init.InitBlocks;
import calemiutils.init.InitItems;
import calemiutils.registry.IHasModel;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockMarker extends BlockTorch implements IHasModel {

    public BlockMarker() {

        String name = "marker";
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CalemiUtils.TAB);
        setLightLevel(0.5F);
        setSoundType(SoundType.WOOD);

        if (CUConfig.blockUtils.buildingUnit && getRegistryName() != null) {
            InitBlocks.BLOCKS.add(this);
            InitItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
        }
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Used by Building Units to determine its size. Placed vertically or horizontally to cap the max read size.");
    }

    @Override
    public void registerModels() {

        CalemiUtils.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {

    }
}
