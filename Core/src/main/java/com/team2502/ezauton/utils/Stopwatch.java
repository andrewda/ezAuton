package com.team2502.ezauton.utils;

import java.util.concurrent.TimeUnit;

/**
 * A handy stopwatch for recording time in seconds every time it is polled
 *
 * @deprecated Use {@link ICopyableStopwatch}
 */
public class Stopwatch
{

    private final IClock clock;
    long millis = -1;

    public Stopwatch(IClock clock)
    {
        this.clock = clock;
    }

    public void init()
    {
        millis = clock.getTime();

    }

    /**
     * Read and reset
     *
     * @return The value of the stopwatch
     */
    double pop()
    {
        double readVal = read();
        reset();
        return readVal;
    }

    /**
     * Read without resetting
     *
     * @return The value of the stopwatch
     */
    double read() {
        return clock.getTime() - millis;
    }

    /**
     * Reset without reading
     */
    void reset()
    {
        millis = clock.getTime();
    }

    /**
     * @return If this stopwatch is initialized
     */
    boolean isInit() {
        return millis != -1;
    }

    /**
     * @return If is not init
     */
    boolean resetIfNotInit()
    {
        if(isInit())
        {
            return false;
        }
        reset();
        return true;
    }

    /**
     * Locks current thread for the time specified
     *
     * @param amount
     * @param timeUnit
     *
     * @throws InterruptedException
     */
    void wait(int amount, TimeUnit timeUnit) throws InterruptedException
    {
        Thread.sleep(timeUnit.toMillis(amount));
    }
}