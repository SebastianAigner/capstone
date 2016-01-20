package capstone.views.staticviews;

import capstone.gameobject.dynamicObjects.PlayerGameObject;
import capstone.level.LevelInputOutput;
import capstone.notificationcenter.NotificationCenter;
import capstone.notificationcenter.NotificationMessage;
import capstone.views.levelview.LevelView;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

/**
 * The Win View is shown to the player when he has successfully completed the game / a level. It shows interesting
 * information like score and remaining lives.
 */
public class WinView extends StaticView {
    private PlayerGameObject playerGameObject;
    private LevelView levelView;

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
     *  @param s                Lanterna screen to be drawn on
     * @param width            width of the view
     * @param height           height of the view
     * @param playerGameObject Player Game Object containing the displayed information.
     * @param levelView
     */
    public WinView(Screen s, int width, int height, PlayerGameObject playerGameObject, LevelView levelView) {
        this(s, width, height);
        this.playerGameObject = playerGameObject;
        this.levelView = levelView;
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
        if (keystroke.getKind() == Key.Kind.Enter) {
            reloadLevelAndContinue();
            this.viewStackRemoval = true;
        }
    }

    private void reloadLevelAndContinue() {
        try {
            this.levelView.setLevel(LevelInputOutput.readLevel(levelView.getLevel().getLevelName()));
            viewStackRemoval = true;
            NotificationCenter.postNotification(NotificationMessage.CONTINUE);
        } catch (IOException ex) {
            System.out.println("Could not reload level.");
            ex.printStackTrace();
            System.exit(1);
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
            screen.putString(0, 3, "Press Escape to end the game. Enter to restart the level!", Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
            hasDrawnStatics = true;
        }
        return true;
    }

}
