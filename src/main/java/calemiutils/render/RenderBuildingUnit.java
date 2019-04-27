package calemiutils.render;

import calemiutils.blueprint.BlueprintPos;
import calemiutils.init.InitBlocks;
import calemiutils.tileentity.TileEntityBuildingUnit;
import calemiutils.util.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class RenderBuildingUnit extends TileEntitySpecialRenderer<TileEntityBuildingUnit> {

    private static final World world = Minecraft.getMinecraft().world;
    private static final EntityItem ITEM = new EntityItem(world, 0, 0, 0);

    @Override
    public void render(TileEntityBuildingUnit te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        ITEM.hoverStart = 0F;

        List<BlueprintPos> positions = te.getCurrentPositions();

        if (positions.size() > 0) {

            for (BlueprintPos position : positions) {

                Location loc = position.toLocation(te);

                ITEM.setItem(new ItemStack(InitBlocks.BLUEPRINT, 1, position.meta));

                if (loc.isBlockValidForPlacing(InitBlocks.BLUEPRINT)) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableLighting();
                    GlStateManager.enableBlend();

                    GlStateManager.translate(x + position.x + 0.5F, y + position.y - 0.45F, z + position.z + 0.5F);
                    GlStateManager.scale(2.75F, 2.75F, 2.75F);
                    Minecraft.getMinecraft().getRenderManager().renderEntity(ITEM, 0, 0, 0, 0F, 0F, true);

                    GlStateManager.disableBlend();
                    GlStateManager.disableLighting();
                    GlStateManager.popMatrix();
                }
            }
        }
    }
}
