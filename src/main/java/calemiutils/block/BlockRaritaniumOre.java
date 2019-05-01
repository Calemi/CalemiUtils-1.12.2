package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockBase;
import calemiutils.config.CUConfig;
import calemiutils.init.InitItems;
import calemiutils.util.MaterialSound;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockRaritaniumOre extends BlockBase {

    private final Random rand = new Random();

    public BlockRaritaniumOre() {

        super("raritanium_ore", MaterialSound.STONE, 3, 2, 3);
        setCreativeTab(CalemiUtils.TAB);
        if (CUConfig.blockUtils.raritaniumOre) addBlock();
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Found between Y levels " + CUConfig.worldGen.raritaniumOreGenMinY + " and " + CUConfig.worldGen.raritaniumOreGenMaxY + ".");
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {

        return MathHelper.getInt(rand, 1, 3);
    }

    public int quantityDroppedWithBonus(int fortune, Random random) {

        if (fortune > 0) {

            int j = random.nextInt(fortune + 2) - 1;

            if (j < 0) j = 0;

            return quantityDropped(random) * (j + 1);
        }

        return quantityDropped(random);
    }

    @Override
    public int quantityDropped(Random rand) {

        return MathHelper.getInt(rand, 1, 3);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {

        return InitItems.RARITANIUM;
    }
}
