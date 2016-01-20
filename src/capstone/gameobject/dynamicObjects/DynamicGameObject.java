package capstone.gameobject.dynamicObjects;

import capstone.gameobject.GameObject;
import capstone.level.Level;

/**
 * A dynamic game object, can, other than a static game object, move about a level, as it holds its own x and y coordinates
 * and is usually not confined to the space of a 2D array. It can interact with the player.
 */
public abstract class DynamicGameObject extends GameObject {
    int x;
    int y;
    int oldX;
    int oldY;
    boolean needsUpdate = true;
    boolean destroyable;
    Level level;
    protected boolean savable;

    /**
     * Constructs a new Dynamic Game Object. Since the class is abstract, this is never directly called.
     * However, all subclasses should call it via super().
     *
     * @param x the x position in the level for the dynamic game object
     * @param y the y position in the level for the dynamic game object
     * @param l a reference to the current level (used e.g. for collision checking)
     */
    DynamicGameObject(int x, int y, Level l) {
        this.x = x;
        this.y = y;
        this.level = l;
        this.destroyable = false;
    }

    /**
     * Gets the x coordinate of the dynamic game object
     *
     * @return x coordinate of the dynamic game object
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the dynamic game object
     *
     * @return y coordinate of the dynamic game object
     */
    public int getY() {
        return y;
    }

    /**
     * Whether an update of the player representation is required.
     *
     * @return update is required
     */
    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    /**
     * Set whether an update is needed or not.
     *
     * @param needsUpdate update needed
     */
    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    /**
     * Returns the pre-movement x coordinate
     *
     * @return x coordinate before movement
     */
    public int getOldX() {
        return oldX;
    }

    /**
     * Returns the pre-movement y coordinate
     *
     * @return y coordinate before movement
     */
    public int getOldY() {
        return oldY;
    }

    /**
     * Modifies the player object with the given traits of the dynamic game object.
     * In the case of dynamic game objects, it is the duty of the game object implementation to check whether locations
     * of player and object overlap and an effect should be applied.
     *
     * @param p a reference to the player object.
     */
    @Override
    public abstract void modifyPlayer(PlayerGameObject p);

    /**
     * Updates a dynamic game object according to the time delta that has passed
     *
     * @param deltaTime passed time since last call
     */
    public abstract void update(int deltaTime);

    /**
     * Whether an object can / should be saved within a save file.
     *
     * @return the object should be saved
     */
    public boolean isDestroyable() {
        return this.destroyable;
    }

    public boolean isSavable() {
        return savable;
    }
}
