package calemiutils.gui.base;

public class GuiRect {

    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public GuiRect(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(int px, int py) {

        return px > x - 1 && px < (x + width) + 1 && py > y - 1 && py < (y + height) + 1;
    }
}
