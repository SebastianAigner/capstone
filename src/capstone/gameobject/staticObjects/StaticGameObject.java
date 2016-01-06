package capstone.gameobject.staticObjects;

import capstone.gameobject.GameObject;

public abstract class StaticGameObject extends GameObject {
    protected boolean playerWalkable;
    protected boolean computerWalkable;
    protected boolean pickup = false;

    public boolean isPlayerWalkable() {
        return playerWalkable;
    }

    public boolean isPickup() {
        return pickup;
    }

    public boolean isComputerWalkable() {
        return computerWalkable;
    }
}
