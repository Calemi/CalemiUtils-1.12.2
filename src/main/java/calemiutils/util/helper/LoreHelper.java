package calemiutils.util.helper;

import calemiutils.config.CUConfig;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class LoreHelper {

    public static void addDisabledLore(List<String> tooltip, int value) {

        if (value <= 0) {
            tooltip.add(ChatFormatting.RED + "" + ChatFormatting.ITALIC + "Disabled by config!");
        }
    }

    public static void addInformationLore(List<String> tooltip, String lore) {

        if (CUConfig.tooltips.showInfoOnTooltips) {

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                tooltip.add(ChatFormatting.ITALIC + lore);
            }

            else tooltip.add(getPlateText("Shift", ChatFormatting.AQUA) + " Info");
        }
    }

    public static void addControlsLore(List<String> tooltip, String lore, LoreHelper.Type type) {

        addControlsLore(tooltip, lore, type, false);
    }

    public static void addControlsLore(List<String> tooltip, String lore, LoreHelper.Type type, boolean isFirst) {

        if (CUConfig.tooltips.showControlsOnTooltips) {

            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {

                addActionLore(tooltip, lore, type);
            }

            else if (isFirst) tooltip.add(getPlateText("Ctrl", ChatFormatting.AQUA) + " Controls");
        }
    }

    public static void addCurrencyLore(List<String> tooltip, int currentCurrency) {

        addCurrencyLore(tooltip, currentCurrency, 0);
    }

    public static void addCurrencyLore(List<String> tooltip, int currentCurrency, int maxCurrency) {

        tooltip.add("Currency: " + ChatFormatting.GOLD + StringHelper.printCurrency(currentCurrency) + (maxCurrency != 0 ? (" / " + StringHelper.printCurrency(maxCurrency)) : ""));
    }

    private static void addActionLore(List<String> tooltip, String lore, LoreHelper.Type type) {

        tooltip.add(getPlateText(type.getName(), ChatFormatting.YELLOW) + " " + lore);
    }

    private static String getPlateText(String text, ChatFormatting format) {

        return "[" + format + text + ChatFormatting.GRAY + "]";
    }

    public enum Type {
        USE("Use"),
        USE_OPEN_HAND("Use-Open-Hand"),
        USE_WRENCH("Use-Wrench"),
        USE_WALLET("Use-Wallet"),
        USE_BOOK("Use-Book"),
        SNEAK_USE("Sneak-Use"),
        SNEAK_USE_BLUEPRINT("Sneak-Use-Blueprint"),
        SNEAK_BREAK_BLOCK("Sneak-Break-Block"),
        LEFT_CLICK_BLOCK("Left-Click-Block"),
        SNEAK_LEFT_CLICK_BLOCK("Sneak-Left-Click-Block"),
        LEFT_CLICK_BLUEPRINT("Left-Click-Blueprint");

        private final String name;

        Type(String name) {

            this.name = name;
        }

        String getName() {

            return name;
        }
    }
}

