package capstone.views.menu;

import capstone.level.Level;
import capstone.level.LevelInputOutput;
import capstone.notificationcenter.NotificationCenter;
import capstone.notificationcenter.NotificationMessage;
import capstone.views.LevelLoadView;
import capstone.views.View;
import capstone.views.levelview.LevelView;
import capstone.views.staticviews.LegendView;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Menu View allows users navigate a structure of menu points, Choices, that will in return trigger notifications
 * that can be read using the Notification Center.
 *
 * @see NotificationCenter
 */
public class MenuView extends View {
    private boolean hasDrawnText;
    private Level level;
    private Terminal.Color textColorForeground = Terminal.Color.WHITE;
    private Terminal.Color textColorBackground = Terminal.Color.DEFAULT;
    private LevelView levelView;
    private String statusLine = "";
    private boolean statusLineDrawn = false;
    private int choiceListEndPosition = 0;
    private ArrayList<Choice> choices = new ArrayList<>(Arrays.asList(
            new Choice("Legend", NotificationMessage.LEGEND),
            new Choice("Save Current Game", NotificationMessage.SAVE_SAVE),
            new Choice("Load Save File", NotificationMessage.SAVE_LOAD),
            new Choice("Load file by name", NotificationMessage.LEVEL_LOAD_BY_NAME),
            new Choice("Save+Quit", NotificationMessage.SAVE_QUIT),
            new Choice("Quit", NotificationMessage.QUIT)
    ));
    private int cursorPosition = 0;

    /**
     * Creates a new Menu View that allows the user to select different choices.
     *
     * @param s         Lanterna screen to be drawn on
     * @param width     width of the view
     * @param height    height of the view
     * @param levelView LevelView that is currently loaded
     */
    public MenuView(Screen s, int width, int height, LevelView levelView) {
        super(s, width, height);
        this.levelView = levelView;
        this.level = levelView.getLevel();
        if (level != null) {
            // There exists a level "below" the menu, so we can add a "continue" message.
            choices.add(0, new Choice("Continue", NotificationMessage.CONTINUE));
        }
    }

    /**
     * Processes the keystrokes by the user. Allows him to move the caret up and down, use the Enter button to select
     * an entry, and use escape to continue the underlying game.
     *
     * @param keystroke last button the user pressed
     */
    @Override
    public void processKeystroke(Key keystroke) {
        if (keystroke != null) {
            switch (keystroke.getKind()) {
                case ArrowUp:
                    --cursorPosition;
                    break;
                case ArrowDown:
                    ++cursorPosition;
                    break;
                case Enter:
                    click(cursorPosition);
                    break;
                case Escape:
                    NotificationCenter.postNotification(NotificationMessage.CONTINUE);
                    screen.clear();
                    break;
                default:
            }
            hasDrawnText = false;
        }
        if (cursorPosition < 0) {
            cursorPosition = choices.size() - 1;
        }
        if (cursorPosition >= choices.size()) {
            cursorPosition = 0;
        }
    }

    /**
     * Triggers a choice based on caret position
     *
     * @param position position of the cursor
     */
    private void click(int position) {
        Choice clicked = choices.get(position);
        clicked.trigger();
        screen.clear();
    }

    /**
     * Redraws the menu upon change.
     *
     * @param deltatime
     * @return
     */
    @Override
    public boolean update(int deltatime) {
        if (!hasDrawnText) {
            hasDrawnText = true;
            screen.clear();
            screen.putString(3, 1, "THE ADVENTURES OF X", Terminal.Color.WHITE, Terminal.Color.DEFAULT);
            for (int i = 0; i < choices.size(); ++i) {
                String cursorPlace = " ";
                if (cursorPosition == i) {
                    cursorPlace = ">";
                }
                screen.putString(2, 2 + i, cursorPlace + choices.get(i), textColorForeground, textColorBackground);
                choiceListEndPosition = 2 + i;
            }
        }
        if (!statusLineDrawn) {
            screen.putString(2, choiceListEndPosition + 2, statusLine, Terminal.Color.GREEN, Terminal.Color.DEFAULT);
            statusLineDrawn = true;
        }
        return true;
    }

    /**
     * If the screen size has changed, make sure all the text is drawn again.
     */
    @Override
    public void processResize() {
        hasDrawnText = false;
    }

    /**
     * Checks for different tasks such as continuing the game, saving a game, quitting the game and creating subviews
     * such as legends.
     */
    @Override
    public void managementUpdate() {
        if (NotificationCenter.checkForNotification(NotificationMessage.CONTINUE)) {
            //Notice however that the Notification does not get removed here. Instead, it is used in the LevelView to
            //Ensure that all on-screen elements will be redrawn correctly.
            viewStackRemoval = true;
        }
        if (NotificationCenter.checkForNotification(NotificationMessage.SAVE_SAVE)) {
            saveCurrentLevel();
            NotificationCenter.removeNotifictaion(NotificationMessage.SAVE_SAVE);
        }
        if (NotificationCenter.checkForNotification(NotificationMessage.SAVE_QUIT)) {
            saveCurrentLevel();
            NotificationCenter.removeNotifictaion(NotificationMessage.SAVE_QUIT);
            NotificationCenter.postNotification(NotificationMessage.QUIT);
        }
        if (NotificationCenter.checkForNotification(NotificationMessage.LEGEND)) {
            NotificationCenter.removeNotifictaion(NotificationMessage.LEGEND);
            this.viewStackAddition = new LegendView(screen, width, height);
        }
        if (NotificationCenter.checkForNotification(NotificationMessage.SAVE_LOAD_SUCCESS)) {
            NotificationCenter.removeNotifictaion(NotificationMessage.SAVE_LOAD_SUCCESS);
            NotificationCenter.postNotification(NotificationMessage.CONTINUE);
        }
        if (NotificationCenter.checkForNotification(NotificationMessage.LEVEL_LOAD_BY_NAME)) {
            NotificationCenter.removeNotifictaion(NotificationMessage.LEVEL_LOAD_BY_NAME);
            this.viewStackAddition = new LevelLoadView(screen, width, height, levelView);
        }
        if (NotificationCenter.checkForNotification(NotificationMessage.SAVE_SAVE_SUCCESS)) {
            this.statusLineDrawn = false;
            this.statusLine = "Successfully saved file to save.properties.";
            NotificationCenter.removeNotifictaion(NotificationMessage.SAVE_SAVE_SUCCESS);
        }

    }

    /**
     * Saves the current level to a file called "save.properties".
     */
    private void saveCurrentLevel() {
        try {
            LevelInputOutput.writeLevel(level, "save.properties");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
