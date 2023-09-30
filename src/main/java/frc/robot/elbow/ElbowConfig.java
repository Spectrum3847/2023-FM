package frc.robot.elbow;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.SpectrumLib.subsystems.angleMech.AngleMechConfig;

public class ElbowConfig extends AngleMechConfig {

    public boolean kInverted = true;

    public final int elbowMaxFalcon = 63256; // 63256

    // Positions set as percentage of
    // 0 is vertical
    public final int initializedPosition = 90;

    public final double intake = -96;
    public final double airIntake = -36;
    public final double shelfIntake = -95;

    public final double home = -45; // -25
    public final double unblockCameraPos = -88;
    public final double stow = -29;

    public final double floor = -65;
    public final double coneTop = -80;
    public final double coneMid = coneTop;
    public final double prescore = -60;
    public final double cubeUp = -112; // -108

    public final double scorePos = -106;

    public final double throwBack = 0;
    public final double throwFwd = -50;
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

        this.currentLimit = 5;
        this.tirggerThresholdLimit = 5;
        this.PeakCurrentDuration = 0.0;
        this.EnableCurrentLimit = true;
        this.kNeutralMode = NeutralMode.Brake;
        updateTalonFXConfig();
    }
}
