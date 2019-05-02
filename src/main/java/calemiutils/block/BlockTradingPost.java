package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockInventoryContainerBase;
import calemiutils.config.CUConfig;
import calemiutils.init.InitItems;
import calemiutils.item.ItemWallet;
import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.util.*;
import calemiutils.util.helper.*;
import com.mojang.realmsclient.gui.ChatFormatting;
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
        if (CUConfig.blockUtils.tradingPost && CUConfig.itemUtils.economy) addBlock();
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Used to buy/sell blocks and items.");
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
                    if (!worldIn.isRemote) tePost.getUnitName(player).printMessage(ChatFormatting.GREEN, "Admin Mode is enabled for this block. Sneak place this block to disable it.");
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        Location location = new Location(world, pos);

        ItemStack heldStack = player.getHeldItem(hand);
        ItemStack walletStack = WalletHelper.getCurrentWalletStack(player);

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
                    message.printMessage(ChatFormatting.GREEN, tePost.getSecurityProfile().getOwnerName() + " is selling " + StringHelper.printCommas(tePost.amountForSale) + " " + tePost.getStackForSale().getDisplayName() + " for " + (tePost.salePrice > 0 ? (StringHelper.printCurrency(tePost.salePrice)) : "free"));
                    message.printMessage(ChatFormatting.GREEN, "Hold a wallet in your inventory to make a purchase.");
                }

                else {
                    message.printMessage(ChatFormatting.RED, "There is nothing to trade!");
                }
            }
        }

        return true;
    }

    private void handleTrade(UnitChatMessage message, World world, EntityPlayer player, TileEntityTradingPost tePost) {

        ItemStack walletStack = WalletHelper.getCurrentWalletStack(player);
        ItemWallet wallet = (ItemWallet) walletStack.getItem();

        if (tePost.hasValidTradeOffer) {

            if (tePost.getStock() >= tePost.amountForSale || tePost.adminMode) {

                if (ItemWallet.getBalance(walletStack) >= tePost.salePrice) {

                    NBTTagCompound nbt = ItemHelper.getNBT(walletStack);

                    nbt.setInteger("balance", nbt.getInteger("balance") - tePost.salePrice);

                    if (tePost.storedCurrency + tePost.salePrice < CUConfig.misc.postCurrencyCapacity) {

                        ItemStack is = new ItemStack(tePost.getStackForSale().getItem(), tePost.amountForSale, tePost.getStackForSale().getItemDamage());

                        if (tePost.adminMode) {

                            if (!world.isRemote) {

                                EntityItem dropItem;
                                dropItem = ItemHelper.spawnItem(world, player, is);

                                if (is.hasTagCompound()) {
                                    dropItem.getItem().setTagCompound(is.getTagCompound());
                                }

                                tePost.storedCurrency += tePost.salePrice;
                                tePost.markForUpdate();
                            }
                        }

                        else {

                            for (ItemStack stack : tePost.slots) {

                                if (ItemStack.areItemsEqual(stack, is)) {

                                    if (!world.isRemote) {

                                        EntityItem dropItem;

                                        dropItem = ItemHelper.spawnItem(world, player, is);

                                        if (stack.hasTagCompound()) {
                                            dropItem.getItem().setTagCompound(stack.getTagCompound());
                                        }
                                    }

                                    InventoryHelper.consumeItem(tePost, tePost.amountForSale, true, tePost.getStackForSale());

                                    tePost.storedCurrency += tePost.salePrice;
                                    tePost.markForUpdate();

                                    tePost.writeToNBT(tePost.getTileData());
                                }
                            }
                        }
                    }

                    else if (!world.isRemote) message.printMessage(ChatFormatting.RED, "Full of money!");
                }

                else if (!world.isRemote) message.printMessage(ChatFormatting.RED, "You don't have enough money!");
            }

            else if (!world.isRemote) message.printMessage(ChatFormatting.RED, "There is no more items in stock!");
        }

        else if (!world.isRemote) message.printMessage(ChatFormatting.RED, "The trade is not set up properly!");
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
