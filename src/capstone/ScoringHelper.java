package capstone;

/**
 * The scoring helper provides only simple functionality to determine the correct scoring methods.
 */
public final class ScoringHelper {
    private static int width;
    private static int height;
    private static int baseValue;

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        ScoringHelper.width = width;
        updateBaseValue();
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        ScoringHelper.height = height;
        updateBaseValue();
    }

    /**
     * Updates the current value of the base value to the square root of width times height multiplied by 2000.
     */
    private static void updateBaseValue() {
        baseValue = (int) (2000 * Math.sqrt(width * height));
    }

    /**
     * Gets the current Base Value
     *
     * @return base value of the score
     */
    public static int getBaseValue() {
        return baseValue;
    }
}
