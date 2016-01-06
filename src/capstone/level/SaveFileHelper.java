package capstone.level;

import capstone.gameobject.GameObject;
import capstone.gameobject.dynamicObjects.MovingTrapGameObject;
import capstone.gameobject.dynamicObjects.PlayerGameObject;
import capstone.gameobject.staticObjects.*;


public class SaveFileHelper {
    /**
     * Helper method used to determine the correct way to save objects in properties files.
     * Allows saving of all the original elements with their representations, plus the extra objects (healthkit).
     *
     * @param gameObject object that should be saved
     * @return game object representation according to the specifications in the original document.
     */
    public static String getSavegameCharacterRepresentation(GameObject gameObject) {
        if (gameObject == null) {
            return null;
        }
        if (gameObject instanceof WallGameObject) {
            return "0";
        }
        if (gameObject instanceof EntranceGameObject) {
            return "1";
        }
        if (gameObject instanceof ExitGameObject) {
            return "2";
        }
        if (gameObject instanceof StaticTrapGameObject) {
            return "3";
        }
        if (gameObject instanceof MovingTrapGameObject) {
            return "4";
        }
        if (gameObject instanceof KeyGameObject) {
            return "5";
        }
        if (gameObject instanceof HealthGameObject) {
            return "6";
        }
        if (gameObject instanceof PlayerGameObject) {
            return "-1";
        }
        System.out.println("Warning: " + gameObject.getName() + " cannot be transcribed to the save format.");
        return null;
    }
}
