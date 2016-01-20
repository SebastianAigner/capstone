package capstone;

import capstone.notificationcenter.NotificationCenter;
import capstone.notificationcenter.NotificationMessage;
import capstone.views.levelview.LevelView;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

import java.io.IOException;

public class Main {
    static String startLevel = "level_big_sparse.properties";

    /**
     * Starts the game. Sets up timekeeping devices, a new Swing Terminal, sets the properties required, generates a new
     * View Manager instance, and starts the main game loop.
     *
     * @param args
     */
    public static void main(String[] args) {
        DeltaTimeHelper dt = new DeltaTimeHelper(); //used to keep track of time between refreshes
        Screen screen = TerminalFacade.createScreen(new SwingTerminal());
        screen.getTerminal().setCursorVisible(false); //attempt to hide the cursor
        //padding characters can cause issues upon resize
        screen.setPaddingCharacter(' ', Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
        screen.startScreen();
        ViewManager viewManager = new ViewManager();
        LevelView levelView = null;
        //read the first level and embed in in a LevelView
        try {
            levelView = new LevelView(screen, startLevel);
        } catch (IOException ex) {
            System.out.println("Could not load start level! Please ensure that "
                    + System.getProperty("user.dir") + startLevel + " exists and is in the correct format!");
            ex.printStackTrace();
            System.exit(1);
            //Failure to load the level results in ending the program
        }
        //add the levelView to the viewManager's stack
        viewManager.push(levelView);
        //main game loop, will be executed until a QUIT notification is received
        while (!NotificationCenter.checkForNotification(NotificationMessage.QUIT)) {
            int delta = dt.getDeltaTime();
            dt.reset();
            viewManager.update(delta);
            Key key = screen.getTerminal().readInput();
            if (key != null) {
                viewManager.processKeystroke(key);
            }
            if (screen.resizePending()) {
                screen.clear();
                viewManager.processResize();
            }
            try {
                screen.refresh();
            } catch (ArrayIndexOutOfBoundsException e) {
                //this just means that a character was written outside the possible screen. It is discarded and not handled
                // because this is a common occurrence when changing the terminal's size.
            }
        }
        screen.stopScreen();
    }

}
