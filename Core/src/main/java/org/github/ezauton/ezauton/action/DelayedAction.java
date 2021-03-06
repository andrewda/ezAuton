package org.github.ezauton.ezauton.action;

import org.github.ezauton.ezauton.utils.IClock;

import java.util.concurrent.TimeUnit;

/**
 * Describes an action that waits a certain amount of time before running
 */
public class DelayedAction extends BaseAction
{

    private Runnable runnable;
    private long millis;

    public DelayedAction(long value, TimeUnit unit)
    {
        millis = unit.toMillis(value);
    }

    public DelayedAction(long value, TimeUnit unit, Runnable runnable)
    {
        this(value, unit);
        this.runnable = runnable;
    }

    /**
     * Called after delay is done
     */
    public void onTimeUp(IClock clock)
    {
        if(runnable != null)
        {
            runnable.run();
        }
    }

    /**
     * Do not override!
     *
     * @param clock
     */
    @Override
    public void run(IClock clock)
    {
        try
        {
            clock.sleep(millis, TimeUnit.MILLISECONDS);
        }
        catch(InterruptedException e)
        {
            return;
        }

        if(isStopped())
        {
            return;
        }
        onTimeUp(clock);
    }
}