package calemiutils.item;

import calemiutils.item.base.ItemBase;
import calemiutils.util.Location;
import calemiutils.util.helper.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

public class ItemTorchBelt extends ItemBase {

    public ItemTorchBelt() {

        super("torch_belt", 1);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {

        return ItemHelper.getNBT(stack).getBoolean("on");
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addInformationLore(tooltip, "Place this anywhere in your inventory. Automatically uses and places torches in dark areas.");
        LoreHelper.addControlsLore(tooltip, "Toggle ON/OFF", LoreHelper.Type.USE, true);
        tooltip.add("");
        tooltip.add("Status: " + ChatFormatting.AQUA + (ItemHelper.getNBT(stack).getBoolean("on") ? "ON" : "OFF"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);

        ItemHelper.getNBT(stack).setBoolean("on", !ItemHelper.getNBT(stack).getBoolean("on"));
        SoundHelper.playClick(worldIn, playerIn);

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        if (entityIn instanceof EntityPlayer && ItemHelper.getNBT(stack).getBoolean("on")) {

            EntityPlayer player = (EntityPlayer) entityIn;

            Location location = new Location(worldIn, (int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));

            if (location.getLightValue() <= 7) {

                if (player.capabilities.isCreativeMode || player.inventory.hasItemStack(new ItemStack(Blocks.TORCH))) {

                    if (TorchHelper.canPlaceTorchAt(location)) {

                        location.setBlock(Blocks.TORCH);

                        if (!player.capabilities.isCreativeMode) InventoryHelper.consumeItem(player.inventory, new ItemStack(Blocks.TORCH), 1, true);
                    }
                }
            }
        }
    }
}
