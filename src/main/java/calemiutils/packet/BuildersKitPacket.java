package calemiutils.packet;

import calemiutils.item.ItemBuildersKit;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BuildersKitPacket extends ServerPacketHandler {

    public BuildersKitPacket() {

    }

    public BuildersKitPacket(String text) {

        this.text = text;
    }

    public static class Handler implements IMessageHandler<BuildersKitPacket, IMessage> {

        @Override
        public IMessage onMessage(BuildersKitPacket message, MessageContext ctx) {

            String[] data = message.text.split("%");
            EntityPlayer player = ctx.getServerHandler().player;
            World world = player.world;

            if (data[0].equalsIgnoreCase("extractblocks")) {

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

            else if (data[0].equalsIgnoreCase("togglesuck")) {

                ItemStack heldStack = player.getHeldItemMainhand();

                if (heldStack.getItem() instanceof ItemBuildersKit) {

                    ItemBuildersKit buildersKit = (ItemBuildersKit) heldStack.getItem();

                    buildersKit.toggleSuck(heldStack);
                }
            }

            else if (data[0].equalsIgnoreCase("resetblock")) {

                ItemStack heldStack = player.getHeldItemMainhand();

                if (heldStack.getItem() instanceof ItemBuildersKit) {

                    ItemBuildersKit buildersKit = (ItemBuildersKit) heldStack.getItem();

                    if (buildersKit.getBlockType(heldStack) != null && !buildersKit.getBlockType(heldStack).isEmpty() && ItemBuildersKit.getAmountOfBlocks(heldStack) <= 0) {

                        NBTHelper.saveItem(heldStack.getTagCompound(), ItemStack.EMPTY, 0);
                    }
                }
            }

            return null;
        }
    }
}
