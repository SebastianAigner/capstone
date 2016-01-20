package capstone.views;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;

public abstract class View {
    protected final Screen screen;
    protected int width;
    protected int height;
    protected View viewStackAddition;
    protected boolean viewStackRemoval;

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

    /**
     * The view processes a user input keystroke
     *
     * @param keystroke last user keystroke
     */
    public abstract void processKeystroke(Key keystroke);

    /**
     * The view will update in relation to the given time passed since the last call
     *
     * @param deltatime time passed since last call
     * @return successful update
     */
    public abstract boolean update(int deltatime);

    /**
     * The view will handle a resize of the window in width and/or height
     */
    public abstract void processResize();

    /**
     * The view exposes views that should be added by the current view manager
     *
     * @return new view to be shown
     */
    public View requestsViewStackAddition() {
        return this.viewStackAddition;
    }

    /**
     * The view exposes the intent to be removed by the current view manager
     *
     * @return this view should be removed
     */
    public boolean requestsViewStackRemoval() {
        return this.viewStackRemoval;
    }

    /**
     * The view updates itself regardless of its position in the view manager, even in background.
     */
    public abstract void managementUpdate();

    /**
     * Resets the intention to add something to the view manager in order to prevent multiple additions of the same
     * object
     */
    public void resetViewStackAddition() {
        this.viewStackAddition = null;
    }

}
