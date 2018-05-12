package com.team2502.ezauton.localization.sensors;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Encoders {

    int CTRE_MAG_ENCODER = 4096;

    public static IEncoder fromTalon(TalonSRX talonSRX, int unitsPerRev)
    {
        return new IEncoder() {
            @Override
            public double getPosition() {
                return talonSRX.getSelectedSensorPosition(0)/unitsPerRev;
            }

            @Override
            public double getVelocity() {
                return talonSRX.getSelectedSensorVelocity(0)/unitsPerRev*10F;
            }
        };
    }
}
