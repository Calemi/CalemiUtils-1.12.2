package calemiutils.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class UnitChatMessage {

    private final String unitName;
    private final EntityPlayer[] players;

    public UnitChatMessage(String unitName, EntityPlayer... players) {

        this.unitName = unitName;
        this.players = players;
    }

    public void printMessage(TextFormatting format, String message) {

        for (EntityPlayer player : players) {

            TextComponentString componentString = new TextComponentString(getUnitName() + (format + message));
            player.sendMessage(componentString);
        }
    }

    private String getUnitName() {

        return "[" + unitName + "] ";
    }
}
