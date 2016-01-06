package capstone;

import capstone.views.View;
import com.googlecode.lanterna.input.Key;

import java.util.Stack;

/**
 * The ViewManager is the class responsible for managing multiple views on top of each other (e.g. a Pause Menu on top
 * of a level screen). It provides a stack and certain management- and update methods that allow for multiple views
 * to be managed simultaneously. It also takes care of passing on keyboard input to the correct view.
 */
public class ViewManager {
    private final Stack<View> stack = new Stack<>();

    public ViewManager() {
    }

    /**
     * Pushes a new view on top of the stack.
     *
     * @param view View to be pushed on top of the stack
     */
    public void push(View view) {
        stack.push(view);
    }

    /**
     * Returns and removes the topmost view on the stack.
     *
     * @return popped View
     */
    public View pop() {
        return stack.pop();
    }

    /**
     * Hands off keystroke processing to the topmost view on the stack.
     *
     * @param keystroke Keystroke to be processed.
     */
    public void processKeystroke(Key keystroke) {
        stack.peek().processKeystroke(keystroke);
    }

    /**
     * Allows all views to do so-called management update in which the views can clean up after themselves, check
     * for pending actions, or e.g. request to removed from the view stack.
     * If the top view has requested removal from the view stack, it will be removed.
     * If the top view has requested for a new view to be pushed upon the stack, the new view will be added.
     * The now topmost view will be updated.
     * Keep in mind that this method does not clear the screen even when pushing and popping views from the stack to
     * ensure the possibility of expansion lateron (e.g. smaller windows on top of backgrounds.)
     *
     * @param deltatime
     * @return
     */
    public boolean update(int deltatime) {

        //Allow every view to do management update regardless of their position in the stack.
        for (View v : stack) {
            v.managementUpdate();
        }

        View currentView = stack.peek();
        if (stack.peek().requestsViewStackRemoval()) {
            System.out.println(stack.peek() + " has requested view stack removal");
            //the topmost view will be removed
            stack.pop();
        }
        View newView = currentView.requestsViewStackAddition();
        if (newView != null) {
            System.out.println(stack.peek() + " has requested to push " + newView + " onto view stack");
            stack.peek().resetViewStackAddition();
            //a view has requested for a new view to be pushed onto the stack
            stack.push(newView);
        }
        //the topmost view will be able to update / draw to the screen
        return stack.peek().update(deltatime);
    }

    /**
     * Hands off window resizing functionality to the topmost view in the stack.
     */
    public void processResize() {
        //the topmost object (responsible for drawing) will handle the resize event
        stack.peek().processResize();
    }

}
