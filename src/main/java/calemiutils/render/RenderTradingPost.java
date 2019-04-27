package calemiutils.render;

import calemiutils.tileentity.TileEntityTradingPost;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;

public class RenderTradingPost extends TileEntitySpecialRenderer<TileEntityTradingPost> {

    private static final World world = Minecraft.getMinecraft().world;
    private static final EntityItem ITEM = new EntityItem(world, 0, 0, 0);

    private long lastTime;
    private float rot;

    @Override
    public void render(TileEntityTradingPost te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        ITEM.hoverStart = 0F;

        if (te.getStackForSale() != null) {

            ITEM.setItem(te.getStackForSale());

            long targetTime = 10;
            if (System.currentTimeMillis() - lastTime >= targetTime) {
                lastTime = System.currentTimeMillis();
                rot += 1F;
                rot %= 360;
            }

            float offset = 0;

            if (te.getStackForSale().getItem() instanceof ItemBlock) {
                offset = 0.1F;
            }

            GlStateManager.pushMatrix();

            GlStateManager.translate(x + 0.5F, y + 0.05F + offset, z + 0.5F);
            GlStateManager.rotate(rot, 0, 1, 0);

            Minecraft.getMinecraft().getRenderManager().renderEntity(ITEM, 0, 0, 0, 0F, 0F, false);

            GlStateManager.popMatrix();
        }
    }
}
