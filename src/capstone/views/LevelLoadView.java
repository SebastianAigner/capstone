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

public class LevelLoadView extends View {

    private boolean hasDrawnStatics;
    private String filename = "";
    private String oldFilename = "";
    private String promptPrefix = "filename: ";
    private String statusLine = "";
    private boolean statisLineDrawn;
    private boolean requestsViewStackRemoval = false;
    private LevelView levelView;
    private ArrayList<String> fileNames;

    /**
     * Generates a new view with the given parameters.
     *
     * @param s      Lanterna Screen that is supposed to be used.
     * @param width  Width of the View in Characters
     * @param height Height of the View in Characters
     */
    public LevelLoadView(Screen s, int width, int height, LevelView levelView) {
        super(s, width, height);
        this.levelView = levelView;
    }

    @Override
    public void processKeystroke(Key keystroke) {
        if (keystroke.getKind() == Key.Kind.NormalKey) {
            filename += keystroke.getCharacter();
        }
        switch (keystroke.getKind()) {
            case Escape:
                requestsViewStackRemoval = true;
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

    @Override
    public boolean update(int deltatime) {
        if (!hasDrawnStatics) {
            screen.clear();
            hasDrawnStatics = true;
            screen.putString(0, 0, promptPrefix, Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);

            //draw available files
            ArrayList<String> availableLevels = LevelInputOutput.getAvailableLevels();
            screen.putString(0, 6, "Available levels to load:", Terminal.Color.GREEN, Terminal.Color.DEFAULT);
            for (int i = 0; i < availableLevels.size(); ++i) {
                screen.putString(0, 7 + i, availableLevels.get(i), Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
            }
        }
        if (!filename.equals(oldFilename)) {
            oldFilename = filename;
            screen.putString(promptPrefix.length(), 0, filename, Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
            screen.putString(promptPrefix.length() + filename.length(), 0, " ", Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
        }
        if (!statisLineDrawn) {
            screen.putString(0, 3, statusLine, Terminal.Color.RED, Terminal.Color.DEFAULT);
        }
        return true;
    }

    @Override
    public void processResize() {
        hasDrawnStatics = false;
        oldFilename = "";
        statisLineDrawn = false;
    }

    @Override
    public View requestsViewStackAddition() {
        return null;
    }

    @Override
    public boolean requestsViewStackRemoval() {
        return requestsViewStackRemoval;
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
            this.requestsViewStackRemoval = true;
            NotificationCenter.postNotification(NotificationMessage.CONTINUE);
        } catch (IOException | IllegalArgumentException ex) {
            ex.printStackTrace();
            this.statusLine = "Loading level " + filename + " failed. Maybe check filename?";
            this.filename = "";
            this.statisLineDrawn = false;
            hasDrawnStatics = false;
        }
    }
}
