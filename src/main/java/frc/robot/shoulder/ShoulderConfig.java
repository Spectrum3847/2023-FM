package frc.robot.shoulder;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.SpectrumLib.subsystems.angleMech.AngleMechConfig;

public class ShoulderConfig extends AngleMechConfig {

    public boolean kInverted = true;

    public final int shoulderMaxFalcon = 135181;

    // Positions set as percentage of shoulder

    public final int coneIntake = 0;
    public final int coneStandingIntake = 46;
    public final int coneShelf = 77; // 104127 //done

    public final int airConeIntake = 17; // 23071 //done
    public final int airCubeIntake = 17; // same //done

    public final int coneFloor = 30;
    public final int coneMid = 98; // 132550 //done
    public final int coneTop = 98; // same //done

    public final int cubeIntake = 0;
    public final int cubeMid = 98; // 132550 //done
    public final int cubeTop = 98;
    public final int cubeFloor = 0;

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
