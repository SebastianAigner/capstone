package capstone.views.levelview;

/**
 * The HUD is used to display valuable game information to the player. It is usually rendered alongside with the level
 * view to keep the player updated about his status in the game.
 */

import capstone.DeltaTimeHelper;
import capstone.gameobject.dynamicObjects.PlayerGameObject;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

public class HUD {
    private final Screen screen;
    private PlayerGameObject player;
    private int xOffset, yOffset;
    private int width, height;

    private DeltaTimeHelper deltaTimeHelper;
    private boolean needsUpdate;

    //Keep track of the player information displayed for efficient rendering.
    private int lives;
    private boolean hasKey;
    private int score;
    private int deltaScore;
    private int scoreSnapshot;

    //Configuration for the HUD display.

    //Display Lives / Hearts
    private int heartsXOffset = 0;
    private int heartsYOffset = 0;
    private Terminal.Color heartsColor = Terminal.Color.RED;
    private Terminal.Color heartsBackgroundColor = Terminal.Color.DEFAULT;
    private String heartsRepresentation = "♥";

    //Display whether the player has collected a key
    private int hasKeyXOffset = 0;
    private int hasKeyYOffset = 1;
    private Terminal.Color hasKeyColor = Terminal.Color.YELLOW;
    private Terminal.Color hasKeyBackgroundColor = Terminal.Color.DEFAULT;

    //Display the player score
    private int scoreXOffset = 15;
    private int scoreYOffset = 1;
    private Terminal.Color scoreColor = Terminal.Color.GREEN;
    private Terminal.Color scoreBackgroundColor = Terminal.Color.DEFAULT;

    /**
     * Creates a new HUD that can then be rendered alongside a level view.
     *
     * @param screen  Lanterna screen to render to
     * @param player  Player object from which the information displayed will be extracted.
     * @param xOffset x offset on the screen
     * @param yOffset y offset on the screen
     * @param width   width of the HUD
     * @param height  height of the HUD
     */
    public HUD(Screen screen, PlayerGameObject player, int xOffset, int yOffset, int width, int height) {
        this.screen = screen;
        this.player = player;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
        this.deltaTimeHelper = new DeltaTimeHelper();
        this.needsUpdate = true;
    }

    /**
     * Flushes the display for the lives / hearts.
     */
    private void flushHearts() {
        screen.putString(xOffset + heartsXOffset, yOffset + heartsYOffset, "                    ", Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
    }

    /**
     * Flushes the display that indicates whether the player has already picked up a key
     */
    private void flushKey() {
        screen.putString(xOffset + hasKeyXOffset, yOffset + hasKeyYOffset, "        ", hasKeyColor, hasKeyBackgroundColor);
    }

    /**
     * Flushes the part of the display that is used to show the score.
     */
    private void flushScore() {
        screen.putString(xOffset + scoreXOffset, yOffset + scoreYOffset, "                             ", scoreColor, scoreBackgroundColor);
    }

    /**
     * Makes sure the HUD gets redrawn the next tick.
     *
     * @param needsUpdate
     */
    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    /**
     * Renders the HUD with all the information extracted from the Player Game Object (but only if information has
     * actually changed.)
     */
    public void render() {
        if (lives != player.getLives() || needsUpdate) {
            flushHearts();
            lives = player.getLives();
            if (lives > width) {
                lives = width;
            }
            String hearts = " ";
            for (int i = 0; i < lives; ++i) {
                hearts += heartsRepresentation;
                hearts += " ";
            }
            screen.putString(xOffset + heartsXOffset, yOffset + heartsYOffset, hearts, heartsColor, heartsBackgroundColor);
        }

        if (hasKey != player.isHasKey() || needsUpdate) {
            flushKey();
            hasKey = player.isHasKey();
            String haveKey = "HAS KEY: " + (hasKey ? "YES" : "NO");
            screen.putString(xOffset + hasKeyXOffset, yOffset + hasKeyYOffset, haveKey, hasKeyColor, hasKeyBackgroundColor);
        }
        if (score != player.getScore() || needsUpdate) {
            flushScore();
            if (deltaTimeHelper.getDeltaTime() >= 1000) {
                deltaScore = player.getScore() - scoreSnapshot;
                scoreSnapshot = player.getScore();
                deltaTimeHelper.reset();
                deltaScore /= 1000;
                deltaScore *= 1000;
            }

            score = player.getScore();
            String currentScore = "SCORE: " + score + " ";
            if (deltaScore > 100) {
                currentScore += "+" + deltaScore;
            } else if (deltaScore < -1000) {
                currentScore += deltaScore;
            }
            screen.putString(xOffset + scoreXOffset, yOffset + scoreYOffset, currentScore, scoreColor, scoreBackgroundColor);
        }
        needsUpdate = false;
    }
}

