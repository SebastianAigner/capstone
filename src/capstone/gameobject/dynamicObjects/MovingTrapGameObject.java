package capstone.gameobject.dynamicObjects;

import capstone.DeltaTimeHelper;
import capstone.level.Level;
import capstone.level.LevelHelper;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.Random;

public class MovingTrapGameObject extends DynamicGameObject {
    private final DeltaTimeHelper deltaTimeHelper;
    private DeltaTimeHelper damageDeltaTimeHelper;
    private final DeltaTimeHelper animationTimeHelper;
    private int animationStep = 0;
    private int scoreDeduction;
    private int playerLocationX;
    private int playerLocationY;
    private Random random;
    /**
     * Creates a new moving trap at a given X/Y-spawn position in a given level.
     *
     * @param x initial x coordinate of the trap
     * @param y initial y coordinate of the trap
     * @param l the level that contains the trap
     */
    public MovingTrapGameObject(int x, int y, Level l) {
        this(x, y, -10000, l);
    }

    public MovingTrapGameObject(int x, int y, int scoreDeduction, Level l) {
        super(x, y, l);
        this.representation = '|';
        this.foregroundColor = Terminal.Color.RED;
        this.backgroundColor = Terminal.Color.BLACK;
        this.entityName = "Moving Trap";
        this.scoreDeduction = scoreDeduction;
        this.deltaTimeHelper = new DeltaTimeHelper();
        this.damageDeltaTimeHelper = new DeltaTimeHelper();
        this.animationTimeHelper = new DeltaTimeHelper();
        this.animationStep = 0;
        this.destroyable = true;
        this.savable = true;
        random = new Random();
    }

    /**
     * Checks whether the player stands on the trap and damages him if so.
     * A DeltaTimeHelper is used to ensure that the player does not get hurt repeatedly too quickly. He will take
     * a maximum of 1 damage per second.
     *
     * @param p a reference to the player object.
     */
    @Override
    public void modifyPlayer(PlayerGameObject p) {
        this.playerLocationX = p.getX();
        this.playerLocationY = p.getY();
        if (x == p.getX() && y == p.getY()) {
            needsUpdate = true;
            if (damageDeltaTimeHelper.getDeltaTime() > 1000) {
                damageDeltaTimeHelper.reset();
                p.modifyLives(-1);
                p.modifyScore(scoreDeduction);
            }
        }
        p.setNeedsUpdate(true);
        //Redraw the player upon collision
    }

    /**
     * Randomly and "frame-independently" moves the trap in a random direction.
     * This method also checks whether collisions are possible, and if so, does not move the trap.
     *
     * @param deltaTime passed time since last call
     */
    @Override
    public void update(int deltaTime) {
        updateRepresentation();
        //System.out.println(internalDeltaCounter);
        if (deltaTimeHelper.getDeltaTime() > 500) {
            deltaTimeHelper.reset();
            oldX = x;
            oldY = y;
            //determine walking direction by throw of dice
            int direction = chooseDirection();
            switch (direction) {
                case 0:
                    //up
                    if (LevelHelper.checkWalkable(level, x, y - 1, false)) {
                        --y;
                    }
                    break;
                case 1:
                    //down
                    if (LevelHelper.checkWalkable(level, x, y + 1, false)) {
                        ++y;
                    }
                    break;
                case 2:
                    //left
                    if (LevelHelper.checkWalkable(level, x - 1, y, false)) {
                        --x;
                    }
                    break;
                case 3:
                    //right
                    if (LevelHelper.checkWalkable(level, x + 1, y, false)) {
                        ++x;
                    }
                    break;
            }
            if (x != oldX || y != oldY) {
                needsUpdate = true;
            }
        }
    }

    private int chooseDirection() {
        int direction = 0;
        int playerDeltaX = x - playerLocationX;
        int playerDeltaY = y - playerLocationY;
        if (Math.abs(playerDeltaX) > Math.abs(playerDeltaY)) {
            if (playerDeltaX > 0) {
                direction = 2; //go left
            } else {
                direction = 3; //go right
            }
        } else {
            if (playerDeltaY > 0) {
                direction = 0; //go up
            } else {
                direction = 1; //go down
            }
        }
        if (random.nextInt(5) > 2) {
            //throw in some random movement just for the natural movement and to prevent getting stuck in a corner
            return random.nextInt(4);
        }
        return direction;
    }

    private void updateRepresentation() {
        if (animationTimeHelper.getDeltaTime() > 100) {
            animationStep++;
            animationStep %= 4;
            switch (animationStep) {
                case 0:
                    this.representation = '|';
                    break;
                case 1:
                    this.representation = '/';
                    break;
                case 2:
                    this.representation = '-';
                    break;
                case 3:
                    this.representation = '\\';
                    break;
                default:
            }
            animationTimeHelper.reset();
            this.needsUpdate = true;
        }
    }
}
