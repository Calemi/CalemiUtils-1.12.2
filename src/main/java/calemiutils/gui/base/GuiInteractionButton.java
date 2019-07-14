package calemiutils.gui.base;

import calemiutils.tileentity.TileEntityInteractionInterface;
import calemiutils.util.IExtraInformation;
import calemiutils.util.Location;
import calemiutils.util.helper.GuiHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiInteractionButton extends GuiButton {

    GuiRect rect;
    private final TileEntityInteractionInterface teII;
    public final Location location;
    private final ItemStack stack;
    private final RenderItem itemRender;

    GuiInteractionButton(int id, int x, int y, RenderItem itemRender, TileEntityInteractionInterface teII, Location location, ItemStack stack, List<GuiButton> buttonList) {

        super(id, x, y, 16, 16, "");
        rect = new GuiRect(this.x, this.y, width, height);
        this.teII = teII;
        this.location = location;
        this.stack = stack;
        this.itemRender = itemRender;
        buttonList.add(this);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

        if (this.visible && enabled) {

            hovered = rect.contains(mouseX, mouseY);

            ItemStack icon = stack;
            List<String> list = new ArrayList<>();

            if (teII != null) {

                //Add extra strings to the list if the block has more information to share

                if (Block.getBlockFromItem(stack.getItem()) instanceof IExtraInformation) {

                    IExtraInformation info = ((IExtraInformation) Block.getBlockFromItem(stack.getItem()));

                    icon = info.getButtonIcon(mc.world, location, stack);
                    info.getButtonInformation(list, mc.world, location, stack);

                    if (list.size() > 0) list.add("");
                }

                //Render a custom icon instead of the block above if there is one set

                ItemStack blockStack = teII.blockIconStack;

                if (!blockStack.isEmpty()) {
                    icon = blockStack;
                }

                //Add a custom name if there is one set

                if (!teII.blockName.isEmpty()) {

                    list.add(0, "");
                    list.add(0, teII.blockName);
                }
            }

            //Convert List to Array

            String[] strings = new String[list.size() + 1];

            for (int i = 0; i < list.size(); i++) {
                strings[i] = list.get(i);
            }

            strings[strings.length - 1] = ChatFormatting.ITALIC + stack.getDisplayName() + ": " + location.toString();

            GuiHelper.drawItemStack(itemRender, icon, rect.x, rect.y);

            GuiHelper.drawHoveringTextBox(mouseX, mouseY, 150, rect, strings);
            this.displayString = strings[0];

            GL11.glColor4f(1, 1, 1, 1);
        }
    }
}
