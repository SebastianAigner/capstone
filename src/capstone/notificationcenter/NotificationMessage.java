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
    LEVEL_LOAD_BY_NAME,
    MAIN_MENU,
    LEGEND,
    SAVE_LOAD_SUCCESS,
    SAVE_SAVE_SUCCESS,
    LEVEL_LOAD_SUCCESS
}
