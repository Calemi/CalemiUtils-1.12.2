package calemiutils.item;

import calemiutils.block.BlockBlueprint;
import calemiutils.gui.GuiPencil;
import calemiutils.init.InitBlocks;
import calemiutils.item.base.ItemBase;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.LoreHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
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

public class ItemPencil extends ItemBase {

    public ItemPencil() {

        super("pencil", 1);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addInformationLore(tooltip, "Places Blueprint. Blueprint can be used for mass building!");
        LoreHelper.addControlsLore(tooltip, "Place Blueprint", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltip, "Change Blueprint Color", LoreHelper.Type.SNEAK_USE);
        tooltip.add("");
        tooltip.add("Color: " + ChatFormatting.AQUA + (EnumDyeColor.byMetadata(getColorMeta(stack)).getDyeColorName()).toUpperCase());
    }

    public int getColorMeta(ItemStack stack) {

        int meta = 11;

        if (ItemHelper.getNBT(stack).hasKey("color")) {
            meta = ItemHelper.getNBT(stack).getInteger("color");
        }

        return meta;
    }

    public void setColorByMeta(ItemStack stack, int meta) {

        ItemHelper.getNBT(stack).setInteger("color", meta);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        BlockBlueprint BLUEPRINT = (BlockBlueprint) InitBlocks.BLUEPRINT;
        Location location = new Location(worldIn, pos);

        if (player.isSneaking()) {
            return EnumActionResult.FAIL;
        }

        if (!location.getBlock().getMaterial(location.getBlockState()).isReplaceable()) {

            location = new Location(location, facing);

            if (!location.isBlockValidForPlacing(InitBlocks.BLUEPRINT)) return EnumActionResult.FAIL;
        }

        if (!player.canPlayerEdit(pos, facing, player.getHeldItem(hand))) return EnumActionResult.FAIL;

        else {

            if (location.isBlockValidForPlacing(InitBlocks.BLUEPRINT)) {
                location.setBlock(BLUEPRINT.getStateFromMeta(getColorMeta(player.getHeldItem(hand))));
            }

            return EnumActionResult.SUCCESS;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack stack = playerIn.getHeldItemMainhand();

        if (worldIn.isRemote && playerIn.isSneaking() && !stack.isEmpty() && stack.getItem() instanceof ItemPencil) {
            openGui(playerIn, stack);
            return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
        }

        return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    }

    @SideOnly(Side.CLIENT)
    private void openGui(EntityPlayer player, ItemStack stack) {

        FMLClientHandler.instance().displayGuiScreen(player, new GuiPencil(player, stack));
    }
}
