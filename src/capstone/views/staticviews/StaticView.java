package capstone.views.staticviews;

import capstone.views.View;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;

/**
 * A static view is a "stupid" view. It can only display text, but is not intended to have a lot of dynamic and moving
 * elements (such as menus). The user can opt to leave the static view by pressing the Escape key.
 */
public abstract class StaticView extends View {
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
     * @param keystroke key pressed event by the user
     */
    @Override
    public void processKeystroke(Key keystroke) {
        if (keystroke.getKind() == Key.Kind.Escape) {
            viewStackRemoval = true;
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
     * Static views do not require management updates.
     */
    @Override
    public void managementUpdate() {
    }
}
