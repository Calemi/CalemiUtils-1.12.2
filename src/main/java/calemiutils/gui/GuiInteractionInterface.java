package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.GuiContainerBase;
import calemiutils.gui.base.GuiFakeSlot;
import calemiutils.item.ItemInteractionInterfaceFilter;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.tileentity.TileEntityInteractionInterface;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.PacketHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiInteractionInterface extends GuiContainerBase {

    private final TileEntityInteractionInterface teInterface;
    private GuiFakeSlot fakeSlot;

    public GuiInteractionInterface(EntityPlayer player, TileEntityInteractionInterface te) {

        super(te.getTileContainer(player), player, te);
        this.teInterface = te;
    }

    @Override
    public String getGuiTextureName() {

        return "one_slot";
    }

    @Override
    public String getGuiTitle() {

        return "Interaction Interface";
    }

    @Override
    public void initGui() {

        super.initGui();

        fakeSlot = new GuiFakeSlot(0, getScreenX() + 80, getScreenY() + 18, itemRender, buttonList);
        if (!teInterface.filter.isEmpty()) fakeSlot.setItemStack(teInterface.filter);
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        InventoryPlayer inv = mc.player.inventory;

        if (button.id == fakeSlot.id) {

            ItemStack stack = inv.getItemStack();

            if (teInterface.canSetFilter(stack)) {

                ItemStack filterIcon = ItemInteractionInterfaceFilter.getFilterIcon(stack);
                String filterName = ItemInteractionInterfaceFilter.getFilterName(stack);

                CalemiUtils.network.sendToServer(new ServerPacketHandler("interactioninterface-setfilter%" + PacketHelper.sendLocation(teInterface.getLocation()) + ItemHelper.getStringFromStack(filterIcon) + "%" + filterName));
                teInterface.filter = stack;
                fakeSlot.setItemStack(stack);
            }
        }
    }

    @Override
    public int getGuiSizeY() {

        return 123;
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {
        fakeSlot.renderButton(mouseX, mouseY, 150);
    }
}
