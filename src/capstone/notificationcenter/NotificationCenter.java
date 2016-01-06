package capstone.notificationcenter;

import java.util.ArrayList;

/**
 * The notification center is a central hub within the game for status messages that concern a varying number of client
 * classes.
 */
public class NotificationCenter {
    static final ArrayList<NotificationMessage> notifications = new ArrayList<>();

    /**
     * Adds a new notification to the queued Notifications
     *
     * @param n Notification to be posted
     */
    public static void postNotification(NotificationMessage n) {
        System.out.println(n);
        notifications.add(n);
    }

    /**
     * Checks whether there is a notification with the given Tag.
     *
     * @param comparingNotification Notification value
     * @return whether a notification exists
     */
    public static boolean checkForNotification(NotificationMessage comparingNotification) {
        for (NotificationMessage notificationMessage : notifications) {
            if (comparingNotification == notificationMessage) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a notification with a given tag from the center.
     *
     * @param n notification to be removed.
     */
    public static void removeNotifictaion(NotificationMessage n) {
        notifications.remove(n);
    }
}
