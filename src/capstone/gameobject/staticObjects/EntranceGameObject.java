package capstone.gameobject.staticObjects;

import capstone.gameobject.dynamicObjects.PlayerGameObject;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * The entrance game object marks the beginning of the level. It serves as a spawn point in a new save.
 */
public class EntranceGameObject extends StaticGameObject {
    /**
     * Generates a new Entrance Game Object. An entrance is player walkable (as he spawns in the entrance), but
     * not computer walkable (to prevent immediate killing).
     */
    public EntranceGameObject() {
        this.foregroundColor = Terminal.Color.CYAN;
        this.backgroundColor = Terminal.Color.BLACK;
        this.representation = 'I';
        this.playerWalkable = true;
        this.computerWalkable = false;
        this.entityName = "Entrance";
    }

    @Override
    public void modifyPlayer(PlayerGameObject playerGameObject) {

    }

}
