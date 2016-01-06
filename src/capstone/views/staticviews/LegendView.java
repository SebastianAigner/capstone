package capstone.views.staticviews;

import capstone.gameobject.GameObject;
import capstone.gameobject.dynamicObjects.BulletGameObject;
import capstone.gameobject.dynamicObjects.MovingTrapGameObject;
import capstone.gameobject.dynamicObjects.PlayerGameObject;
import capstone.gameobject.staticObjects.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * By choice, the user will be presented with a legend view detailing all the information about the objects available
 * in the game.
 */
public class LegendView extends StaticView {
    private ArrayList<GameObject> explainedGameObjects;
    private String descriptionText;

    /**
     * Creates a new legend with the (currently available) Game Objects in the game.
     *
     * @param s      Lanterna screen to be drawn on
     * @param width  width of the view
     * @param height height of the view
     */
    public LegendView(Screen s, int width, int height) {
        super(s, width, height);
        explainedGameObjects = new ArrayList<>(Arrays.asList(
                new HealthGameObject(),
                new KeyGameObject(),
                new MovingTrapGameObject(0, 0, null),
                new PlayerGameObject(0, 0, 0, null, false),
                new EntranceGameObject(),
                new ExitGameObject(),
                new StaticTrapGameObject(),
                new WallGameObject(),
                new BulletGameObject(0, 0, null, BulletGameObject.Direction.UP)
        ));
        descriptionText = "This game has bonus features that are above the requirements stated in the official document." +
                " Besides being able to load levels by name (apart from the regular savegame feature), the game" +
                " intelligently pauses when navigating menus. It shows a score that has a dynamically adjusted base" +
                " value and is automatically decreased the more time passes. There are health kits available for player" +
                " use throughout levels that implement it (such as level_demo.properties). Another included level," +
                " level_bench.properties, shows how the game can handle around 6400 individual dynamic and animated" +
                " moving traps at the same time. The player can throw shurikens by pressing the WASD keys.";
    }


    /**
     * Draws the static legend to screen (only if an update is necessary). Also intelligently wraps the description text
     * according to window width.
     *
     * @param deltatime
     * @return
     */
    @Override
    public boolean update(int deltatime) {
        if (!hasDrawnStatics) {
            screen.clear();
            screen.putString(0, 0, "The Legend.", Terminal.Color.WHITE, Terminal.Color.DEFAULT);
            int lastMenuIndex = 0;
            for (int i = 0; i < explainedGameObjects.size(); ++i) {
                GameObject explainedGameObject = explainedGameObjects.get(i);
                screen.putString(1, i + 2, explainedGameObject.toString(), explainedGameObject.getForegroundColor(), explainedGameObject.getBackgroundColor());
                screen.putString(4, i + 2, explainedGameObject.getName(), Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
                lastMenuIndex = i + 2;
            }
            ArrayList<String> lines = new ArrayList<>();
            String currentLineText = "";
            boolean printed = false;
            //Wrap the description text according to the window width.
            for (String word : descriptionText.split(" ")) {
                if (currentLineText.length() + word.length() < width - 3) {
                    currentLineText += word + " ";
                } else {
                    lines.add(currentLineText);
                    currentLineText = word + " ";
                }
            }
            lines.add(currentLineText);
            for (int i = 0; i < lines.size(); ++i) {
                screen.putString(2, lastMenuIndex + 4 + i, lines.get(i), Terminal.Color.DEFAULT, Terminal.Color.DEFAULT);
            }
            hasDrawnStatics = true;
        }
        return true;
    }

}
