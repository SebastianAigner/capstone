package capstone.level;

import capstone.ScoringHelper;
import capstone.gameobject.dynamicObjects.DynamicGameObject;
import capstone.gameobject.dynamicObjects.MovingTrapGameObject;
import capstone.gameobject.dynamicObjects.PlayerGameObject;
import capstone.gameobject.staticObjects.*;
import capstone.notificationcenter.NotificationCenter;
import capstone.notificationcenter.NotificationMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
/**
 * The Level IO class provides static methods for saving and reading levels in the format that is outlined by the
 * capstone documentation.
 */
public class LevelInputOutput {

    /**
     * Reads and prepares a level for usage within the game
     *
     * @param filename file to be read relative to source path
     * @return a level object with all the information from the properties file.
     * @throws IOException
     */
    public static Level readLevel(String filename) throws IOException {
        Properties prop = new Properties();
        FileInputStream fileInputStream = new FileInputStream(filename);
        prop.load(fileInputStream);
        int width = Integer.parseInt(prop.getProperty("Width"));
        int height = Integer.parseInt(prop.getProperty("Height"));
        ScoringHelper.setHeight(height);
        ScoringHelper.setWidth(width);
        Level level = new Level(width, height);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int field = Integer.parseInt(prop.getProperty(x + "," + y, "-1"));
                if (field == -1) {
                    continue;
                }
                switch (field) {
                    case 0:
                        level.addStaticGameObject(new WallGameObject(), x, y);
                        break;
                    case 1:
                        level.addStaticGameObject(new EntranceGameObject(), x, y);
                        if (prop.getProperty("playerX") == null) {
                            // it's not a player save, hence we generate a new object
                            level.setPlayer(new PlayerGameObject(x, y, 5, level, false));
                            //todo refactor life count in external config file maybe?
                        }
                        break;
                    case 2:
                        level.addStaticGameObject(new ExitGameObject(), x, y);
                        break;
                    case 3:
                        level.addStaticGameObject(new StaticTrapGameObject(), x, y);
                        break;
                    case 4:
                        level.addDynamicGameobject(new MovingTrapGameObject(x, y, level));
                        break;
                    case 5:
                        level.addStaticGameObject(new KeyGameObject(), x, y);
                        break;
                    case 6:
                        level.addStaticGameObject(new HealthGameObject(), x, y);
                    default:
                }
            }
        }
        if (prop.getProperty("playerX") != null) {
            //for simplicity's sake we assume that once playerX exists, a full set of player info exists.
            int playerX = Integer.parseInt(prop.getProperty("playerX"));
            int playerY = Integer.parseInt(prop.getProperty("playerY"));
            int playerLives = Integer.parseInt(prop.getProperty("playerLives"));
            int playerScore = Integer.parseInt(prop.getProperty("playerScore"));
            boolean playerHasKey = Boolean.parseBoolean(prop.getProperty("playerHaskey"));
            level.setPlayer(new PlayerGameObject(playerX, playerY, playerLives, level, playerHasKey));
            NotificationCenter.postNotification(NotificationMessage.SAVE_LOAD_SUCCESS);
        } else {
            NotificationCenter.postNotification(NotificationMessage.LEVEL_LOAD_SUCCESS);
        }
        level.setLevelname(filename);
        fileInputStream.close();
        return level;
    }

    /**
     * Saves a level to a file with all its data in accordance to the specifications given by the capstone project.
     * It also stores the player information such as location, whether he posesses a key, how many lives he has left,
     * and what his score is. All of the information encoded in this way can in return be read by this game again.
     *
     * @param l        Level object to be saved
     * @param filename Filename for the save file
     * @throws IOException
     */
    public static void writeLevel(Level l, String filename) throws IOException {
        ArrayList<DynamicGameObject> dynamicGameObjects = l.getDynamicGameObjects();
        StaticGameObject[][] staticGameObjects = l.getStaticGameObjects();
        PlayerGameObject playerGameObject = l.getPlayer();
        Properties prop = new Properties();
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        prop.setProperty("Height", Integer.toString(staticGameObjects.length));
        prop.setProperty("Width", Integer.toString(staticGameObjects[0].length));

        for (int x = 0; x < staticGameObjects.length; ++x) {
            for (int y = 0; y < staticGameObjects[0].length; ++y) {
                //we assume they are always rectangles.
                StaticGameObject s = staticGameObjects[x][y];
                String representation = SaveFileHelper.getSavegameCharacterRepresentation(s);
                if (representation != null) {
                    prop.setProperty(x + "," + y, representation);
                }
            }
        }
        for (DynamicGameObject d : dynamicGameObjects) {
            if (d.isSavable()) {
                prop.setProperty(d.getX() + "," + d.getY(), SaveFileHelper.getSavegameCharacterRepresentation(d));
            }
        }
        prop.setProperty("playerX", Integer.toString(playerGameObject.getX()));
        prop.setProperty("playerY", Integer.toString(playerGameObject.getY()));
        prop.setProperty("playerLives", Integer.toString(playerGameObject.getLives()));
        prop.setProperty("playerScore", Integer.toString(playerGameObject.getScore()));
        prop.setProperty("playerHaskey", Boolean.toString(playerGameObject.isHasKey()));
        prop.store(fileOutputStream, null);
        NotificationCenter.postNotification(NotificationMessage.SAVE_SAVE_SUCCESS);
        fileOutputStream.close();
    }

    public static ArrayList<String> getAvailableLevels() {
        File currentDirectory = new File(".");
        ArrayList<String> names = new ArrayList<>();
        File[] filesList = currentDirectory.listFiles();
        for (File f : filesList) {
            if (f.isFile() && f.getName().contains(".properties")) {
                names.add(f.getName());
            }
        }
        return names;
    }
}
