package com.team2502.ezauton.localization.sensors;

public class EncoderWheel {

    private final IEncoder encoder;
    private final double wheelDiameter;
    private double multiplier;

    /**
     * @param encoder The encoder for measuring revolutions
     * @param wheelDiameter The diameter of the wheel with the encoder (recommended in ft)
     */
    public EncoderWheel(IEncoder encoder, double wheelDiameter)
    {
        this.encoder = encoder;
        this.wheelDiameter = wheelDiameter;
    }

    public IEncoder getEncoder() {
        return encoder;
    }

    public double getWheelDiameter() {
        return wheelDiameter;
    }

    public double getMultiplier() {
        return multiplier;
    }

    /**
     *
     * @return position (probably in ft)
     */
    public double getPosition()
    {
        return encoder.getPosition() * Math.PI * wheelDiameter * getMultiplier();
    }

    /**
     *
     * @return velocity (probably in ft/s)
     */
    public double getVelocity()
    {
        return encoder.getPosition() * Math.PI * wheelDiameter * getMultiplier() / 60D; // because minute to second
    }

    /**
     *
     * @param multiplier If there are additional gear ratios to consider, this is the multiplier
     *                          (wheel rev / encoder rev)
     */
    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}