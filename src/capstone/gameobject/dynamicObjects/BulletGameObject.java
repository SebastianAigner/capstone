package capstone.gameobject.dynamicObjects;

import capstone.DeltaTimeHelper;
import capstone.ScoringHelper;
import capstone.level.Level;
import capstone.level.LevelHelper;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.ArrayList;

/**
 * A bullet (or ingame: Shuriken) is a projectile that flies across the level until it hits either a player collider
 * (such as a wall) or it hits an enemy, killing it in the process. It can not change direction while flying.
 */
public class BulletGameObject extends DynamicGameObject {

    private DeltaTimeHelper deltaTimeHelper; //used for flight speed
    private Direction direction; //direction the bullet is going
    private boolean collided; //whether the bullet has collided
    private boolean hasMoved; //whether the bullet has moved from its recent location
    private boolean hasHitEnemy; //whether the bullet has hit an enemy
    private boolean hasAddedPlayerScore; //whether the player score was already modified
    private char alternativeRepresentation;
    private char originalRepresentation;

    /**
     * Directions used for the trajectory of the Bullet
     */
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    /**
     * Creates a new bullet with given coordinates in a given level with a trajecotry direction
     *
     * @param x         x coordinate of the spawned bullet
     * @param y         y coordinate of the spawned bullet
     * @param l         level in which the bullet resides
     * @param direction trajectory of the bullet
     */
    public BulletGameObject(int x, int y, Level l, Direction direction) {
        super(x, y, l);
        this.representation = '×';
        this.originalRepresentation = this.representation;
        this.alternativeRepresentation = '+';
        this.backgroundColor = Terminal.Color.DEFAULT;
        this.foregroundColor = Terminal.Color.YELLOW;
        this.entityName = "Shuriken (fired by pressing W,A,S or D)";
        this.direction = direction;
        this.deltaTimeHelper = new DeltaTimeHelper();
        this.savable = false;
    }

    /**
     * Checks whether the bullet has hit an enemy, and, if so, adds score to the player.
     * @param p a reference to the player object.
     */
    @Override
    public void modifyPlayer(PlayerGameObject p) {
        if (hasHitEnemy && !hasAddedPlayerScore) {
            p.modifyScore(ScoringHelper.getBaseValue() / 4);
            hasAddedPlayerScore = true;
        }
    }

    /**
     * Animates the representation of the shuriken ("rotating"), checks for collisions, removes itself from the level
     * if necessary, and removes hit enemies from the game.
     * @param deltaTime passed time since last call
     */
    @Override
    public void update(int deltaTime) {
        if (deltaTimeHelper.getDeltaTime() > 150) {
            if (this.representation == this.originalRepresentation) {
                this.representation = this.alternativeRepresentation;
            } else {
                this.representation = this.originalRepresentation;
            }
            deltaTimeHelper.reset();
            oldX = x;
            oldY = y;
            //collision check
            switch (this.direction) {
                case UP:
                    if (LevelHelper.checkWalkable(level, x, y - 1, true)) {
                        --y;
                    } else {
                        collided = true;
                    }
                    break;
                case DOWN:
                    if (LevelHelper.checkWalkable(level, x, y + 1, true)) {
                        ++y;
                    } else {
                        collided = true;
                    }
                    break;
                case LEFT:
                    if (LevelHelper.checkWalkable(level, x - 1, y, true)) {
                        --x;
                    } else {
                        collided = true;
                    }
                    break;
                case RIGHT:
                    if (LevelHelper.checkWalkable(level, x + 1, y, true)) {
                        ++x;
                    } else {
                        collided = true;
                    }
                    break;
            }
            needsUpdate = true;
            hasMoved = true;
            if (collided) {
                //remove it from the playing field
                x = -1;
                y = -1;
                this.needsUpdate = true;
            }
            ArrayList<DynamicGameObject> dynamicGameObjects = new ArrayList<>(level.getDynamicGameObjects());
            for (DynamicGameObject d : dynamicGameObjects) {
                if (d.isDestroyable() && d.getX() == x && d.getY() == y) {
                    //destroy the hit destroyable object
                    level.removeDynamicGameObject(d);
                    hasHitEnemy = true;
                    collided = true;
                }
            }
        }
    }

    public boolean isCollided() {
        return collided;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }
}
