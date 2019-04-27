package calemiutils.util.helper;

import org.lwjgl.input.Keyboard;

public class ShiftHelper {

    public static int getShiftCtrlInt(int defaultInt, int shiftInt, int ctrlInt, int bothInt) {

        int i = defaultInt;

        boolean s = isShift();
        boolean c = isCtrl();

        if (s) i = shiftInt;
        if (c) i = ctrlInt;
        if (s && c) i = bothInt;

        return i;
    }

    public static boolean isShift() {

        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    private static boolean isCtrl() {

        return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
    }
}
