package capstone.gameobject.staticObjects;

import capstone.ScoringHelper;
import capstone.gameobject.dynamicObjects.PlayerGameObject;
import com.googlecode.lanterna.terminal.Terminal;

public class KeyGameObject extends StaticGameObject {
    private boolean taken = false;

    /**
     * Constructs a new key game object at the given coordinates in the given level.
     *
     */
    public KeyGameObject() {

        this.representation = 'k'; //it's a key.
        this.entityName = "Key for Doors";
        this.foregroundColor = Terminal.Color.YELLOW;
        this.backgroundColor = Terminal.Color.BLACK;
        this.computerWalkable = false;
        this.playerWalkable = true;
        this.pickup = true;
    }

    /**
     * Checks whether a player is within range (read: standing on the key position) and adds the key to the player's
     * inventory.
     *
     * @param p a reference to the player object.
     */
    @Override
    public void modifyPlayer(PlayerGameObject p) {
        if (!taken) {
            p.takeKey();
            p.modifyScore(ScoringHelper.getFormula() / 2);
            taken = true;
            this.representation = ' ';
            this.backgroundColor = Terminal.Color.DEFAULT;
        }
    }

    /**
     * Whether the object should be saved. A key will only be saved if it has not been picked up yet.
     * @return true when not taken
     */
}
