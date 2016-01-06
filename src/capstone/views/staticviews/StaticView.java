package capstone.views.staticviews;

import capstone.views.View;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;

/**
 * A static view is a "stupid" view. It can only display text, but is not intended to have a lot of dynamic and moving
 * elements (such as menus). The user can opt to leave the static view by pressing the Escape key.
 */
public abstract class StaticView extends View {
    protected boolean leaveView = false;
    protected boolean hasDrawnStatics;

    /**
     * Creates a new static view
     *
     * @param s      Lanterna screen to be drawn on
     * @param width  width of the view
     * @param height height of the view
     */
    public StaticView(Screen s, int width, int height) {
        super(s, width, height);
    }

    /**
     * Processes the incoming keystrokes. If an Escape-Press is detected, an intent to leave the view will be issued.
     * All other keys will be disregarded.
     *
     * @param keystroke
     */
    @Override
    public void processKeystroke(Key keystroke) {
        if (keystroke.getKind() == Key.Kind.Escape) {
            leaveView = true;
        }
    }

    /**
     * This method ensures that all the static elements of the view will be redrawn upon resize.
     * Because elements are intended not to move, they do not require to be redrawn at any point, except for when
     * the screen is being resized.
     */
    @Override
    public void processResize() {
        width = screen.getTerminalSize().getColumns();
        height = screen.getTerminalSize().getRows();
        hasDrawnStatics = false;
    }

    /**
     * A static view will not request a new View to be pushed on the view stack.
     *
     * @return null
     */
    @Override
    public View requestsViewStackAddition() {
        return null;
    }

    /**
     * Whether the view wants to be removed from the view stack
     *
     * @return intent to leave the view stack
     */
    @Override
    public boolean requestsViewStackRemoval() {
        return leaveView;
    }

    /**
     * Since there is no view that is requested, the logic for resetting the addition will stay blank.
     */
    @Override
    public void resetViewStackAddition() {

    }

    /**
     * Static views do not require management updates.
     */
    @Override
    public void managementUpdate() {
        //TODO IS MGMT even necessary at this point anymore?
    }
}
