package calemiutils.item;

import calemiutils.block.BlockBlueprint;
import calemiutils.item.base.ItemBase;
import calemiutils.util.Location;
import calemiutils.util.VeinScan;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemEraser extends ItemBase {

    public ItemEraser() {

        super("eraser", 1);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addInformationLore(tooltip, "Destroys blueprint.");
        LoreHelper.addControlsLore(tooltip, "Erases one blueprint", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltip, "Erases all connected blueprints", LoreHelper.Type.SNEAK_USE);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {

        if (state.getBlock() instanceof BlockBlueprint) {
            return 9F;
        }

        return super.getDestroySpeed(stack, state);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        Location location = new Location(worldIn, pos);

        if (location.getBlock() instanceof BlockBlueprint) {

            if (!player.isSneaking()) {
                location.setBlockToAir();
            }

            else {

                VeinScan scan = new VeinScan(location, location.getBlockState());
                scan.startScan();

                for (Location nextLocation : scan.buffer) {

                    nextLocation.setBlockToAir();
                }
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
