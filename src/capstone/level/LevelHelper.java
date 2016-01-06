package capstone.level;

import capstone.gameobject.staticObjects.StaticGameObject;

public class LevelHelper {

    /**
     * Checks whether a given field is walkable by player or computer. This is a fast way of doing collision-detection.
     * It also distinguishes whether the surface is player walkable or computer walkable.
     *
     * @param l        Level in which the collision check is supposed to be done
     * @param x        x coordinate of the field to check
     * @param y        y coordinate of the field to check
     * @param isPlayer whether the surface should be computer or player walkable.
     * @return is a surface walkable
     */
    public static boolean checkWalkable(Level l, int x, int y, boolean isPlayer) {
        StaticGameObject[][] s = l.getStaticGameObjects();
        if (x < 0 || y < 0 || y >= s.length || x >= s[0].length) {
            return false;
        }
        StaticGameObject location = l.getStaticGameObjects()[x][y];
        if (location == null) {
            return true;
        }
        if (isPlayer) {
            return location.isPlayerWalkable();
        } else {
            return location.isComputerWalkable();
        }
    }
}
