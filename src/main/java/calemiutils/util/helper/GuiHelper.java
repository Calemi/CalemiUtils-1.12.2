package calemiutils.util.helper;

import calemiutils.CUReference;
import calemiutils.gui.base.GuiRect;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiHelper {

    private static final int TEXTURE_SIZE = 256;
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void bindTexture(String name) {

        mc.getTextureManager().bindTexture(new ResourceLocation(CUReference.MOD_ID + ":textures/gui/" + name + ".png"));
    }

    public static void bindGuiTextures() {

        mc.getTextureManager().bindTexture(CUReference.GUI_TEXTURES);
    }

    /*public static void drawOneLineHoveringTextBox(String text, int mouseX, int mouseY, GuiRect rect) {

        if (rect.contains(mouseX, mouseY)) {

            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 325);

            bindGuiTextures();
            drawCappedRect(mouseX + 2, mouseY - 13, 0, 168, 0, mc.fontRenderer.getStringWidth(text) + 5, 13, 256, 72);
            mc.fontRenderer.drawString(text, mouseX + 5, mouseY - 10, 0xFFFFFF);

            GL11.glPopMatrix();

            GL11.glColor4f(1, 1, 1, 1);
        }
    }*/

    public static void drawHoveringTextBox(int mouseX, int mouseY, int zLevel, GuiRect rect, String... text) {

        if (rect.contains(mouseX, mouseY)) {

            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, zLevel);

            int maxLength = mc.fontRenderer.getStringWidth(text[0]);

            for (String str : text) {

                if (mc.fontRenderer.getStringWidth(str) > maxLength) {
                    maxLength = mc.fontRenderer.getStringWidth(str);
                }
            }

            bindGuiTextures();
            drawCappedRect(mouseX + 7, mouseY - 13, 0, 138, 0, maxLength + 5, 13 + ((text.length - 1) * 9), 256, 102);

            for (int i = 0; i < text.length; i++) {

                String str = text[i];

                mc.fontRenderer.drawString(ChatFormatting.WHITE + str, (mouseX + 10), (mouseY - 10) + (i * 9), 0xFFFFFF);
            }

            GL11.glTranslatef(0, 0, 0);

            GL11.glPopMatrix();

            GL11.glColor4f(1, 1, 1, 1);
        }
    }

    public static void drawLimitedString(String text, int x, int y, int textLimit, int color) {

        String temp = text;

        if (temp.length() > textLimit) {

            temp = temp.substring(0, textLimit - 1);
            temp += "...";
        }

        mc.fontRenderer.drawString(temp, x, y, color);
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void drawCenteredString(String text, int x, int y, int color) {

        mc.fontRenderer.drawString(text, x - (mc.fontRenderer.getStringWidth(text) / 2), y, color);
    }

    /*
    public static void drawLimitedCenteredString(String text, int x, int y, int maxWidth, int color) {

        int xPos = (x - 3);
        int yPos = y;

        int stringWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);

        if (stringWidth > maxWidth) {

            ArrayList arrayList = new ArrayList();
            Iterator iterator = mc.fontRenderer.listFormattedStringToWidth(text, maxWidth).iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();
                arrayList.add(s1);
            }

            String[] string = (String[]) arrayList.toArray(new String[0]);
            int yOffset = yPos + 5;

            for (int i = 0; i < string.length; ++i) {

                String s = string[i];
                drawCenteredString(s, xPos + 3, yOffset, color);
                yOffset += mc.fontRenderer.FONT_HEIGHT + 1;
            }
        }

        else {
            mc.fontRenderer.drawString(text, (xPos - (stringWidth / 2)) + 3, yPos + 5, color);
        }
    }*/

    public static void drawCappedRect(int x, int y, int u, int v, int zLevel, int width, int height, int maxWidth, int maxHeight) {

        drawRect(x, y, u, v, zLevel, width - 2, height - 2);
        drawRect(x + width - 2, y, u + maxWidth - 2, v, zLevel, 2, height - 2);
        drawRect(x, y + height - 2, u, v + maxHeight - 2, zLevel, width - 2, 2);
        drawRect(x + width - 2, y + height - 2, u + maxWidth - 2, v + maxHeight - 2, zLevel, 2, 2);
    }

    public static void drawRect(int x, int y, int u, int v, int zLevel, int width, int height) {

        int maxX = x + width;
        int maxY = y + height;

        int maxU = u + width;
        int maxV = v + height;

        float pixel = 1F / TEXTURE_SIZE;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        bufferbuilder.pos(x, maxY, zLevel);
        bufferbuilder.tex(u * pixel, maxV * pixel).endVertex();

        bufferbuilder.pos(maxX, maxY, zLevel);
        bufferbuilder.tex(maxU * pixel, maxV * pixel).endVertex();

        bufferbuilder.pos(maxX, y, zLevel);
        bufferbuilder.tex(maxU * pixel, v * pixel).endVertex();

        bufferbuilder.pos(x, y, zLevel);
        bufferbuilder.tex(u * pixel, v * pixel).endVertex();

        tessellator.draw();
    }

    public static void drawColoredRect(int x, int y, int zLevel, int width, int height, int hex, float alpha) {

        float r = (hex >> 16) & 0xFF;
        float g = (hex >> 8) & 0xFF;
        float b = (hex) & 0xFF;

        float red = ((((r * 100) / 255) / 100));
        float green = ((((g * 100) / 255) / 100));
        float blue = ((((b * 100) / 255) / 100));

        int maxX = x + width;
        int maxY = y + height;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        bufferbuilder.pos(x, maxY, zLevel).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(maxX, maxY, zLevel).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(maxX, y, zLevel).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x, y, zLevel).color(red, green, blue, alpha).endVertex();

        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawItemStack(RenderItem itemRender, ItemStack stack, int x, int y) {

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        itemRender.zLevel = -94;
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        itemRender.zLevel = 0F;
    }
}
