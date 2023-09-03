package frc.robot.elbow;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.SpectrumLib.subsystems.angleMech.AngleMechConfig;

public class ElbowConfig extends AngleMechConfig {

    public boolean kInverted = true;

    public final int elbowMaxFalcon = 63256; // 63256

    // Positions set as percentage of
    public final int initializedPosition = 90;

    public final int intake = -104;
    public final int airIntake = -43;
    public final int shelfIntake = -108;

    public final int home = -38;
    public final int unblockCameraPos = -88;
    public final int stow = -29;

    public final int floor = -88;
    public final int coneTop = -89;
    public final double coneMid = -93;
    public final int cubeUp = -108;

    public final int scorePos = coneTop - 17;
    // public final int cubeScorePos = cubeUp - 2;

    public final double zeroSpeed = -0.2;

    // Physical Constants
    public final double gearRatio = 1; // TODO: change; not actually used though

    public ElbowConfig() {
        super("Elbow");
        this.kP = 0.4;
        this.kI = 0;
        this.kD = 0;
        this.kF = 0.064;
        this.motionCruiseVelocity = 10500; // 10500
        this.motionAcceleration = 42000;

        this.currentLimit = 10;
        this.tirggerThresholdLimit = 10;
        this.PeakCurrentDuration = 0.0;
        this.EnableCurrentLimit = true;
        this.kNeutralMode = NeutralMode.Brake;
        updateTalonFXConfig();
    }
}
