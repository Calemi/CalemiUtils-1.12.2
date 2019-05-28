package calemiutils.util;

import net.minecraft.util.text.TextFormatting;

public enum CoinColor {

    BRONZE(TextFormatting.GOLD, 0xECA747),
    GRAY(TextFormatting.DARK_GRAY, 0xA7A7A7),
    SILVER(TextFormatting.GRAY, 0xFFFFFF),
    GOLD(TextFormatting.YELLOW, 0x11FFD800),

    ORANGE(TextFormatting.GOLD, 16351261),
    MAGENTA(TextFormatting.AQUA, 13061821),
    LIGHT_BLUE(TextFormatting.BLUE, 3847130),
    YELLOW(TextFormatting.YELLOW, 16701501),
    LIME(TextFormatting.GREEN, 8439583),
    PINK(TextFormatting.LIGHT_PURPLE, 15961002),
    CYAN(TextFormatting.DARK_AQUA, 1481884),
    PURPLE(TextFormatting.DARK_PURPLE, 8991416),
    BLUE(TextFormatting.BLUE, 3949738),
    BROWN(TextFormatting.GOLD, 8606770),
    GREEN(TextFormatting.DARK_GREEN, 6192150),
    RED(TextFormatting.DARK_RED, 11546150),
    BLACK(TextFormatting.BLACK, 1908001);


    public final TextFormatting format;
    public final int hexCode;

    CoinColor(TextFormatting format, int hexCode) {

        this.format = format;
        this.hexCode = hexCode;
    }
}
