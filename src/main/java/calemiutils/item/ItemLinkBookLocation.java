package calemiutils.item;

import calemiutils.CalemiUtils;
import calemiutils.gui.GuiLinkBook;
import calemiutils.item.base.ItemBase;
import calemiutils.tileentity.TileEntityBookStand;
import calemiutils.util.Location;
import calemiutils.util.UnitChatMessage;
import calemiutils.util.helper.EntityHelper;
import calemiutils.util.helper.InventoryHelper;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.LoreHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemLinkBookLocation extends ItemBase {

    public ItemLinkBookLocation() {

        super("link_book_location", 1);
        setCreativeTab(CalemiUtils.TAB);
    }

    private static UnitChatMessage getUnitChatMessage(EntityPlayer player) {

        return new UnitChatMessage("Location Link Book", player);
    }

    public static Location getLinkedLocation(World world, ItemStack is) {

        NBTTagCompound nbt = ItemHelper.getNBT(is);

        if (nbt.hasKey("X") && nbt.hasKey("Y") && nbt.hasKey("Z")) {
            return new Location(world, nbt.getInteger("X"), nbt.getInteger("Y"), nbt.getInteger("Z"));
        }

        return null;
    }

    public static int getLinkedDimension(ItemStack is) {

        NBTTagCompound nbt = ItemHelper.getNBT(is);

        if (nbt.hasKey("Dim")) {
            return nbt.getInteger("Dim");
        }

        return 0;
    }

    public static void bindName(ItemStack is, String name) {

        if (!name.isEmpty()) {
            is.setStackDisplayName(name);
        }

        else is.clearCustomName();
    }

    public static void bindLocation(ItemStack is, EntityPlayer player, Location location, boolean printMessage) {

        ItemHelper.getNBT(is).setBoolean("linked", true);

        NBTTagCompound nbt = ItemHelper.getNBT(is);

        nbt.setInteger("X", location.x);
        nbt.setInteger("Y", location.y);
        nbt.setInteger("Z", location.z);
        nbt.setInteger("Dim", player.world.provider.getDimension());
        nbt.setString("DimName", player.world.provider.getDimensionType().name());

        if (!player.world.isRemote && printMessage) {

            getUnitChatMessage(player).printMessage(ChatFormatting.GREEN, "Linked location to " + location.toString());
        }
    }

    public static void resetLocation(ItemStack is, EntityPlayer player) {

        ItemHelper.getNBT(is).setBoolean("linked", false);

        NBTTagCompound nbt = ItemHelper.getNBT(is);

        nbt.removeTag("X");
        nbt.removeTag("Y");
        nbt.removeTag("Z");
        nbt.removeTag("Dim");
        nbt.removeTag("DimName");

        if (!player.world.isRemote) {

            getUnitChatMessage(player).printMessage(ChatFormatting.GREEN, "Cleared Book");
        }
    }

    public static void teleport(World world, EntityPlayer player, Location location, int dim) {

        if (!world.isRemote) {

            EntityHelper.teleportPlayer((EntityPlayerMP) player, location, dim);
            getUnitChatMessage(player).printMessage(ChatFormatting.GREEN, "Teleported you to " + location.toString());
        }

    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        NBTTagCompound nbt = ItemHelper.getNBT(stack);
        Location location = getLinkedLocation(worldIn, stack);

        LoreHelper.addInformationLore(tooltip, "Creates a link to teleport to.");
        LoreHelper.addControlsLore(tooltip, "Open Gui", LoreHelper.Type.USE, true);
        tooltip.add("");

        String locationStr = "Not set";

        if (location != null) {
            locationStr = (location.x + ", " + location.y + ", " + location.z);
        }

        tooltip.add("[Location] " + ChatFormatting.AQUA + locationStr);

        tooltip.add("[Dimension] " + ChatFormatting.AQUA + (nbt.hasKey("DimName") ? nbt.getString("DimName") : "Not set"));
    }

    public boolean isLinked(ItemStack stack) {

        return ItemHelper.getNBT(stack).getBoolean("linked");
    }

    @Override
    public boolean hasEffect(ItemStack stack) {

        return isLinked(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack heldItem = playerIn.getHeldItemMainhand();

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemLinkBookLocation) {

            if (worldIn.isRemote) openGui(playerIn, heldItem);
        }

        return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        Location location = new Location(worldIn, pos);
        ItemStack heldItem = player.getHeldItemMainhand();

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemLinkBookLocation && location.getTileEntity() != null && location.getTileEntity() instanceof TileEntityBookStand && location.getIInventory() != null) {

            if (InventoryHelper.insertHeldItem(player, hand, location, location.getIInventory(), 0, true)) {
                return EnumActionResult.SUCCESS;
            }

            else {

                ItemStack bookInventory = ((IInventory) location.getTileEntity()).getStackInSlot(0);
                Location linkedLocation = ItemLinkBookLocation.getLinkedLocation(worldIn, bookInventory);

                if (!bookInventory.isEmpty() && linkedLocation != null) {

                    bindLocation(heldItem, player, linkedLocation, false);
                    if (bookInventory.hasDisplayName()) bindName(heldItem, bookInventory.getDisplayName());
                    if (!worldIn.isRemote) getUnitChatMessage(player).printMessage(ChatFormatting.GREEN, "Copied Data from Book Stand");
                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.FAIL;
    }

    @SideOnly(Side.CLIENT)
    public void openGui(EntityPlayer player, ItemStack stack) {

        FMLClientHandler.instance().displayGuiScreen(player, new GuiLinkBook(player, stack));
    }
}