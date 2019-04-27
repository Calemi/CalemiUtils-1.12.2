package calemiutils.render;

import calemiutils.tileentity.base.TileEntityDiggingUnitBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RenderDiggingUnit extends TileEntitySpecialRenderer<TileEntityDiggingUnitBase> {

    private static final long TARGET_TIME = 10;
    private static final World world = Minecraft.getMinecraft().world;
    private static final EntityItem ITEM = new EntityItem(world, 0, 0, 0);
    private long lastTime;
    private float rot;

    @Override
    public void render(TileEntityDiggingUnitBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        ITEM.hoverStart = 0F;

        ItemStack stack = te.getCurrentLocationStack();

        if (stack != null) {

            ITEM.setItem(stack);

            if (System.currentTimeMillis() - lastTime >= TARGET_TIME) {
                lastTime = System.currentTimeMillis();
                rot += 1F;
                rot %= 360;
            }

            GlStateManager.pushMatrix();

            GlStateManager.translate(x + 0.5F, y + 1F, z + 0.5F);
            GlStateManager.rotate(rot, 0, 1, 0);

            Minecraft.getMinecraft().getRenderManager().renderEntity(ITEM, 0, 0, 0, 0F, 0F, false);

            GlStateManager.popMatrix();
        }

    }
}
