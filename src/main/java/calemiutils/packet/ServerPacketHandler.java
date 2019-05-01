package calemiutils.packet;

import calemiutils.CalemiUtils;
import calemiutils.init.InitItems;
import calemiutils.item.*;
import calemiutils.tileentity.TileEntityBank;
import calemiutils.tileentity.TileEntityBuildingUnit;
import calemiutils.tileentity.TileEntityInteractionInterface;
import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.NBTHelper;
import calemiutils.util.helper.WalletHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerPacketHandler implements IMessage {

    private String text;

    public ServerPacketHandler() {

    }

    public ServerPacketHandler(String text) {

        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, text);
    }

    public static class Handler implements IMessageHandler<ServerPacketHandler, IMessage> {

        @Override
        public IMessage onMessage(ServerPacketHandler message, MessageContext ctx) {

            String[] data = message.text.split("%");

            EntityPlayer player = ctx.getServerHandler().player;

            if (data[0].equalsIgnoreCase("te-enable")) {

                int x = Integer.parseInt(data[1]);
                int y = Integer.parseInt(data[2]);
                int z = Integer.parseInt(data[3]);
                boolean value = Boolean.valueOf(data[4]);

                Location location = new Location(player.world, x, y, z);
                TileEntity te = location.getTileEntity();

                if (te instanceof TileEntityBase) {
                    ((TileEntityBase) te).enable = value;
                    ((TileEntityBase) te).markForUpdate();
                }
            }

            if (data[0].equalsIgnoreCase("gui-open")) {

                int id = Integer.parseInt(data[1]);

                player.openGui(CalemiUtils.instance, id, player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
            }

            if (data[0].equalsIgnoreCase("wallet-withdraw")) {

                int id = Integer.parseInt(data[1]);

                int multiplier = Integer.parseInt(data[2]);

                Item item = InitItems.COIN_PENNY;
                int price = 1;

                if (id == 1) {
                    item = InitItems.COIN_NICKEL;
                    price = 5;
                }
                if (id == 2) {
                    item = InitItems.COIN_QUARTER;
                    price = 25;
                }
                if (id == 3) {
                    item = InitItems.COIN_DOLLAR;
                    price = 100;
                }

                price *= multiplier;

                ItemStack walletStack = WalletHelper.getCurrentWalletStack(player);

                if (!walletStack.isEmpty()) {

                    NBTTagCompound nbt = ItemHelper.getNBT(walletStack);

                    nbt.setInteger("balance", nbt.getInteger("balance") - price);

                    ItemHelper.spawnItem(player.world, player, new ItemStack(item, multiplier));
                }
            }

            if (data[0].equalsIgnoreCase("wallet-togglesuck")) {

                ItemStack heldStack = WalletHelper.getCurrentWalletStack(player);

                if (heldStack.getItem() instanceof ItemWallet) {

                    ItemWallet wallet = (ItemWallet) heldStack.getItem();

                    wallet.toggleSuck(heldStack);
                }
            }

            if (data[0].equalsIgnoreCase("pencil-setcolor")) {

                int color = Integer.parseInt(data[1]);

                if (player.getHeldItemMainhand().getItem() instanceof ItemPencil) {

                    ItemPencil pencil = (ItemPencil) player.getHeldItemMainhand().getItem();
                    pencil.setColorByMeta(player.getHeldItemMainhand(), color);
                }
            }


            if (data[0].equalsIgnoreCase("builderskit-extractblocks")) {

                int multiplier = Integer.parseInt(data[1]);

                ItemStack heldStack = player.getHeldItemMainhand();

                if (heldStack.getItem() instanceof ItemBuildersKit) {

                    ItemBuildersKit buildersKit = (ItemBuildersKit) heldStack.getItem();

                    ItemStack filterStack = buildersKit.getBlockType(heldStack);

                    if (filterStack != null && ItemBuildersKit.getAmountOfBlocks(heldStack) > 0) {

                        int subAmount = multiplier;

                        if (multiplier > ItemBuildersKit.getAmountOfBlocks(heldStack)) {
                            subAmount = ItemBuildersKit.getAmountOfBlocks(heldStack);
                        }

                        ItemBuildersKit.setAmountOfBlocks(heldStack, ItemBuildersKit.getAmountOfBlocks(heldStack) - subAmount);

                        ItemStack is = new ItemStack(filterStack.getItem(), subAmount, filterStack.getItemDamage(), filterStack.getTagCompound());

                        ItemHelper.spawnItem(player.world, player, is);
                    }
                }
            }

            if (data[0].equalsIgnoreCase("builderskit-togglesuck")) {

                ItemStack heldStack = player.getHeldItemMainhand();

                if (heldStack.getItem() instanceof ItemBuildersKit) {

                    ItemBuildersKit buildersKit = (ItemBuildersKit) heldStack.getItem();

                    buildersKit.toggleSuck(heldStack);
                }
            }

            if (data[0].equalsIgnoreCase("builderskit-resetblock")) {

                ItemStack heldStack = player.getHeldItemMainhand();

                if (heldStack.getItem() instanceof ItemBuildersKit) {

                    ItemBuildersKit buildersKit = (ItemBuildersKit) heldStack.getItem();

                    if (buildersKit.getBlockType(heldStack) != null && !buildersKit.getBlockType(heldStack).isEmpty() && ItemBuildersKit.getAmountOfBlocks(heldStack) <= 0) {

                        NBTHelper.saveItem(heldStack.getTagCompound(), ItemStack.EMPTY);
                    }
                }
            }

            if (data[0].equalsIgnoreCase("bank-sync")) {

                BlockPos pos = new BlockPos(Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));

                TileEntityBank tileEntity = (TileEntityBank) player.world.getTileEntity(pos);

                if (tileEntity != null) {
                    NBTTagCompound nbt = ItemHelper.getNBT(tileEntity.getStackInSlot(1));

                    tileEntity.storedCurrency = Integer.parseInt(data[1]);
                    nbt.setInteger("balance", Integer.parseInt(data[2]));
                }
            }

            if (data[0].equalsIgnoreCase("tradingpost-setoptions")) {

                BlockPos pos = new BlockPos(Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));

                TileEntityTradingPost tileEntity = (TileEntityTradingPost) player.world.getTileEntity(pos);

                if (tileEntity != null) {

                    if (data[1].equalsIgnoreCase("amount")) {
                        tileEntity.amountForSale = Integer.parseInt(data[2]);
                    }

                    if (data[1].equalsIgnoreCase("price")) {
                        tileEntity.salePrice = Integer.parseInt(data[2]);
                    }

                    tileEntity.amountForSale = MathHelper.clamp(tileEntity.amountForSale, 1, 1000);
                    tileEntity.salePrice = MathHelper.clamp(tileEntity.salePrice, 0, 10000);
                }
            }

            if (data[0].equalsIgnoreCase("tradingpost-setstackforsale")) {

                BlockPos pos = new BlockPos(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                TileEntityTradingPost tileEntity = (TileEntityTradingPost) player.world.getTileEntity(pos);

                String string = data[4];

                if (tileEntity != null) tileEntity.setStackForSale(ItemHelper.getStackFromString(string));
            }

            if (data[0].equalsIgnoreCase("buildingunit-synccurrentbuildblueprint")) {

                BlockPos pos = new BlockPos(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                TileEntityBuildingUnit tileEntity = (TileEntityBuildingUnit) player.world.getTileEntity(pos);

                if (tileEntity != null) {
                    tileEntity.currentBuildBlueprint = Integer.parseInt(data[4]);
                    tileEntity.markForUpdate();
                }
            }

            if (data[0].equalsIgnoreCase("buildingunit-build")) {

                BlockPos pos = new BlockPos(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                TileEntityBuildingUnit tileEntity = (TileEntityBuildingUnit) player.world.getTileEntity(pos);

                if (tileEntity != null) tileEntity.placeBlueprints();
            }

            if (data[0].equalsIgnoreCase("buildingunit-read")) {

                BlockPos pos = new BlockPos(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                TileEntityBuildingUnit tileEntity = (TileEntityBuildingUnit) player.world.getTileEntity(pos);

                String name = "";
                if (data.length > 4) name = data[4];

                if (tileEntity != null) tileEntity.readBlueprintsInRange(name);
            }

            if (data[0].equalsIgnoreCase("buildingunit-rotate")) {

                BlockPos pos = new BlockPos(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                TileEntityBuildingUnit tileEntity = (TileEntityBuildingUnit) player.world.getTileEntity(pos);

                if (tileEntity != null) {

                    tileEntity.addRotation();
                    tileEntity.markForUpdate();
                }
            }

            if (data[0].equalsIgnoreCase("linkbook")) {

                if (data[1].equalsIgnoreCase("name")) {

                    String name = "";

                    if (data.length > 2) {
                        name = data[2];
                    }

                    ItemLinkBookLocation.bindName(player.getHeldItemMainhand(), name);
                }

                else if (data[1].equalsIgnoreCase("reset")) {
                    ItemLinkBookLocation.resetLocation(player.getHeldItemMainhand(), player);
                }

                else {

                    int x = Integer.parseInt(data[2]);
                    int y = Integer.parseInt(data[3]);
                    int z = Integer.parseInt(data[4]);
                    int dim = Integer.parseInt(data[5]);

                    Location location = new Location(player.world, x, y, z);

                    if (data[1].equalsIgnoreCase("bind")) {
                        ItemLinkBookLocation.bindLocation(player.getHeldItemMainhand(), player, location, true);
                    }

                    if (data[1].equalsIgnoreCase("teleport")) {
                        ItemLinkBookLocation.teleport(player.world, player, location, dim);
                    }
                }
            }

            if (data[0].equalsIgnoreCase("interactioninterface-setfilter")) {

                BlockPos pos = new BlockPos(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                TileEntityInteractionInterface tileEntity = (TileEntityInteractionInterface) player.world.getTileEntity(pos);

                String filterIconString = data[4];
                String filterName = "";

                if (data.length > 5) {
                    filterName = data[5];
                }

                ItemStack filterStack = new ItemStack(InitItems.INTERACTION_INTERFACE_FILTER);

                ItemInteractionInterfaceFilter.setFilterIcon(filterStack, ItemHelper.getStackFromString(filterIconString));
                ItemInteractionInterfaceFilter.setFilterName(filterStack, filterName);

                if (tileEntity != null) tileEntity.filter = filterStack;
            }

            if (data[0].equalsIgnoreCase("interactionterminal-interact")) {

                int x = Integer.parseInt(data[1]);
                int y = Integer.parseInt(data[2]);
                int z = Integer.parseInt(data[3]);

                boolean b = Boolean.parseBoolean(data[4]);

                Location location = new Location(player.world, x, y, z);

                player.setSneaking(b);
                location.getBlock().onBlockActivated(player.world, location.getBlockPos(), location.getBlockState(), player, EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);
            }

            if (data[0].equalsIgnoreCase("iifilter-seticon")) {

                ItemStack stackToSet = ItemHelper.getStackFromString(data[1]);
                ItemStack stack = player.getHeldItemMainhand();
                ItemInteractionInterfaceFilter.setFilterIcon(stack, stackToSet);
            }

            if (data[0].equalsIgnoreCase("iifilter-setname")) {

                String name = "";

                if (data.length > 1) {
                    name = data[1];
                }

                ItemStack stack = player.getHeldItemMainhand();
                ItemInteractionInterfaceFilter.setFilterName(stack, name);
            }

            return null;
        }
    }
}