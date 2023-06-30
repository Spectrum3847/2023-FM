package frc.robot.elbow;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.SpectrumLib.subsystems.angleMech.AngleMechConfig;

public class ElbowConfig extends AngleMechConfig {

    public boolean kInverted = true;

    public final int elbowMaxFalcon = 63256; // 63256

    // Positions set as percentage of elbow

    public final int airConeIntake = 52; // 33194 //done
    public final int airCubeIntake = 52; // same //done
    public final int coneFloorScore = 15; // not used
    public final int cubeFloorScore = 30; // not used

    public final int coneIntake = 0;
    public final int coneStandingIntake = 46;
    public final int coneShelf = 5; // 3191 between 1000 and 3000 //done

    public final int coneFloor = 30;
    public final int coneMid = 19; // 12263 //done
    public final int coneTop = 19; // SAME

    public final int cubeIntake = 70; // 73;
    public final int cubeMid = 0; // 0
    public final int cubeTop = 0;
    public final int cubeFloor = 0;

    public final double zeroSpeed = -0.2;

    // Physical Constants
    public final double gearRatio = 1; // TODO: might change?

    public ElbowConfig() {
        super("Elbow");
        this.kP = 0.4; // not accurate value, just testing //TODO: change
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
