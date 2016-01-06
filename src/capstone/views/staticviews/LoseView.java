package capstone.views.staticviews;

import capstone.level.LevelInputOutput;
import capstone.notificationcenter.NotificationCenter;
import capstone.notificationcenter.NotificationMessage;
import capstone.views.levelview.LevelView;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

/**
 * Upon death the player will be presented with a Lose View.
 */
public class LoseView extends StaticView {
    private LevelView levelView;

    /**
     * Creates a new Lose View
     *
     * @param s      Lanterna screen to be drawn on
     * @param width  width of the screen
     * @param height height of the screen
     */
    public LoseView(Screen s, int width, int height, LevelView levelView) {
        super(s, width, height);
        this.levelView = levelView;
    }


    /**
     * Processes user Keystrokes. Upon pressing Escape, the game will be ended.
     * Upon pressing Enter, the level will be reloaded and the player is given another chance.
     *
     * @param keystroke User Input.
     */
    @Override
    public void processKeystroke(Key keystroke) {
        switch (keystroke.getKind()) {
            case Escape:
                NotificationCenter.postNotification(NotificationMessage.QUIT);
                break;
            case Enter:
                reloadLevelAndContinue();
            default:
        }
        //todo press enter to restart
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
     * Draws a "You Lose" message on the screen. Only redraws when necessary.
     *
     * @param deltatime
     * @return
     */
    @Override
    public boolean update(int deltatime) {
        if (!hasDrawnStatics) {
            screen.clear();
            screen.putString(0, 0, "DISASTROUS :(", Terminal.Color.RED, Terminal.Color.DEFAULT);
            screen.putString(0, 1, "You have lost the game. Esc to end. Enter to retry.", Terminal.Color.WHITE, Terminal.Color.DEFAULT);
            hasDrawnStatics = true;
        }
        return true;
    }
}
