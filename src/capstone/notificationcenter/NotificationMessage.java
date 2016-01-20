package capstone.notificationcenter;

/**
 * Possible Notifications that can be posted to the NotificationCenter
 *
 * @see NotificationCenter
 */
public enum NotificationMessage {
    SAVE_QUIT, // expresses the intent to save the current game and quit the game afterwards
    QUIT, // expresses the intent to quit the current game without saving
    SAVE_SAVE, // expresses the intent to save a savefile
    CONTINUE, // expresses the intent to remove menus from the view stack and continue playing the game
    SAVE_LOAD, // expresses the intent to load a save file
    LEVEL_LOAD_BY_NAME, // expresses the intent to load a level by name
    LEGEND, // expresses the intent to open the legend
    SAVE_LOAD_SUCCESS, // information that loading the save was successful.
    SAVE_SAVE_SUCCESS, // information that saving the file was successful.
    LEVEL_LOAD_SUCCESS // information that loading the level was successful.
}
