package capstone.gameobject;

import capstone.gameobject.dynamicObjects.PlayerGameObject;
import com.googlecode.lanterna.terminal.Terminal;

public abstract class GameObject {
    protected char representation; //Character representation that will be printed in the game and the legend
    protected Terminal.Color foregroundColor; //foreground color in game and legend
    protected Terminal.Color backgroundColor; //background color in game and legend
    protected String entityName;

    /**
     * Creates the new game object with default values for the terminal representation
     * In case a game object forgets to set its terminal representation, it will be provided with a bright color scheme
     * and a question mark to indicate to the developer that while functionality might already be working, the character
     * representation for the object is still missing.
     */
    public GameObject() {
        this.representation = '?';
        this.backgroundColor = Terminal.Color.CYAN;
        this.foregroundColor = Terminal.Color.RED;
    }

    /**
     * Gets the character representation of the game object
     *
     * @return character representation
     */
    public char getRepresentation() {
        return representation;
    }

    /**
     * Gets the foreground color for the game object
     *
     * @return foreground color
     */
    public Terminal.Color getForegroundColor() {
        return foregroundColor;
    }

    /**
     * gets the background color for the game object
     *
     * @return background color
     */
    public Terminal.Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Modifies the player based on whether he stands on them or is "within range" for dynamic game objects
     *
     * @param p Player object to be modified
     */
    public abstract void modifyPlayer(PlayerGameObject p);

    /**
     * Gets the human readable name of the game object.
     *
     * @return human readable name of the game object
     */
    public String getName() {
        return this.entityName;
    }

    /**
     * Gets the character representation as a string.
     *
     * @return character representation
     */
    @Override
    public String toString() {
        return Character.toString(representation);
    }

}
