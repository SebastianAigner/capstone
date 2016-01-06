package capstone.gameobject.staticObjects;

import capstone.gameobject.dynamicObjects.PlayerGameObject;
import com.googlecode.lanterna.terminal.Terminal;

public class HealthGameObject extends StaticGameObject {
    private boolean taken = false;

    /**
     * Creates a new health game object with the given representation
     */
    public HealthGameObject() {
        this.entityName = "Health Pickup";
        this.pickup = true;
        this.playerWalkable = true;
        this.computerWalkable = false;
        this.representation = 'H';
        this.foregroundColor = Terminal.Color.WHITE;
        this.backgroundColor = Terminal.Color.RED;
    }

    /**
     * Adds a life to the player character
     *
     * @param p a reference to the player object.
     */
    @Override
    public void modifyPlayer(PlayerGameObject p) {
        if (!taken) {
            p.modifyLives(1);
            taken = true;
        }
    }
}
