package capstone.gameobject.staticObjects;

import capstone.gameobject.dynamicObjects.PlayerGameObject;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * The exit game object marks the end of the level. The player has won once he reached the end with a key in his
 * inventory.
 */
public class ExitGameObject extends StaticGameObject {
    /**
     * Creates a new exit game object. An exit is player walkable, but not computer walkable (to prevent objective
     * camping).
     */

    public ExitGameObject() {
        this.foregroundColor = Terminal.Color.GREEN;
        this.backgroundColor = Terminal.Color.BLACK;
        this.representation = 'E';
        this.entityName = "Exit";
        this.computerWalkable = false;
        this.playerWalkable = true;
    }

    @Override
    public void modifyPlayer(PlayerGameObject p) {
        if (p.isHasKey()) {
            p.setHasReachedExit(true);
        }
    }

}
