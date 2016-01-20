package capstone.views;

import capstone.level.LevelInputOutput;
import capstone.notificationcenter.NotificationCenter;
import capstone.notificationcenter.NotificationMessage;
import capstone.views.levelview.LevelView;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Upon request, the user can be presented with a Level Load View. It shows the player all the available levels /
 * files ending in .properties. The user can then type a filename. Upon pressing Enter, the game will load the level
 * and catapult him into the action.
 */
public class LevelLoadView extends View {

    private boolean hasDrawnStatics;
    private String filename = "";
    private String oldFilename = "";
    private String promptPrefix = "filename: ";
    private String statusLine = "";
    private boolean statusLineDrawn;
    private LevelView levelView;
    private ArrayList<String> fileNames;

    /**
     * Generates a new view with the given parameters.
     *
     * @param s      Lanterna Screen that is supposed to be used.
     * @param width  Width of the View in Characters
     * @param height Height of the View in Characters
     * @param levelView levelView to which to write the newly loaded level
     */
    public LevelLoadView(Screen s, int width, int height, LevelView levelView) {
        super(s, width, height);
        this.levelView = levelView;
    }

    /**
     * Process the player keystrokes. If it is a normal character, write it to the variable that will be printed to
     * the screen in the update routine. Also enables the backspace and escape keys.
     *
     * @param keystroke keystroke of the player
     */
    @Override
    public void processKeystroke(Key keystroke) {
        if (keystroke.getKind() == Key.Kind.NormalKey) {
            filename += keystroke.getCharacter();
        }
        switch (keystroke.getKind()) {
            case Escape:
                viewStackRemoval = true;
                break;
            case Backspace:
                if (filename.length() > 0) {
                    filename = filename.substring(0, filename.length() - 1);
                }
                break;
            case Enter:
                loadLevel();
                break;
            default:
        }
    }

    /**
     * Updates the view based on the passed time. Will draw the characters of the typed filename and intelligently
     * add or remove new characters to the screen.
     * @param deltatime
     * @return successful screen update
     */
    @Override
    public boolean update(int deltatime) {
        if (!hasDrawnStatics) {
            screen.clear();
            hasDrawnStatics = true;
            screen.putString(0, 0, promptPrefix, Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);

            //draw available files
            ArrayList<String> availableLevels = LevelInputOutput.getAvailableLevels();
            screen.putString(0, 6, "Available levels to load in " + System.getProperty("user.dir") + ":", Terminal.Color.GREEN, Terminal.Color.DEFAULT);
            for (int i = 0; i < availableLevels.size(); ++i) {
                screen.putString(0, 7 + i, availableLevels.get(i), Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
            }
        }
        if (!filename.equals(oldFilename)) {
            oldFilename = filename;
            screen.putString(promptPrefix.length(), 0, filename, Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
            screen.putString(promptPrefix.length() + filename.length(), 0, " ", Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
        }
        if (!statusLineDrawn) {
            screen.putString(0, 3, statusLine, Terminal.Color.RED, Terminal.Color.DEFAULT);
        }
        return true;
    }

    /**
     * Ensures that upon resizing the window all the contents will be redrawn and not "flushed out" by the padding
     * characters
     */
    @Override
    public void processResize() {
        hasDrawnStatics = false;
        oldFilename = "";
        statusLineDrawn = false;
    }


    @Override
    public boolean requestsViewStackRemoval() {
        return viewStackRemoval;
    }

    @Override
    public void managementUpdate() {

    }

    @Override
    public void resetViewStackAddition() {

    }

    public void loadLevel() {
        try {
            this.levelView.setLevel(LevelInputOutput.readLevel(filename));
            this.viewStackRemoval = true;
            NotificationCenter.postNotification(NotificationMessage.CONTINUE);
        } catch (IOException | IllegalArgumentException ex) {
            ex.printStackTrace();
            this.statusLine = "Loading level " + filename + " failed. Maybe check filename?";
            this.filename = "";
            this.statusLineDrawn = false;
            hasDrawnStatics = false;
        }
    }
}
