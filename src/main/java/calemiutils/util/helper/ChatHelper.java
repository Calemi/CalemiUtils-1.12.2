package calemiutils.util.helper;

import calemiutils.CUReference;
import calemiutils.util.UnitChatMessage;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;

public class ChatHelper {

    public static void printModMessage(ChatFormatting format, String message, EntityPlayer... players) {

        UnitChatMessage unitMessage = new UnitChatMessage(CUReference.MOD_NAME, players);
        unitMessage.printMessage(format, message);
    }
}
