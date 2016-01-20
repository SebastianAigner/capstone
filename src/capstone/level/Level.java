package capstone.level;

import capstone.gameobject.GameObject;
import capstone.gameobject.dynamicObjects.DynamicGameObject;
import capstone.gameobject.dynamicObjects.PlayerGameObject;
import capstone.gameobject.staticObjects.StaticGameObject;
import com.googlecode.lanterna.input.Key;

import java.util.ArrayList;

/**
 * The level class represents all game objects and the attached logic that is required in order to play the game.
 * A few words about the memory architecture:
 *
 * Static Game Objects are held in a 2D array. They do not know their own coordinates, and they do not need to, since
 * they will never move. They also do not need to be updated periodically. Because they have fixed positions, they make
 * for great collision detection (the collision of a dynamic element with the surrounding static elements can be done
 * within fixed time, whilst if I would not split them into dynamic and static game objects, you would have to compare
 * every object to each other, which is very calculation intensive.
 *
 * Dynamic Game Object are held in an arraylist. They know their own coordinates, and they are naturally drawn on top of
 * any static game objects (e.g. a player on top of a spike or a key). Since they are updated regularly, I chose this
 * memory format because it ensures I only iterate over the necessary items.
 */
public class Level {
    private PlayerGameObject player;
    private StaticGameObject[][] staticGameObjects;
    private ArrayList<DynamicGameObject> dynamicGameObjects;
    private int levelWidth;
    private int levelHeight;
    private String levelName;

    /**
     * Creates an empty rectangular level with the correct size.
     *
     * @param width  Width of the level in gameobjects
     * @param height Height of the level in gameobjects
     */
    public Level(int width, int height) {
        //creates an empty level with
        //the correct size.
        dynamicGameObjects = new ArrayList<>();
        staticGameObjects = new StaticGameObject[width][height];
        this.levelWidth = width;
        this.levelHeight = height;
    }

    /**
     * Generates a player game object at a given position.
     *
     * @param x
     * @param y
     */
    public void generatePlayerAtPosition(int x, int y) {
        if (!(x >= 0 && x < levelWidth)) {
            x = 0;
        }
        if (!(y >= 0 && y < levelHeight)) {
            y = 0;
        }
        //at this point it is made sure that the player is within the boundaries of the level
        this.player = new PlayerGameObject(x, y, 5, this, false);
    }

    /**
     * Gets the player object for the current level
     *
     * @return Player object
     */
    public PlayerGameObject getPlayer() {
        return this.player;
    }

    /**
     * Adds a static game object to the scene.
     *
     * @param g GameObject to be added.
     * @param x x coordinate of the game object in the scene
     * @param y y coordinate of the game object in the scene
     */
    public void addStaticGameObject(StaticGameObject g, int x, int y) {
        staticGameObjects[x][y] = g;
    }

    /**
     * Adds a dynamic game object to the scene
     *
     * @param d dynamic game object to be added.
     */
    public void addDynamicGameobject(DynamicGameObject d) {
        dynamicGameObjects.add(d);
        if (d instanceof PlayerGameObject) {
            //if the dynamic game object is a player, assign it to the player variable instead.
            this.player = (PlayerGameObject) d;
        }
    }

    /**
     * Processes the keystroke from the player. Since for this level, the only thing controlled by the player is
     * the character, it will forward the keystrokes to the underlying player object.
     *
     * @param key
     */
    public void processKeystroke(Key key) {
        player.processKeystroke(key);
    }

    /**
     * Gets static objects for a given frame.
     *
     * @param xOffset x offset of the rectangle
     * @param yOffset y offset of the rectangle
     * @param width   width of the rectangle
     * @param height  height of the rectangle
     * @return the gameobjects contained in the given square
     */
    public GameObject[][] requestStaticObjectsForFrame(int xOffset, int yOffset, int width, int height) {
        //making sure we don't go out of bounds
        if (width > levelWidth) {
            width = levelWidth;
        }
        if (height > levelHeight) {
            height = levelHeight;
        }
        if (xOffset < 0) {
            xOffset = 0;
        }
        if (yOffset < 0) {
            yOffset = 0;
        }

        if (width < 0 || height < 0) {
            throw new ArrayIndexOutOfBoundsException("A negative frame was requested");
        }
        GameObject[][] frame = new GameObject[width][height];
        //Creates a new 2D array with the correct size and fills it with the elements. accordingly.
        for (int x = xOffset; x < xOffset + width; ++x) {
                try {
                    System.arraycopy(staticGameObjects[x], yOffset, frame[x - xOffset], 0, yOffset + height - yOffset);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("An illegal frame was requested.");
                }
        }
        return frame;
    }

    /**
     * gets the filename of the current level
     *
     * @return filename of the current level
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Sets the level name (e.g. level_big_dense.properties)
     *
     * @param levelName name of the level
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    /**
     * Updates all the dynamic game objects in the level
     *
     * @param deltatime time passed since last call
     */

    public void updateDynamicObjects(int deltatime) {
        ArrayList<DynamicGameObject> workingCopy = new ArrayList<>(dynamicGameObjects);
        for (DynamicGameObject d : workingCopy) {
            d.update(deltatime);
        }
    }

    /**
     * Removes a dynamic game object from the arraylist.
     * @param dynamicGameObject
     */
    public void removeDynamicGameObject(DynamicGameObject dynamicGameObject) {
        dynamicGameObjects.remove(dynamicGameObject);
    }

    /**
     * Remove a static game object from the 2D array.
     * @param x x coordinate of the static game object to be removed
     * @param y y coordinate of the static game object to be removed
     */
    public void removeStaticGameObject(int x, int y) {
        staticGameObjects[x][y] = null;
    }

    /**
     * Whether the player has reached the exit (and has a key)
     *
     * @return player has reached the exit / finished the level
     */
    public boolean hasPlayerReachedExit() {
        return player.isHasReachedExit();
    }

    /**
     * Allows the static object at the current player position to interact with the player.
     * Allows the dynamic game objects to interact with the player (they, in return, check the player position for
     * themselves. This is done to allow future additions such as field effects)
     */
    public void doPlayerInteraction() {
        //static game object the player might be standing on
        StaticGameObject s = staticGameObjects[player.getX()][player.getY()];
        if (s != null) {
            s.modifyPlayer(player);
        }
        for (DynamicGameObject d : this.getDynamicGameObjects()) {
            d.modifyPlayer(player);
        }
    }

    /**
     * Updates all entities in the scene.
     *
     * @param deltaTime time passed since last call
     */
    public void updateEntities(int deltaTime) {
        doPlayerInteraction();
        updateDynamicObjects(deltaTime);
    }

    /**
     * Set the player object of the current scene
     *
     * @param player new player object
     */
    public void setPlayer(PlayerGameObject player) {
        this.player = player;
        dynamicGameObjects.add(player);
    }

    /**
     * Get all the static game objects in the scene
     *
     * @return all static game objects in the scene as a 2D array
     */
    public StaticGameObject[][] getStaticGameObjects() {
        return staticGameObjects;
    }

    /**
     * Get the level width
     *
     * @return width of the level
     */
    public int getLevelWidth() {
        return levelWidth;
    }

    /**
     * Get the level height
     *
     * @return height of the level
     */
    public int getLevelHeight() {
        return levelHeight;
    }

    /**
     * Get all dynamic elements in the level
     *
     * @return all dynamic elements
     */
    public ArrayList<DynamicGameObject> getDynamicGameObjects() {
        return dynamicGameObjects;
    }
}
