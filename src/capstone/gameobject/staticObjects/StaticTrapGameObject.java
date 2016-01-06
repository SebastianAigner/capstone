package capstone.gameobject.staticObjects;

import capstone.DeltaTimeHelper;
import capstone.ScoringHelper;
import capstone.gameobject.dynamicObjects.PlayerGameObject;
import com.googlecode.lanterna.terminal.Terminal;

public class StaticTrapGameObject extends StaticGameObject {

    private final DeltaTimeHelper d;

    /**
     * Creates a new static trap with the according terminal representation.
     * A static trap is player walkable, but not computer walkable (to prevent damage stacking).
     */
    public StaticTrapGameObject() {
        this.representation = '^';
        this.foregroundColor = Terminal.Color.GREEN;
        this.backgroundColor = Terminal.Color.BLACK;
        this.entityName = "Static Trap";
        this.playerWalkable = true;
        this.computerWalkable = false;
        d = new DeltaTimeHelper();
        d.reset();
    }

    /**
     * A static trap deducts a maximum of one life per second when the player stands on it.
     *
     * @param p Player object to be modified
     */
    @Override
    public void modifyPlayer(PlayerGameObject p) {
        //todo deduct score
        if (d.getDeltaTime() > 1000) {
            p.modifyLives(-1);
            p.modifyScore(-ScoringHelper.getFormula() / 5);
            d.reset();
        }
    }
}
