package capstone.views.levelview;

import capstone.gameobject.GameObject;
import capstone.gameobject.dynamicObjects.DynamicGameObject;
import capstone.gameobject.dynamicObjects.PlayerGameObject;
import capstone.gameobject.staticObjects.StaticGameObject;
import capstone.level.Level;
import capstone.level.LevelInputOutput;
import capstone.notificationcenter.NotificationCenter;
import capstone.notificationcenter.NotificationMessage;
import capstone.views.View;
import capstone.views.menu.MenuView;
import capstone.views.staticviews.LoseView;
import capstone.views.staticviews.WinView;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Like any view, the LevelView usually resides within the stack of a ViewManager.
 * It handles all the drawing logic of the game and hands off in-game logic to the attached level object.
 */
public class LevelView extends View {
    private Level level;
    private boolean hasPrintedStatics;
    private int xOffset;
    private int yOffset;
    private HUD hud;

    private View requestedView;
    private boolean requestsStackRemoval;

    /**
     * Creates a new level view based on a lanterna screen and a given filename relative to the working directory.
     * The file contains a structure that is compatible with the requirements given in the capstone document.
     *
     * @param screen   Lanterna Screen to be used for output
     * @param filename Filename of the Level that should be read.
     * @throws IOException in case the level could not be created.
     * @see LevelInputOutput
     */
    public LevelView(Screen screen, String filename) throws IOException {
        this(screen, LevelInputOutput.readLevel(filename));
    }

    /**
     * Creates a new level view based on an already existing level object and a lanterna screen.
     *
     * @param screen Lanterna screen to be used
     * @param level  Level object to be used
     */
    public LevelView(Screen screen, Level level) {
        super(screen, screen.getTerminalSize().getColumns(), screen.getTerminalSize().getRows() - 2);
        this.level = level;
        initializeHUD();

    }

    /**
     * Sets the level for the levelview
     *
     * @param level Level Game Object to be rendered
     */
    public void setLevel(Level level) {
        this.level = level;
    }

    /**
     * Initializes the HUD that shows important player information.
     * (usually called after the first initialization of the LevelView object, and after resizing the screen.)
     */
    private void initializeHUD() {
        hud = new HUD(screen, level.getPlayer(), 0, height, width, height);
        hud.setNeedsUpdate(true);
    }

    /**
     * Processes the user's keystrokes.
     * If the press of the Escape Key is detected, the player game object will be paused, and a menu view will be
     * requested.
     * If a different key is detected, it is assumed the player is trying to interact with the game logic, thus the
     * keystroke will be forwarded to the level.
     *
     * @param keystroke last key press of the player
     */
    @Override
    public void processKeystroke(Key keystroke) {
        if (keystroke.getKind() == Key.Kind.Escape) {
            this.requestedView = new MenuView(screen, width, height, this);
            level.getPlayer().setPaused(true);
        }
        level.processKeystroke(keystroke);
    }

