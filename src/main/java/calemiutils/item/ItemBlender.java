package calemiutils.item;

import calemiutils.config.CUConfig;
import calemiutils.item.base.ItemBase;
import calemiutils.util.helper.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;

import java.util.List;

public class ItemBlender extends ItemBase {

    public ItemBlender() {

        super("blender", 1);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addDisabledLore(tooltip, CUConfig.misc.blenderMaxJuice);
        LoreHelper.addInformationLore(tooltip, "Blends up food into juice which you can drink!");
        LoreHelper.addControlsLore(tooltip, "Drink", LoreHelper.Type.USE);
        LoreHelper.addControlsLore(tooltip, "Toggle Processing Mode", LoreHelper.Type.SNEAK_USE, true);
        tooltip.add("");
        tooltip.add("Process Food: " + ChatFormatting.AQUA + (ItemHelper.getNBT(stack).getBoolean("process") ? "ON" : "OFF"));
        tooltip.add("Juice: " + ChatFormatting.AQUA + StringHelper.printCommas((int) ItemHelper.getNBT(stack).getFloat("juice")) + " / " + StringHelper.printCommas(CUConfig.misc.blenderMaxJuice));
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        if (ItemHelper.getNBT(stack).getBoolean("process")) {

            if (entityIn instanceof EntityPlayer) {

                EntityPlayer player = (EntityPlayer) entityIn;

                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {

                    ItemStack currentStack = player.inventory.getStackInSlot(i);

                    if (!currentStack.isEmpty() && currentStack.getItem() instanceof ItemFood) {

                        float food = ((ItemFood) currentStack.getItem()).getHealAmount(currentStack) / 2;

                        if (ItemHelper.getNBT(stack).getFloat("juice") + food <= CUConfig.misc.blenderMaxJuice) {

                            InventoryHelper.consumeItem(player.inventory, currentStack, 1, false);
                            ItemHelper.getNBT(stack).setFloat("juice", ItemHelper.getNBT(stack).getFloat("juice") + food);
                        }
                    }
                }
            }
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {

        NBTTagCompound nbt = ItemHelper.getNBT(stack);

        FoodStats stats = ((EntityPlayer) entityLiving).getFoodStats();

        float juice = nbt.getFloat("juice");

        int missingFood = 20 - stats.getFoodLevel();

        int addedFood = 0;
        int addedSat = 0;

        if (juice >= 1) {

            if (missingFood > 0) {
                addedFood += 1;
                addedSat += 2;
            }

            decreaseJuice(nbt, 1);

            stats.addStats(addedFood, addedSat);
        }

        return stack;
    }

    private void decreaseJuice(NBTTagCompound nbt, float amount) {

        nbt.setFloat("juice", nbt.getFloat("juice") - amount);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.isSneaking()) {

            ItemHelper.getNBT(itemstack).setBoolean("process", !ItemHelper.getNBT(itemstack).getBoolean("process"));
            SoundHelper.playClick(worldIn, playerIn);

            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        }

        else {

            if (playerIn.getFoodStats().getFoodLevel() < 20) {

                playerIn.setActiveHand(handIn);

                new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
            }
        }

        return new ActionResult<>(EnumActionResult.FAIL, itemstack);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {

        return ItemHelper.getNBT(stack).getBoolean("process");
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {

        return 16;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {

        return EnumAction.DRINK;
    }
}
