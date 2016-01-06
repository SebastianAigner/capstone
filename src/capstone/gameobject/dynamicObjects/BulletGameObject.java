package capstone.gameobject.dynamicObjects;

import capstone.DeltaTimeHelper;
import capstone.ScoringHelper;
import capstone.level.Level;
import capstone.level.LevelHelper;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.ArrayList;

public class BulletGameObject extends DynamicGameObject {

    /**
     * Constructs a new Dynamic Game Object. Since the class is abstract, this is never directly called.
     * However, all subclasses should call it via super().
     *
     * @param x the x position in the level for the dynamic game object
     * @param y the y position in the level for the dynamic game object
     * @param l a reference to the current level (used e.g. for collision checking)
     */

    private DeltaTimeHelper deltaTimeHelper;
    private Direction direction;
    private boolean collided = false;
    private boolean hasMoved;
    private boolean hasHitEnemy;
    private boolean hasAddedPlayerScore;

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    public BulletGameObject(int x, int y, Level l, Direction direction) {
        super(x, y, l);
        this.representation = '×';
        this.backgroundColor = Terminal.Color.DEFAULT;
        this.foregroundColor = Terminal.Color.YELLOW;
        this.entityName = "Shuriken (fired by pressing W,A,S or D)";
        this.direction = direction;
        this.deltaTimeHelper = new DeltaTimeHelper();
        this.savable = false;
    }

    @Override
    public void modifyPlayer(PlayerGameObject p) {
        if (hasHitEnemy && !hasAddedPlayerScore) {
            p.modifyScore(ScoringHelper.getFormula() / 4);
            hasAddedPlayerScore = true;
        }
    }

    @Override
    public void update(int deltaTime) {
        if (deltaTimeHelper.getDeltaTime() > 150) {
            if (this.representation == '×') {
                this.representation = '+';
            } else {
                this.representation = '×';
            }
            deltaTimeHelper.reset();
            oldX = x;
            oldY = y;
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
            if (this.representation == ' ') {
                level.removeDynamicGameObject(this);
            }
            if (collided) {
                x = -1;
                y = -1;
                this.representation = ' ';
                this.needsUpdate = true;
            }
            ArrayList<DynamicGameObject> dynamicGameObjects = new ArrayList<>(level.getDynamicGameObjects());
            for (DynamicGameObject d : dynamicGameObjects) {
                if (d.isDestroyable() && d.getX() == x && d.getY() == y) {
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
