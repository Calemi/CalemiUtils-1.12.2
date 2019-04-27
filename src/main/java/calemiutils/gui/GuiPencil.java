package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.block.BlockBlueprint;
import calemiutils.gui.base.GuiBlueprintButton;
import calemiutils.gui.base.GuiScreenBase;
import calemiutils.init.InitBlocks;
import calemiutils.item.ItemPencil;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.util.helper.GuiHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiPencil extends GuiScreenBase {

    private final GuiBlueprintButton[] buttons = new GuiBlueprintButton[16];

    private final ItemStack stack;

    public GuiPencil(EntityPlayer player, ItemStack stack) {

        super(player);
        this.stack = stack;
    }

    @Override
    public String getGuiTextureName() {

        return null;
    }

    @Override
    public int getGuiSizeX() {

        return 0;
    }

    @Override
    public int getGuiSizeY() {

        return 0;
    }

    @Override
    public void initGui() {

        super.initGui();

        BlockBlueprint blueprint = (BlockBlueprint) InitBlocks.BLUEPRINT;

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new GuiBlueprintButton(i, getScreenX() + (i * 20) - 158, getScreenY() - 8, itemRender, new ItemStack(InitBlocks.BLUEPRINT, 1, i), buttonList);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);

        for (int i = 0; i < buttons.length; i++) {

            if (button.id == buttons[i].id) {

                if (stack != null) {

                    ItemPencil pencil = (ItemPencil) stack.getItem();

                    CalemiUtils.network.sendToServer(new ServerPacketHandler("pencil-setcolor%" + i));
                    pencil.setColorByMeta(stack, i);

                    mc.player.closeScreen();
                }
            }
        }
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        for (int i = 0; i < EnumDyeColor.values().length; i++) {

            int color = EnumDyeColor.byMetadata(i).getColorValue();
            GuiHelper.drawColoredRect(getScreenX() + (i * 20) - 160, 0, 0, 20, this.height, color, 0.4F);
        }

        GuiHelper.drawCenteredString("Choose a Color", getScreenX(), getScreenY() - 25, 0xFFFFFF);
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    @Override
    public boolean canCloseWithInvKey() {

        return true;
    }
}
