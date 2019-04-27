package calemiutils.util.helper;

public class MathHelper {

    public static int[] getCountingArray(int offset, int max) {

        int length = max - offset;

        int[] array = new int[length + 1];

        for (int i = 0; i < array.length; i++) {
            array[i] = offset + i;
        }

        return array;
    }

    public static int getAmountToAdd(int value, int amountToAdd, int maxAmount) {

        if (value + amountToAdd > maxAmount) {

            return 0;
        }

        else {
            return amountToAdd;
        }
    }

    public static int getRemainder(int value, int amountToAdd, int maxAmount) {

        if (value + amountToAdd > maxAmount) {
            return maxAmount - value;
        }

        else {
            return 0;
        }
    }

    public static int scaleInt(int value, int maxValue, int maxScale) {

        float f = value * maxScale / maxValue;

        return (int) f;
    }
}
