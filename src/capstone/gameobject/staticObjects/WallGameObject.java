package capstone.gameobject.staticObjects;

import capstone.gameobject.dynamicObjects.PlayerGameObject;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * A wall game object provides the boundaries of the level as well as a non-lethal obstacle for the player and dynamic
 * enemeies.
 */
public class WallGameObject extends StaticGameObject {

    /**
     * Creates a new Wall game object with the given screen representation. A wall game object is never player-walkable
     * and never computer walkable.
     */
    public WallGameObject() {
        this.representation = 'X';
        this.foregroundColor = Terminal.Color.WHITE;
        this.backgroundColor = Terminal.Color.WHITE;
        this.computerWalkable = false;
        this.playerWalkable = false;
        this.entityName = "Wall";
    }

    @Override
    public void modifyPlayer(PlayerGameObject playerGameObject) {

    }
}
