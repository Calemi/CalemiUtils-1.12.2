package calemiutils.item;

import calemiutils.CalemiUtils;
import calemiutils.config.CUConfig;
import calemiutils.item.base.ItemBase;
import calemiutils.util.Location;
import calemiutils.util.helper.*;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemBuildersKit extends ItemBase {

    public ItemBuildersKit() {

        super("builders_kit", 1);
        if (CUConfig.itemUtils.buildersKit) addItem();
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addDisabledLore(tooltip, CUConfig.misc.buildersKitCapacity);
        LoreHelper.addInformationLore(tooltip, "Stores huge amounts of the same block type that can be placed! Can also be used on Blueprints!");
        LoreHelper.addControlsLore(tooltip, "Place Block", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltip, "Open Inventory", LoreHelper.Type.SNEAK_USE);
        LoreHelper.addControlsLore(tooltip, "Place the blocks stored into the Blueprints", LoreHelper.Type.LEFT_CLICK_BLUEPRINT);
        tooltip.add("");
        tooltip.add("Block: " + ChatFormatting.AQUA + getBlockName(stack));
        tooltip.add("Amount: " + ChatFormatting.AQUA + StringHelper.printCommas(getAmountOfBlocks(stack)) + " / " + StringHelper.printCommas(CUConfig.misc.buildersKitCapacity));
        tooltip.add("Suck: " + ChatFormatting.AQUA + (ItemHelper.getNBT(stack).getBoolean("suck") ? "ON" : "OFF"));
    }

    public String getBlockName(ItemStack stack) {

        String name = "Not set";

        ItemStack stackFilter = ((ItemBuildersKit) stack.getItem()).getBlockType(stack);

        if (stackFilter != null && !stackFilter.isEmpty()) {
            name = stackFilter.getDisplayName();
        }

        return name;
    }

    public ItemStack getBlockType(ItemStack stack) {

        return NBTHelper.loadItem(ItemHelper.getNBT(stack), 0);
    }

    public static int getAmountOfBlocks(ItemStack stack) {

        NBTTagCompound nbt = ItemHelper.getNBT(stack);

        if (nbt == null) {
            return 0;
        }

        return nbt.getInteger("amount");
    }

    public static void setAmountOfBlocks(ItemStack stack, int amount) {

        ItemHelper.getNBT(stack).setInteger("amount", amount);
    }

    public static int addAmountOfBlocks(ItemStack stack, int amount) {

        int amountToSet = getAmountOfBlocks(stack) + amount;
        int amountAdded = amount;

        if (amountToSet > CUConfig.misc.buildersKitCapacity) {
            amountToSet = CUConfig.misc.buildersKitCapacity;
            amountAdded = CUConfig.misc.buildersKitCapacity - getAmountOfBlocks(stack);
        }

        setAmountOfBlocks(stack, amountToSet);

        return amountAdded;
    }

    /*public boolean canAddAmount(ItemStack stack, int amount) {

        int amountToSet = getAmountOfBlocks(stack) + amount;

        return amountToSet <= CUConfig.misc.buildersKitCapacity;
    }*/

    public void toggleSuck(ItemStack stack) {

        ItemHelper.getNBT(stack).setBoolean("suck", !ItemHelper.getNBT(stack).getBoolean("suck"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (!player.isSneaking()) {

            ItemStack heldStack = player.getHeldItemMainhand();

            if (!heldStack.isEmpty()) {

                Location hit = new Location(worldIn, pos);

                ItemStack filterStack = getBlockType(heldStack);

                if (!hit.getBlock().getMaterial(hit.getBlockState()).isReplaceable()) {
                    hit = new Location(hit, facing);
                }

                if (filterStack != null && filterStack.getItem() instanceof ItemBlock) {

                    ItemBlock itemBlock = (ItemBlock) filterStack.getItem();

                    Location playerLoc = new Location(player.world, (int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));

                    if (getAmountOfBlocks(heldStack) > 0 && itemBlock.getBlock().canPlaceBlockOnSide(worldIn, hit.getBlockPos(), facing) && !(playerLoc).equals(hit) && !(new Location(playerLoc, EnumFacing.UP)).equals(hit)) {

                        IBlockState state = Block.getBlockFromItem(filterStack.getItem()).getStateForPlacement(player.world, hit.getBlockPos(), facing, 0, 0, 0, filterStack.getItemDamage(), player, EnumHand.MAIN_HAND);

                        hit.setBlock(state, player);
                        setAmountOfBlocks(heldStack, getAmountOfBlocks(heldStack) - 1);
                        SoundHelper.playBlockPlaceSound(player.world, player, itemBlock.getBlock().getDefaultState(), hit);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        if (!worldIn.isRemote && CUConfig.misc.buildersKitCapacity > 0 && playerIn != null && !playerIn.getHeldItemMainhand().isEmpty()) {

            if (playerIn.isSneaking()) {
                playerIn.openGui(CalemiUtils.instance, CalemiUtils.guiIdBuildersKit, worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        if (entityIn.world.getWorldTime() % 2 == 0) {

            if (entityIn instanceof EntityPlayer) {

                EntityPlayer player = (EntityPlayer) entityIn;

                if (ItemHelper.getNBT(stack).getBoolean("suck") && getBlockType(stack) != null) {

                    int count = InventoryHelper.countItems(player.inventory, false, false, getBlockType(stack));

                    if (count > 64) {
                        count = 64;
                    }

                    if (count > 0) InventoryHelper.consumeItem(player.inventory, addAmountOfBlocks(stack, count), false, getBlockType(stack));
                }
            }
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {

        return ItemHelper.getNBT(stack).getBoolean("suck");
    }
}
