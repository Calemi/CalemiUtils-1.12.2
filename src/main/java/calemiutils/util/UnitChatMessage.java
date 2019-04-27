package calemiutils.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class UnitChatMessage {

    private final String unitName;
    private final EntityPlayer[] players;

    public UnitChatMessage(String unitName, EntityPlayer... players) {

        this.unitName = unitName;
        this.players = players;
    }

    public void printMessage(ChatFormatting format, String message) {

        StringBuilder newMessage = new StringBuilder();
        String[] m = message.split(" ");

        for (String t : m) {

            newMessage.append(format);
            newMessage.append(t);
            newMessage.append(" ");
        }

        for (EntityPlayer player : players) {
            player.sendMessage(new TextComponentString(getUnitName() + (format + newMessage.toString())));
        }
    }

    /*public void printSpace() {

        for (EntityPlayer player : players) {

            player.sendMessage(new TextComponentString(""));
        }
    }*/

    /*private int getMessageLength(String message) {

        return getUnitName().length() + message.length();
    }*/

    private String getUnitName() {

        return "[" + unitName + "] ";
    }
}
