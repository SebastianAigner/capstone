package capstone;

import static java.lang.Math.toIntExact;

/**
 * DeltaTimeHelper is a helper class that provides a simple object that is capable of doing basic timekeeping.
 * It can be used to set up timers, refresh rates, and in general keep track of how much time has passed since
 * creation and / or last reset of the helper object.
 */
public class DeltaTimeHelper {
    private int millis;

    /**
     * Creates a new DeltaTimeHelper with the current system time.
     */
    public DeltaTimeHelper() {
        millis = toIntExact(System.currentTimeMillis() % 100000000);
    }

    /**
     * Resets the time used to determine the time delta.
     */
    public void reset() {
        millis = toIntExact(System.currentTimeMillis() % 100000000);
    }

    /**
     * Gets the time delta since the last reset.
     *
     * @return time delta
     */
    public int getDeltaTime() {
        int time = toIntExact(System.currentTimeMillis() % 100000000) - millis;
        return time;
    }

    /**
     * Gives a String representation of the current deltatime.
     *
     * @return current delta time passed as String
     */
    @Override
    public String toString() {
        return Integer.toString(getDeltaTime());
    }
}
