package capstone.views.staticviews;

import capstone.gameobject.dynamicObjects.PlayerGameObject;
import capstone.notificationcenter.NotificationCenter;
import capstone.notificationcenter.NotificationMessage;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * The Win View is shown to the player when he has successfully completed the game / a level. It shows interesting
 * information like score and remaining lives.
 */
public class WinView extends StaticView {
    private PlayerGameObject playerGameObject;

    /**
     * Creates a new WinView
     *
     * @param s      Lanterna screen to be drawn on
     * @param width  width of the view
     * @param height height of the view
     */
    public WinView(Screen s, int width, int height) {
        super(s, width, height);
    }

    /**
     * Creates a new WinView with an attached player object from which it will take the information.
     *
     * @param s                Lanterna screen to be drawn on
     * @param width            width of the view
     * @param height           height of the view
     * @param playerGameObject Player Game Object containing the displayed information.
     */
    public WinView(Screen s, int width, int height, PlayerGameObject playerGameObject) {
        this(s, width, height);
        this.playerGameObject = playerGameObject;
    }


    /**
     * Other than a regular StaticView, upon winning, the game will close itself when pressing the Escape-Key.
     *
     * @param keystroke
     */
    @Override
    public void processKeystroke(Key keystroke) {
        if (keystroke.getKind() == Key.Kind.Escape) {
            NotificationCenter.postNotification(NotificationMessage.QUIT);
        }
    }

    /**
     * Shows the winning message for the player, containing information such as score and remaining lives.
     *
     * @param deltatime
     * @return
     */
    @Override
    public boolean update(int deltatime) {
        if (!hasDrawnStatics) {
            screen.clear();
            screen.putString(0, 0, "LEGENDARY!", Terminal.Color.GREEN, Terminal.Color.DEFAULT);
            screen.putString(0, 1, "You have beaten the level with " + playerGameObject.getScore() + " points!", Terminal.Color.WHITE, Terminal.Color.DEFAULT);
            screen.putString(0, 2, "You had " + playerGameObject.getLives() + " lives left!", Terminal.Color.WHITE, Terminal.Color.DEFAULT);
            hasDrawnStatics = true;
        }
        return true;
    }

}
