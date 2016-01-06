package capstone.gameobject.dynamicObjects;

import capstone.DeltaTimeHelper;
import capstone.ScoringHelper;
import capstone.gameobject.dynamicObjects.BulletGameObject.Direction;
import capstone.gameobject.staticObjects.StaticGameObject;
import capstone.level.Level;
import capstone.level.LevelHelper;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.ArrayList;


/**
 * The player game object holds all the information about the player and is responsible for interacting, such as
 * moving by pressing the corresponding keys.
 */
public class PlayerGameObject extends DynamicGameObject {
    private int lives;
    private boolean hasKey;
    private int score;
    private boolean hasReachedExit;
    private boolean isPaused;
    private DeltaTimeHelper deltaTimeHelper;
    private DeltaTimeHelper bulletTimeHelper;
    private ArrayList<BulletGameObject> bullets;

    /**
     * Creates a new player object at the given coordinate with the given parameters.
     * @param x      x coordinate of the player
     * @param y      y coordinate of the player
     * @param lives  amount of lives the player has
     * @param l      level in which the player resides
     * @param hasKey whether the player has a key in his inventory.
     */
    public PlayerGameObject(int x, int y, int lives, Level l, boolean hasKey) {
        super(x, y, l);
        this.lives = lives;
        this.representation = 'X';
        this.backgroundColor = Terminal.Color.GREEN;
        this.foregroundColor = Terminal.Color.WHITE;
        this.score = ScoringHelper.getBaseValue();
        this.hasKey = hasKey;
        this.entityName = "Player";
        this.deltaTimeHelper = new DeltaTimeHelper();
        this.bulletTimeHelper = new DeltaTimeHelper();
        this.bullets = new ArrayList<>();
        this.savable = false;
    }

    /**
     * Whether the player is paused or not.
     *
     * @return is the player paused
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Set the player pause state.
     *
     * @param paused whether player is paused
     */
    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    /**
     * Pick up a key. Sets the variable so that the player now holds a key.
     */
    public void takeKey() {
        System.out.println("Has picked up key.");
        level.removeStaticGameObject(x, y);
        this.hasKey = true;
    }

    /**
     * Sets the flag that the player has successfully reached the exit.
     *
     * @param hasReachedExit the player has reached the exit
     */
    public void setHasReachedExit(boolean hasReachedExit) {
        System.out.println("Player has reached exit " + Boolean.toString(hasReachedExit));
        this.hasReachedExit = hasReachedExit;
    }

    /**
     * Whether the player has reached the exit successfully.
     *
     * @return player has reached the exit successfully
     */
    public boolean isHasReachedExit() {
        return hasReachedExit;
    }

    /**
     * Get the amount of lives the player still has
     *
     * @return amount of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Whether the player has a key
     *
     * @return player has a key
     */
    public boolean isHasKey() {
        return hasKey;
    }

    /**
     * whether the player is dead.
     *
     * @return true if there are no lives left.
     */
    public boolean isDead() {
        return lives <= 0;
    }

    /**
     * Modifies the player. For now, the player will not modify himself.
     *
     * @param p a reference to the player object.
     */
    @Override
    public void modifyPlayer(PlayerGameObject p) {
    }

    /**
     * Updates the player variables. For now, only score will be deducted amounting to the time that has passed
     * since the last call. The player thus loses 1000 score per second.
     *
     * @param deltatime time that has passed since last call
     */
    @Override
    public void update(int deltatime) {
        if (!isPaused) {
            score -= deltatime;
            if (score < 0) {
                score = 0;
            }
        }
        if (deltaTimeHelper.getDeltaTime() > 500) {
            if (this.representation == 'X') {
                this.representation = '+';
            } else {
                this.representation = 'X';
            }
            deltaTimeHelper.reset();
        }
        ArrayList<BulletGameObject> bulletsCopy = new ArrayList<>(bullets);
        for (BulletGameObject b : bulletsCopy) {
            if (b.isCollided()) {
                //remove collided bullets from the players memory
                bullets.remove(b);
            }
            if (b.getX() == x && b.getY() == y && b.isHasMoved()) {
                //the player has walked into one of his own bullets and thus needs to be redrawn
                this.needsUpdate = true;
            }
        }
    }

    /**
     * Process the player input and move the player game object accordingly while taking collisions into account.
     * Also allows the player to throw shurikens in all directions by pressing the WASD buttons.
     *
     * @param keystroke last pressed key by the player
     */
    public void processKeystroke(Key keystroke) {
        if (keystroke != null) {
            oldX = x;
            oldY = y;
            switch (keystroke.getKind()) {
                case ArrowUp:
                    if (LevelHelper.checkWalkable(level, x, y - 1, true)) {
                        --y; //go up one
                        needsUpdate = true;
                    }
                    break;
                case ArrowDown:
                    if (LevelHelper.checkWalkable(level, x, y + 1, true)) {
                        ++y; //go up one
                        needsUpdate = true;
                    }
                    break;
                case ArrowLeft:
                    if (LevelHelper.checkWalkable(level, x - 1, y, true)) {
                        --x; //go up one
                        needsUpdate = true;
                    }
                    break;
                case ArrowRight:
                    if (LevelHelper.checkWalkable(level, x + 1, y, true)) {
                        ++x; //go up one
                        needsUpdate = true;
                    }
                    break;
                default:
                    Direction d;
                    //the player has not pressed a directional key, so we assume he is trying to use WASD to fire
                    //shurikens
                    switch (keystroke.getCharacter()) {
                        case 'w':
                            d = Direction.UP;
                            break;
                        case 'a':
                            d = Direction.LEFT;
                            break;
                        case 's':
                            d = Direction.DOWN;
                            break;
                        case 'd':
                            d = Direction.RIGHT;
                            break;
                        default:
                            d = null;
                    }
                    if (d != null) {
                        if (bulletTimeHelper.getDeltaTime() > 1000) {
                            //limit the rate at which bullets can be fired
                            BulletGameObject firedBullet = new BulletGameObject(x, y, level, d);
                            bullets.add(firedBullet);
                            level.addDynamicGameobject(firedBullet);
                            bulletTimeHelper.reset();
                        }
                    }

            }
            //redraw the player next tick
            needsUpdate = true;
        }
    }

    /**
     * Modify the amount of lives the player still has given the parameter
     *
     * @param livesDelta amount of lives to add or subtract (for negative values)
     */
    public void modifyLives(int livesDelta) {
        System.out.println("Modifying lives " + livesDelta);
        lives += livesDelta;
        StaticGameObject currentStandingGameObject = level.getStaticGameObjects()[x][y];
        if (currentStandingGameObject != null) {
            if (level.getStaticGameObjects()[x][y].isPickup()) {
                level.removeStaticGameObject(x, y);
            }
        }
    }

    /**
     * Gets the score of the player. The score will always be positive or 0.
     *
     * @return score of the player
     */
    public int getScore() {
        return score > 0 ? score : 0;
    }

    /**
     * Modifies the score by the given delta.
     *
     * @param delta amount by which the score should be changed.
     */
    public void modifyScore(int delta) {
        score += delta;
    }

    /**
     * Sets the score to a certain value.
     *
     * @param score fixed value to which the score will be set.
     */
    public void setScore(int score) {
        this.score = score;
    }
}
