package com.team2502.ezauton.actuators.implementations;

import com.team2502.ezauton.actuators.IVelocityMotor;
import com.team2502.ezauton.localization.sensors.ITranslationalDistanceSensor;
import com.team2502.ezauton.utils.IClock;
import com.team2502.ezauton.utils.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Describes a simulated motor with an encoder. The motor has infinite acceleration
 */
public class BaseSimulatedMotor implements IVelocityMotor, ITranslationalDistanceSensor
{
    private final Stopwatch stopwatch;

    /**
     * Assumed to be in dist/second
     */
    protected double velocity = 0;
    private IVelocityMotor subscribed = null;
    private double position = 0;

    /**
     * Create a basic simulated motor
     *
     * @param clock The clock to keep track of time with
     */
    public BaseSimulatedMotor(IClock clock)
    {
        this.stopwatch = new Stopwatch(clock);
    }

    /**
     * @return The motor to which the velocity is being applied
     */
    public IVelocityMotor getSubscribed()
    {
        return subscribed;
    }

    /**
     * Change the motor to which the velocity will be applied
     *
     * @param subscribed The new motor instance
     */
    public void setSubscribed(IVelocityMotor subscribed)
    {
        this.subscribed = subscribed;
    }


    @Override
    public void runVelocity(double targetVelocity)
    {
        stopwatch.resetIfNotInit();
        if(subscribed != null)
        {
            subscribed.runVelocity(targetVelocity);
        }
        position += velocity * stopwatch.pop();
        this.velocity = targetVelocity;
    }

    @Override
    public double getPosition()
    {
        position += velocity * stopwatch.pop(TimeUnit.SECONDS); // Convert millis to seconds
        return position;
    }

    @Override
    public double getVelocity()
    {
        return velocity;
    }
}
