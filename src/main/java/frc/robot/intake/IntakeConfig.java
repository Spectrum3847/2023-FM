package frc.robot.intake;

public class IntakeConfig {

    public double lowerDiameter = 0.8; // TODO: config: change
    public double frontDiameter = 2.125;
    public double launcherDiamter = 3;

    public double lowerGearRatio = 12 / 15; // TODO: config: change
    public double frontGearRatio = 18 / 22;
    public double launcherGearRatio = 18 / 12;

    public double falconMaxSpeed = 6380; // RPM

    public double intake = 3000;
    public double slowIntake = 600;
    public double eject = -3000;
    public double drop = -300;

    public double intakeCone = 3000;

    public double currentLimit = 40; // TODO: review
    public double threshold = 40;

    public double velocityKp = 0.065;
    public double velocityKf = 0.0519;

    public IntakeConfig() {}
}
