package capstone.gameobject.staticObjects;

import capstone.gameobject.GameObject;

public abstract class StaticGameObject extends GameObject {
    protected boolean playerWalkable;
    protected boolean computerWalkable;
    protected boolean pickup = false;

    /**
     * Whether a game object is a pickup (and should thus be removed from memory once the player has stepped on it)
     *
     * @return object is a pickup
     */
    public boolean isPickup() {
        return pickup;
    }

    /**
     * Whether AI should collide or be allowed to pass onto this object.
     * @return Whether an object is walkable by the AI.
     */
    public boolean isComputerWalkable() {
        return computerWalkable;
    }

    /**
     * Whether the Player should collide or be allowed to pass onto this object
     *
     * @return Whether an object is walkable by the player.
     */
    public boolean isPlayerWalkable() {
        return playerWalkable;
    }
}
