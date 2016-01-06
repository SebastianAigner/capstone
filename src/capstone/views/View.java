package capstone.views;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;

public abstract class View {
    protected Screen screen;
    protected int width;
    protected int height;

    /**
     * Generates a new view with the given parameters.
     *
     * @param s      Lanterna Screen that is supposed to be used.
     * @param width  Width of the View in Characters
     * @param height Height of the View in Characters
     */
    public View(Screen s, int width, int height) {
        this.screen = s;
        this.width = width;
        this.height = height;
        System.out.println("Created new view " + width + "x" + height);
    }

    public abstract void processKeystroke(Key keystroke);

    public abstract boolean update(int deltatime);

    public abstract void processResize();

    public abstract View requestsViewStackAddition();

    public abstract boolean requestsViewStackRemoval();

    public abstract void managementUpdate(); //used for management. duh.

    public abstract void resetViewStackAddition();

}
