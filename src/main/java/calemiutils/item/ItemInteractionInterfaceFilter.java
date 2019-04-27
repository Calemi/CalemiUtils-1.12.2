package calemiutils.item;

import calemiutils.CalemiUtils;
import calemiutils.item.base.ItemBase;
import calemiutils.tileentity.TileEntityInteractionInterface;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.LoreHelper;
import calemiutils.util.helper.NBTHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemInteractionInterfaceFilter extends ItemBase {

    public ItemInteractionInterfaceFilter() {

        super("interaction_interface_filter", 1);
    }

    public static ItemStack getFilterIcon(ItemStack stack) {

        NBTTagCompound nbt = ItemHelper.getNBT(stack);
        return NBTHelper.loadItem(nbt);
    }

    public static void setFilterIcon(ItemStack stack, ItemStack filterStack) {

        NBTTagCompound nbt = ItemHelper.getNBT(stack);
        NBTHelper.saveItem(nbt, filterStack);
    }

    public static String getFilterName(ItemStack stack) {

        NBTTagCompound nbt = ItemHelper.getNBT(stack);

        if (nbt.hasKey("filterName")) {
            return nbt.getString("filterName");
        }

        return "";
    }

    public static void setFilterName(ItemStack stack, String string) {

        NBTTagCompound nbt = ItemHelper.getNBT(stack);
        nbt.setString("filterName", string);
    }

    public static boolean isSameFilter(ItemStack stack1, ItemStack stack2) {

        if (ItemStack.areItemStacksEqual(getFilterIcon(stack1), getFilterIcon(stack2))) {

            return getFilterName(stack1).equalsIgnoreCase(getFilterName(stack2));

        }

        return false;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addInformationLore(tooltip, "Placed in Interaction Interfaces. Used to add custom tabs inside the Interaction Terminal to sort your buttons.");
        LoreHelper.addControlsLore(tooltip, "Open Inventory", LoreHelper.Type.USE, true);
        tooltip.add("");
        tooltip.add("Filter Icon: " + ChatFormatting.AQUA + (getFilterIcon(stack).isEmpty() ? "Not set" : getFilterIcon(stack).getDisplayName()));
        tooltip.add("Filter Name: " + ChatFormatting.AQUA + (getFilterName(stack).isEmpty() ? "Not set" : getFilterName(stack)));
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

        Location location = new Location(world, pos);
        ItemStack heldItem = player.getHeldItem(hand);

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemInteractionInterfaceFilter && location.getTileEntity() != null && location.getTileEntity() instanceof TileEntityInteractionInterface && location.getIInventory() != null) {

            TileEntityInteractionInterface teInterface = (TileEntityInteractionInterface) location.getTileEntity();

            teInterface.filter = heldItem;
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        if (!worldIn.isRemote && playerIn != null && !playerIn.getHeldItemMainhand().isEmpty()) {

            playerIn.openGui(CalemiUtils.instance, CalemiUtils.guiIdInteractionInterfaceFilter, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
