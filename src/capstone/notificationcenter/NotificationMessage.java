package capstone.notificationcenter;

/**
 * Possible Notifications that can be posted to the NotificationCenter
 *
 * @see NotificationCenter
 */
public enum NotificationMessage {
    SAVE_QUIT, // Implemented in MenuView
    QUIT, // Implemented in Main
    SAVE_SAVE, // Implemented in MenuView
    CONTINUE, //Implemented in MenuView
    SAVE_LOAD, //Implemented in MenuView
    LEVEL_LOAD_BY_NAME, //Implemented in MenuView -> Spawns LevelLoadView
    LEGEND, //Implemented in MenuView -> Spawns LegendView
    SAVE_LOAD_SUCCESS,
    SAVE_SAVE_SUCCESS, //Handled by MenuView -> Shows info
    LEVEL_LOAD_SUCCESS
}
