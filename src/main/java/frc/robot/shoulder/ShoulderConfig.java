package frc.robot.shoulder;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.SpectrumLib.subsystems.angleMech.AngleMechConfig;

public class ShoulderConfig extends AngleMechConfig {

    public boolean kInverted = true;

    public final int shoulderMaxFalcon = 135181;

    // Positions set as percentage of shoulder
    public final int initializedPosition = 20;

    public final int intake = 0;
    public final int airIntake = 15;
    public final int shelfIntake = 82;

    public final int home = 22;
    public final int unblockCameraPos = 22;
    public final int stow = 31;

    public final int floor = 20;
    public final int prescore = 60;
    public final int coneTop = 98;
    public final int coneMid = coneTop;
    public final int cubeUp = 90; // 93

    public final int safePositionForElbow = 55;

    public final double zeroSpeed = -0.2;

    // Physical Constants
    public final double gearRatio = 1;

    public ShoulderConfig() {
        super("Shoulder");
        this.kP = 0.4;
        this.kI = 0;
        this.kD = 0;
        this.kF = 0.064;
        this.motionCruiseVelocity = 10500; // 10500
        this.motionAcceleration = 42000;

        this.currentLimit = 5;
        this.tirggerThresholdLimit = 5;
        this.PeakCurrentDuration = 0.0;
        this.EnableCurrentLimit = true;
        this.kNeutralMode = NeutralMode.Brake;
        updateTalonFXConfig();
    }
}
