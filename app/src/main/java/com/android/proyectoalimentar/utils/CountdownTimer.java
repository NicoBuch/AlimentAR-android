package com.android.proyectoalimentar.utils;

import android.os.Handler;

/**
 * Helper class that keeps track of the time in which the next event becomes available and sends
 * updates every second to its listener.
 * Note that this is only for local usage and updates and it can be modified my changing the clock
 * on the phone. That would only be a UI problem since this information will be persisted in API and
 * the UI will fix itself after a maximum of {@code mInitialValue}.
 */
public class CountdownTimer {

    private static final int SECOND_IN_MILLIS = 1000;

    public interface TimerTickListener {

        void onTick(long timeLeft);

        void onTimerFinished();
    }

    // Vars to help get a tick every second.
    private final Handler mTimerHandler;
    private final Runnable mOnTimerFinishedRunnable;
    private long mLastTickTime;

    // Listener to notify with updates.
    private final TimerTickListener mTimerTickListener;

    // Time left until the next event becomes available.
    private long mTimeLeft;

    // Vars
    private long mInitialValue;

    public CountdownTimer(TimerTickListener timerTickListener, long initialValue) {
        mInitialValue = initialValue;
        mTimerTickListener = timerTickListener;
        mTimerHandler = new Handler();
        mOnTimerFinishedRunnable = this::onOneSecondPassed;

        startTimer(mInitialValue);
    }

    public void restartTimer() {
        mTimerHandler.removeCallbacks(mOnTimerFinishedRunnable);
        startTimer(mInitialValue);
    }

    /**
     * Sets the time left for next events.
     */
    public void setTimeLeft(long timeLeft) {
        mTimeLeft = timeLeft;
    }

    private void startTimer(long timeLeft) {
        mTimeLeft = timeLeft;
        mLastTickTime = System.currentTimeMillis();
        mTimerTickListener.onTick(mTimeLeft);
        mTimerHandler.postDelayed(mOnTimerFinishedRunnable, SECOND_IN_MILLIS);
    }

    /**
     * Called every second when the timer is active.
     */
    private void onOneSecondPassed() {
        refreshSecondPassed();

        // Notify the listener that a second has passed.
        mTimerTickListener.onTick(mTimeLeft);

        long currentTime = System.currentTimeMillis();
        // If the timer hasn't finished, reschedule the timer so that it keeps notifying the
        // listener. Otherwise, notify the listener the timer has finished.
        if (mTimeLeft > 0) {
            // delayTime is the time over one second that has passed since the last run. For
            // example, if 1.1 seconds passed, delayTime will be 100 (0.1 seconds). This is
            // done because the handler isn't 100% accurate and the total time between events
            // should't change because of it.
            long delayTime = currentTime - mLastTickTime - SECOND_IN_MILLIS;
            // Update |currentTime| so the next tick calculates the delay time correctly.
            currentTime -= delayTime;
            mTimerHandler.postDelayed(
                    mOnTimerFinishedRunnable, SECOND_IN_MILLIS - delayTime);
        } else {
            mTimerTickListener.onTimerFinished();
        }
        mLastTickTime = currentTime;
    }

    /**
     * Reads from SharedPreferences the time left until the next event becomes available or the
     * default value if it can't be found.
     */
    private long getTimeOfNextEvent() {
        return System.currentTimeMillis() + mInitialValue;
    }

    /**
     * Updates member variables to reflect the fact that a second has passed.
     */
    private void refreshSecondPassed() {
        mTimeLeft -= SECOND_IN_MILLIS;
    }

    public void cancel() {
        mTimerHandler.removeCallbacks(mOnTimerFinishedRunnable);
    }

}

