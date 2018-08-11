package com.team2502.ezauton.test.simulator;

import com.team2502.ezauton.localization.estimators.TankRobotEncoderEncoderEstimator;
import com.team2502.ezauton.utils.SimulatedStopwatch;
import org.junit.Test;

public class SimulatorTest
{

    @Test
    public void testStraight()
    {
        SimulatedStopwatch stopwatch = new SimulatedStopwatch(SimulatedTankRobot.NORM_DT);
        SimulatedTankRobot robot = new SimulatedTankRobot(1, stopwatch, 14, 0.3, 16);
        TankRobotEncoderEncoderEstimator encoderRotationEstimator = new TankRobotEncoderEncoderEstimator(robot.getLeftDistanceSensor(), robot.getRightDistanceSensor(), robot);
        encoderRotationEstimator.reset();
        for(int i = 0; i < 1000; i++)
        {
            robot.run(1, 1);
            encoderRotationEstimator.update();
            stopwatch.progress();
        }
        System.out.println("encoderRotationEstimator = " + encoderRotationEstimator.estimateLocation());
    }
}