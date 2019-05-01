package calemiutils.item;

import calemiutils.config.CUConfig;
import calemiutils.event.WrenchEvent;
import calemiutils.item.base.ItemBase;
import calemiutils.security.ISecurity;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.helper.LoreHelper;
import calemiutils.util.helper.SecurityHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemSecurityWrench extends ItemBase {

    public ItemSecurityWrench() {

        super("security_wrench", 1);
        if (CUConfig.itemUtils.securityWrench) addItem();
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

        Location location = new Location(world, pos);

        if (player.isSneaking() && location.getTileEntity() != null && location.getTileEntity() instanceof TileEntityBase) {

            if (location.getTileEntity() instanceof ISecurity) {

                ISecurity security = (ISecurity) location.getTileEntity();

                if (security.getSecurityProfile().isOwner(player.getName()) || player.capabilities.isCreativeMode || !CUConfig.misc.useSecurity) {
                    WrenchEvent.onBlockWrenched(world, location);
                    return EnumActionResult.SUCCESS;

                }

                else SecurityHelper.printErrorMessage(location, player);
            }

            else {
                WrenchEvent.onBlockWrenched(world, location);
            }
        }

        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addInformationLore(tooltip, "Use this wrench to access secured blocks!");
        LoreHelper.addControlsLore(tooltip, "Interact with secured blocks", LoreHelper.Type.USE);
        LoreHelper.addControlsLore(tooltip, "Pick up secured blocks", LoreHelper.Type.SNEAK_USE, true);
    }
}
