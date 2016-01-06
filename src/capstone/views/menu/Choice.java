package capstone.views.menu;

import capstone.notificationcenter.NotificationCenter;
import capstone.notificationcenter.NotificationMessage;

/**
 * Choices are very simple data containers that contain a NotificationMessage and a String representation.
 * Upon triggering, they will post their Notification Message to the Notification Center. This is used as a
 * "central hub" for messages that can spread widely inside the game system, such as a QUIT or SAVE.
 *
 * @see NotificationMessage
 */
class Choice {
    private String textLabel;
    private NotificationMessage trigger;

    /**
     * Creates a new choice with given text label and notification message trigger
     *
     * @param textLabel label to be displayed for this choice
     * @param trigger   trigger to be dispatched upon choice selection
     */
    public Choice(String textLabel, NotificationMessage trigger) {
        this.textLabel = textLabel;
        this.trigger = trigger;
    }

    /**
     * Dispatches a new Notification to the central Notification Center containing the information of the Choice
     * selected.
     */
    public void trigger() {
        NotificationCenter.postNotification(trigger);
    }

    /**
     * Gets the trigger that will be executed upon calling the trigger method.
     *
     * @return
     */
    public NotificationMessage getTrigger() {
        return trigger;
    }

    /**
     * Sets the text label to be displayed for this choice.
     *
     * @param textLabel new text label
     */
    public void setTextLabel(String textLabel) {
        this.textLabel = textLabel;
    }

    /**
     * Sets the trigger that will be executed when calling the trigger method.
     *
     * @param trigger
     */
    public void setTrigger(NotificationMessage trigger) {
        this.trigger = trigger;
    }

    /**
     * Gets the String representation of the choice
     *
     * @return text label of the choice
     */
    @Override
    public String toString() {
        return this.textLabel;
    }
}
