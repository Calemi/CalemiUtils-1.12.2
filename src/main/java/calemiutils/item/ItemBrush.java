package calemiutils.item;

import calemiutils.config.CUConfig;
import calemiutils.item.base.ItemBase;
import calemiutils.util.Location;
import calemiutils.util.UnitChatMessage;
import calemiutils.util.helper.LoreHelper;
import calemiutils.util.helper.SoundHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemBrush extends ItemBase {

    public Location location1, location2;

    public ItemBrush() {

        super("brush", 1);
        if (CUConfig.itemUtils.brush && CUConfig.blockUtils.blueprint) addItem();
    }

    public static UnitChatMessage getMessage(EntityPlayer player) {

        return new UnitChatMessage("Brush", player);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addDisabledLore(tooltip, CUConfig.blockScans.worldEditMaxSize);
        LoreHelper.addInformationLore(tooltip, "Creates shapes of blueprint for all your building needs! Use /cu for commands.");
        LoreHelper.addControlsLore(tooltip, "Marks the first point", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltip, "Marks the second point", LoreHelper.Type.SNEAK_USE);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (!player.isSneaking()) {

            location1 = new Location(worldIn, pos);

            if (!worldIn.isRemote) getMessage(player).printMessage(TextFormatting.GREEN, "First position set to coords: " + location1.x + ", " + location1.y + ", " + location1.z);
            SoundHelper.playClick(worldIn, player);
        }

        else {

            location2 = new Location(worldIn, pos);

            if (!worldIn.isRemote) getMessage(player).printMessage(TextFormatting.GREEN, "Second position set to coords: " + location2.x + ", " + location2.y + ", " + location2.z);
            SoundHelper.playClick(worldIn, player);
        }

        return EnumActionResult.SUCCESS;
    }
}
