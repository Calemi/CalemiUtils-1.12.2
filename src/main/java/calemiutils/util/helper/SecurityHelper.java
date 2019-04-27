package calemiutils.util.helper;

import calemiutils.config.CUConfig;
import calemiutils.security.ISecurity;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.UnitChatMessage;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class SecurityHelper {

    public static boolean openSecuredBlock(Location location, EntityPlayer player, boolean printError) {

        TileEntity tileEntity = location.getTileEntity();

        if (tileEntity instanceof ISecurity) {

            ISecurity security = (ISecurity) tileEntity;

            if (security.getSecurityProfile().isOwner(player.getName()) || player.capabilities.isCreativeMode || !CUConfig.misc.useSecurity) {

                return true;
            }

            else if (printError) printErrorMessage(location, player);
        }

        return false;
    }

    public static void printErrorMessage(Location location, EntityPlayer player) {

        UnitChatMessage message = new UnitChatMessage(location.getBlock().getLocalizedName(), player);
        if (!player.world.isRemote) message.printMessage(ChatFormatting.RED, "This unit doesn't belong to you!");
    }

    public static String getSecuredGuiName(TileEntityBase te) {

        if (te instanceof ISecurity) {
            return " (" + ((ISecurity) te).getSecurityProfile().getOwnerName() + ")";
        }

        return "";
    }
}