    /**
     * Updates the current view based on the time passed in between.
     *
     * @param deltaTime
     * @return
     */
    @Override
    public boolean update(int deltaTime) {
        //Check whether the game was just unpaused
        if (NotificationCenter.checkForNotification(NotificationMessage.CONTINUE)) {
            NotificationCenter.removeNotifictaion(NotificationMessage.CONTINUE);
            screen.clear();
            level.getPlayer().setPaused(false);
            this.processResize();
        }

        PlayerGameObject playerGameObject = level.getPlayer();
        StaticGameObject[][] staticGameObjects = level.getStaticGameObjects();
        ArrayList<DynamicGameObject> dynamicGameObjects = level.getDynamicGameObjects();
        level.updateEntities(deltaTime);
        if (playerGameObject.isHasReachedExit()) {
            //The player has won the game, we will show a congratulation to him.
            this.requestedView = new WinView(screen, width, height, playerGameObject);
        }

        if (playerGameObject.isDead()) {
            //The player has lost all his lives. We will show him a "Game Over" screen.
            this.requestedView = new LoseView(screen, width, height, this);
        }

        // check whether the player is out of bounds for the camera
        while (playerGameObject.getX() >= xOffset + width || playerGameObject.getY() >= yOffset + height || playerGameObject.getX() < xOffset || playerGameObject.getY() < yOffset) {
            //adjust the camera accordingly
            if (playerGameObject.getX() >= xOffset + width) {
                //scroll by one screen size to the right
                this.xOffset += width;
            }
            if (playerGameObject.getY() >= yOffset + height) {
                //scroll by one screen size down
                this.yOffset += height;
            }
            if (playerGameObject.getX() < xOffset) {
                //scroll by one screen size to the left
                this.xOffset -= width;
            }
            if (playerGameObject.getY() < yOffset) {
                //scroll by one screen size upwards
                this.yOffset -= height;
            }

            // it is possible that there is not enough room for scrolling one whole screen.
            // in that case, adjust scrolling to the maximum.
            if (xOffset + width > level.getLevelWidth()) {
                //adjust to the very right of the level
                this.xOffset = level.getLevelWidth() - width;
            }
            if (yOffset + height > level.getLevelHeight()) {
                //adjust to the very bottom of the level
                this.yOffset = level.getLevelHeight() - height;
            }
            if (xOffset < 0) {
                //adjust to the very left of the level
                this.xOffset = 0;
            }
            if (yOffset < 0) {
                //adjust to the very right of the level
                this.yOffset = 0;
            }

            //the camera has moved, so all static and dynamic objects need to be rendered again.
            this.hasPrintedStatics = false;
            for (DynamicGameObject g : dynamicGameObjects) {
                g.setNeedsUpdate(true);
            }

            //the hud needs to be rendered again since we are going to clear the screen.
            this.hud.setNeedsUpdate(true);
            screen.clear();
        }

        // if necessary, print static objects to the screen.
        if (!hasPrintedStatics) {
            GameObject[][] staticsForFrame = level.requestStaticObjectsForFrame(xOffset, yOffset, width, height);
            for (int x = 0; x < staticsForFrame.length; ++x) {
                for (int y = 0; y < staticsForFrame[0].length; ++y) {
                    GameObject g = staticsForFrame[x][y];
                    if (g != null) {
                        screen.putString(x, y, Character.toString(g.getRepresentation()), g.getForegroundColor(), g.getBackgroundColor());
                    }
                }
            }
            hasPrintedStatics = true;
        }

        // order the game to do the game logic necessary for the game to progress.


        //redraw the dynamic game objects based on necessity.
        for (DynamicGameObject d : dynamicGameObjects) {
            if (d.isNeedsUpdate()) {
                int x = d.getX();
                int y = d.getY();
                int oldX = d.getOldX();
                int oldY = d.getOldY();
                if (!(x < xOffset || y < yOffset - 1 || x >= xOffset + width || y >= yOffset + height + 1) || (x == -1 && y == -1)) {
                    if (!(y >= yOffset + height)) {
                        screen.putString(x - xOffset, y - yOffset, d.toString(), d.getForegroundColor(), d.getBackgroundColor());
                    }
                    if (oldX != x || oldY != y) {
                        StaticGameObject staticGameObject = staticGameObjects[oldX][oldY];
                        if (staticGameObject != null) {
                            screen.putString(oldX, oldY, staticGameObject.toString(), staticGameObject.getForegroundColor(), staticGameObject.getBackgroundColor());
                        } else if (!(oldY >= yOffset + height)) {
                            screen.putString(d.getOldX() - xOffset, d.getOldY() - yOffset, " ", Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
                        }
                    }
                    d.setNeedsUpdate(false);
                }
            }
        }
        hud.render();
        return true;
    }

    /**
     * Processes the resize of the screen by redrawing the static game objects in the next frame, updating all the
     * dynamic game objects, and adjusting width and height of view and HUD.
     */
    @Override
    public void processResize() {
        this.hasPrintedStatics = false;
        for (DynamicGameObject d : level.getDynamicGameObjects()) {
            d.setNeedsUpdate(true);
        }
        level.getPlayer().setNeedsUpdate(true);
        width = screen.getTerminalSize().getColumns();
        height = screen.getTerminalSize().getRows() - 2;
        initializeHUD();
    }

    /**
     * If the LevelView requires another view to be drawn, it will present it's wish to the callers via this method.
     *
     * @return requested View that is supposed to be shown above the current one.
     */
    @Override
    public View requestsViewStackAddition() {
        return requestedView;
    }

    /**
     * Used by the external caller to indicate that the requested view has been shown. Makes sure a view is not
     * requested multiple times.
     */
    @Override
    public void resetViewStackAddition() {
        this.requestedView = null;
    }

    /**
     * Indicates whether this level view has the intent to leave the view stack.
     *
     * @return LevelView wants to leave the view stack
     */
    @Override
    public boolean requestsViewStackRemoval() {
        return requestsStackRemoval;
    }

    /**
     * Management Update function that is called regardless of whether this is the top view or not.
     * Reacts to Notifications about saving and loading levels.
     */
    @Override
    public void managementUpdate() {
        if (NotificationCenter.checkForNotification(NotificationMessage.MAIN_MENU)) {
            //todo implement or remove, sucker!
            this.requestsStackRemoval = true;
        }
        if (NotificationCenter.checkForNotification(NotificationMessage.SAVE_LOAD)) {
            try {
                level = LevelInputOutput.readLevel("save.properties");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            NotificationCenter.removeNotifictaion(NotificationMessage.SAVE_LOAD);
        }
    }

    public Level getLevel() {
        return this.level;
    }
}
