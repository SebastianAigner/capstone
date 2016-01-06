package capstone;

/**
 * The scoring helper provides only simple functionality to determine the correct scoring methods.
 */
public final class ScoringHelper {
    private static int width;
    private static int height;
    private static int formula;

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        ScoringHelper.width = width;
        updateFormula();
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        ScoringHelper.height = height;
        updateFormula();
    }

    /**
     * Updates the current value of the base value / formula to the square root of width times height multiplied by 2000.
     */
    private static void updateFormula() {
        formula = (int) (2000 * Math.sqrt(width * height));
    }

    public static int getFormula() {
        return formula;
    }
}
