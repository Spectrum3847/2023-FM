package frc.robot.shoulder;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.SpectrumLib.subsystems.angleMech.AngleMechConfig;

public class ShoulderConfig extends AngleMechConfig {

    public boolean kInverted = true;

    public final int shoulderMaxFalcon = 135181;

    // Positions set as percentage of shoulder
    public final int intake = 0;
    public final int airIntake = 17; // 23071 //done
    public final int shelfIntake = 77; // 104127 //done

    public final int home = 17;

    public final int floor = 20;
    public final int coneUp = 98; // 132550 //done
    public final int cubeUp = 100; // 132550 //done

    public final int safePositionForElbow = 55;

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
