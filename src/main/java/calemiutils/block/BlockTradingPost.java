package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockInventoryContainerBase;
import calemiutils.config.CUConfig;
import calemiutils.init.InitItems;
import calemiutils.item.ItemWallet;
import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.util.*;
import calemiutils.util.helper.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTradingPost extends BlockInventoryContainerBase implements IExtraInformation {

    private static final float pixel = 1F / 16F;
    private static final AxisAlignedBB AABB = new AxisAlignedBB(pixel * 0, pixel * 0, pixel * 0, pixel * 16, pixel * 3, pixel * 16);

    public BlockTradingPost() {

        super("trading_post", MaterialSound.IRON, HardnessConstants.SECURED);
        setCreativeTab(CalemiUtils.TAB);
        if (CUConfig.blockUtils.tradingPost && CUConfig.economy.economy) addBlock();
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Used to buy and sell blocks and items.");
        LoreHelper.addControlsLore(tooltip, "Show Trade Info", LoreHelper.Type.SNEAK_USE, true);
        LoreHelper.addControlsLore(tooltip, "Open Inventory", LoreHelper.Type.USE_WRENCH);
        LoreHelper.addControlsLore(tooltip, "Buy Item", LoreHelper.Type.USE_WALLET);
    }

    @Override
    public void getButtonInformation(List<String> list, World world, Location location, ItemStack stack) {

        TileEntity te = location.getTileEntity();

        if (te instanceof TileEntityTradingPost) {

            TileEntityTradingPost post = (TileEntityTradingPost) te;

            if (post.hasValidTradeOffer) {
                list.add("Trading: " + post.getStackForSale().getDisplayName());
                list.add("Amount: " + StringHelper.printCommas(post.amountForSale));
                list.add("Price: " + StringHelper.printCurrency(post.salePrice));
            }
        }
    }

    @Override
    public ItemStack getButtonIcon(World world, Location location, ItemStack stack) {

        TileEntity te = location.getTileEntity();

        if (te instanceof TileEntityTradingPost) {

            TileEntityTradingPost post = (TileEntityTradingPost) te;

            if (post.hasValidTradeOffer) {
                return post.getStackForSale();
            }
        }

        return stack;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (placer instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) placer;

            if (player.capabilities.isCreativeMode && !player.isSneaking()) {

                Location location = new Location(worldIn, pos);
                TileEntity te = location.getTileEntity();

                if (te instanceof TileEntityTradingPost) {

                    TileEntityTradingPost tePost = (TileEntityTradingPost) te;
                    tePost.adminMode = true;
                    if (!worldIn.isRemote) tePost.getUnitName(player).printMessage(TextFormatting.GREEN, "Admin Mode is enabled for this block. Sneak place this block to disable it.");
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        Location location = new Location(world, pos);

        ItemStack heldStack = player.getHeldItem(hand);
        ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player);

        TileEntity te = location.getTileEntity();

        if (te instanceof TileEntityTradingPost) {

            TileEntityTradingPost tePost = (TileEntityTradingPost) te;
            UnitChatMessage message = tePost.getUnitName(player);

            if (!player.isSneaking() && heldStack.getItem() == InitItems.SECURITY_WRENCH) {

                if (SecurityHelper.openSecuredBlock(location, player, true)) {
                    FMLNetworkHandler.openGui(player, CalemiUtils.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
                }
            }

            else if (!player.isSneaking() && !walletStack.isEmpty()) {
                handleTrade(message, world, player, tePost);
            }

            else if (!world.isRemote) {

                if (tePost.hasValidTradeOffer) {

                    if (tePost.adminMode) {
                        message.printMessage(TextFormatting.GREEN, (tePost.buyMode ? "Buying " : "Selling ") + StringHelper.printCommas(tePost.amountForSale) + " " + tePost.getStackForSale().getDisplayName() + " for " + (tePost.salePrice > 0 ? (StringHelper.printCurrency(tePost.salePrice)) : "free"));
                    }

                    else message.printMessage(TextFormatting.GREEN, tePost.getSecurityProfile().getOwnerName() + " is " + (tePost.buyMode ? "buying " : "selling ") + StringHelper.printCommas(tePost.amountForSale) + " " + tePost.getStackForSale().getDisplayName() + " for " + (tePost.salePrice > 0 ? (StringHelper.printCurrency(tePost.salePrice)) : "free"));
                    message.printMessage(TextFormatting.GREEN, "Hold a wallet in your inventory to make a purchase.");
                }

                else {
                    message.printMessage(TextFormatting.RED, "There is nothing to trade!");
                }
            }
        }

        return true;
    }

    private void handleTrade(UnitChatMessage message, World world, EntityPlayer player, TileEntityTradingPost tePost) {

        ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player);
        ItemWallet wallet = (ItemWallet) walletStack.getItem();

        if (tePost.hasValidTradeOffer) {

            if (tePost.buyMode) {
                handleSell(message, walletStack, world, player, tePost);
            }

            else {
                handlePurchase(message, walletStack, world, player, tePost);
            }
        }

        else if (!world.isRemote) message.printMessage(TextFormatting.RED, "The trade is not set up properly!");
    }

    private void handlePurchase(UnitChatMessage message, ItemStack walletStack, World world, EntityPlayer player, TileEntityTradingPost tePost) {

        if (tePost.getStock() >= tePost.amountForSale || tePost.adminMode) {

            if (ItemWallet.getBalance(walletStack) >= tePost.salePrice) {

                NBTTagCompound nbt = ItemHelper.getNBT(walletStack);

                if (tePost.storedCurrency + tePost.salePrice < CUConfig.misc.postCurrencyCapacity) {

                    ItemStack is = new ItemStack(tePost.getStackForSale().getItem(), tePost.amountForSale, tePost.getStackForSale().getItemDamage());

                    if (tePost.getStackForSale().hasTagCompound()) {
                        is.setTagCompound(tePost.getStackForSale().getTagCompound());
                    }

                    if (tePost.adminMode) {

                        if (!world.isRemote) {

                            EntityItem dropItem;
                            dropItem = ItemHelper.spawnItem(world, player, is);

                            if (is.hasTagCompound()) {
                                dropItem.getItem().setTagCompound(is.getTagCompound());
                            }

                            tePost.storedCurrency += tePost.salePrice;
                            tePost.markForUpdate();

                            nbt.setInteger("balance", nbt.getInteger("balance") - tePost.salePrice);
                        }
                    }

                    else {

                        int count = InventoryHelper.countItems(tePost, false, true, tePost.getStackForSale());

                        if (count >= tePost.amountForSale) {

                            if (!world.isRemote) {

                                EntityItem dropItem;

                                dropItem = ItemHelper.spawnItem(world, player, is);

                                if (tePost.getStackForSale().hasTagCompound()) {
                                    dropItem.getItem().setTagCompound(tePost.getStackForSale().getTagCompound());
                                }
                            }

                            InventoryHelper.consumeItem(0, tePost, tePost.amountForSale, true, true, tePost.getStackForSale());

                            tePost.storedCurrency += tePost.salePrice;
                            tePost.markForUpdate();

                            tePost.writeToNBT(tePost.getTileData());

                            nbt.setInteger("balance", nbt.getInteger("balance") - tePost.salePrice);
                        }
                    }
                }

                else if (!world.isRemote) message.printMessage(TextFormatting.RED, "Full of money!");
            }

            else if (!world.isRemote) message.printMessage(TextFormatting.RED, "You don't have enough money!");
        }

        else if (!world.isRemote) message.printMessage(TextFormatting.RED, "There is not enough items in stock!");
    }

    private void handleSell(UnitChatMessage message, ItemStack walletStack, World world, EntityPlayer player, TileEntityTradingPost tePost) {

        if (InventoryHelper.countItems(player.inventory, true, true, tePost.getStackForSale()) >= tePost.amountForSale) {

            ItemStack is = new ItemStack(tePost.getStackForSale().getItem(), tePost.amountForSale, tePost.getStackForSale().getItemDamage());

            if (InventoryHelper.canInsertItem(is, tePost) || tePost.adminMode) {

                if (CurrencyHelper.canFitAddedCurrencyToWallet(walletStack, tePost.salePrice)) {

                    if (tePost.storedCurrency >= tePost.salePrice || tePost.getStoredCurrencyInBank() >= tePost.salePrice || tePost.adminMode) {

                        NBTTagCompound nbt = ItemHelper.getNBT(walletStack);

                        InventoryHelper.consumeItem(player.inventory, tePost.amountForSale, true, tePost.getStackForSale());

                        if (!tePost.adminMode) {
                            InventoryHelper.insertItem(is, tePost);

                            if (tePost.storedCurrency >= tePost.salePrice) {
                                tePost.setCurrency(tePost.storedCurrency - tePost.salePrice);
                            }

                            else tePost.decrStoredCurrencyInBank(tePost.salePrice);
                        }

                        nbt.setInteger("balance", ItemWallet.getBalance(walletStack) + tePost.salePrice);
                    }

                    else if (!world.isRemote) message.printMessage(TextFormatting.RED, "The Trading Post is out of money");
                }

                else if (!world.isRemote) message.printMessage(TextFormatting.RED, "Your Wallet is full of money!");
            }

            else if (!world.isRemote) message.printMessage(TextFormatting.RED, "The Trading Post's stock is full!");
        }

        else if (!world.isRemote) message.printMessage(TextFormatting.RED, "You do not have the required item(s) the Trading Post is looking for!");
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {

        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

        return AABB;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileEntityTradingPost();
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {

        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {

        return BlockRenderLayer.CUTOUT;
    }

    public boolean isFullCube(IBlockState state) {

        return false;
    }
}
