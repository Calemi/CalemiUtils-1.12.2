package calemiutils.render;

import calemiutils.tileentity.TileEntityBookStand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;

public class RenderBookStand extends TileEntitySpecialRenderer<TileEntityBookStand> {

    private static final World world = Minecraft.getMinecraft().world;
    private static final EntityItem ITEM = new EntityItem(world, 0, 0, 0);

    @Override
    public void render(TileEntityBookStand te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        ITEM.hoverStart = 0F;

        if (!te.getStackInSlot(0).isEmpty()) {

            ITEM.setItem(te.getStackInSlot(0));

            GlStateManager.pushMatrix();


            GlStateManager.translate(x + 0.5F, y + 0.05F, z + 0.5F);
            GlStateManager.rotate(180, 0, 1, 0);
            GlStateManager.scale(2, 2, 2);

            Minecraft.getMinecraft().getRenderManager().renderEntity(ITEM, 0, 0, 0, 0F, 0F, false);

            GlStateManager.popMatrix();
        }
    }
}
