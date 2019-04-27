package calemiutils.item;

import calemiutils.CalemiUtils;
import calemiutils.config.CUConfig;
import calemiutils.item.base.ItemBase;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

public class ItemWallet extends ItemBase {

    public ItemWallet() {

        super("wallet", 1);
    }

    public int getBalance(ItemStack stack) {

        return ItemHelper.getNBT(stack).getInteger("balance");
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addDisabledLore(tooltip, CUConfig.misc.walletCurrencyCapacity);
        LoreHelper.addInformationLore(tooltip, "Used to store currency in one place.");
        LoreHelper.addControlsLore(tooltip, "Open Inventory", LoreHelper.Type.USE, true);
        tooltip.add("");
        LoreHelper.addCurrencyLore(tooltip, getBalance(stack), CUConfig.misc.walletCurrencyCapacity);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        if (!worldIn.isRemote && CUConfig.misc.walletCurrencyCapacity > 0 && playerIn != null && !playerIn.getHeldItemMainhand().isEmpty()) {

            playerIn.openGui(CalemiUtils.instance, CalemiUtils.guiIdWallet, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
