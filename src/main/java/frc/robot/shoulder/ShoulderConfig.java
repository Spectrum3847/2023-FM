package frc.robot.shoulder;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.SpectrumLib.subsystems.angleMech.AngleMechConfig;

public class ShoulderConfig extends AngleMechConfig {

    public boolean kInverted = true;

    public final int shoulderMaxFalcon = 44000; // TODO: change

    // Positions set as percentage of shoulder

    public final int coneIntake = 94;
    public final int coneStandingIntake = 46;
    public final int coneShelf = 0;

    public final int airConeIntake = 50;
    public final int airCubeIntake = 50;
    public final int topConeIntake = 90;
    public final int topCubeIntake = 90;
    public final int coneFloorScore = 15;
    public final int cubeFloorScore = 30;

    public final int coneHybrid = 30;
    public final int coneMid = 62; // converted from 1800 angle //24700
    public final int coneTop = 100; // converted from 54900 angle

    public final int cubeIntake = 70; // 73;
    public final int cubeMid = 0;
    public final int cubeTop = 0;
    public final int cubeHybrid = 0;

    public final int safePositionForElevator = 55;

    public final double zeroSpeed = -0.2;

    // Physical Constants
    public final double gearRatio = 1;

    public ShoulderConfig() {
        super("Shoulder");
        this.kP = 0.4; // not accurate value, just testing
        this.kI = 0; // could be 0
        this.kD = 0; // could be 0
        this.kF = 0.064;
        this.motionCruiseVelocity = 10500;
        this.motionAcceleration = 42000;

        this.currentLimit = 10;
        this.tirggerThresholdLimit = 10;
        this.PeakCurrentDuration = 0.0;
        this.EnableCurrentLimit = true;
        this.kNeutralMode = NeutralMode.Brake;
        updateTalonFXConfig();
    }
}
