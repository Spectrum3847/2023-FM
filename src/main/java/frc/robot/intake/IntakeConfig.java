package frc.robot.intake;

public class IntakeConfig {

    public double lowerDiameter = 0.8; // TODO: config: change; not actually used though
    public double frontDiameter = 2.125;
    public double launcherDiamter = 3;

    public double lowerGearRatio = 12 / 15; // TODO: config: change; not actually used though
    public double frontGearRatio = 18 / 22;
    public double launcherGearRatio = 18 / 12;

    public double falconMaxSpeed = 6380; // RPM

    public double intake = 5000; // 2000
    public double slowIntake = 1500; // 600
    public double eject = -3000;
    public double drop = -300;

    public double intakeCone = 3000;

    public double currentLimit = 10;
    public double threshold = 20;

    public double velocityKp = 0.065;
    public double velocityKf = 0.0519;

    public IntakeConfig() {}
}
