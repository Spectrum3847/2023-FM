package frc.robot.slide;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.SpectrumLib.subsystems.linearMech.LinearMechConfig;
import frc.robot.RobotConfig.Motors;

public class SlideConfig extends LinearMechConfig {
    public static final String name = "Slide";

    // All these are in inches
    public final double diameterInches = 1.5038;
    public final double gearRatio = 9 / 1;
    public final double maxUpPos = 59.3;
    public final double maxCarriageHeight = 79000; // 80172 max

    public final double homeThreshold = 1000; // falcon units
    public final double homeTimeout = 0.5; // seconds
    public final double maxHomeTimeout = 4; // seconds, no home command can take longer than this
    public final double holdConeHeight =
            2; // inches, hold cone will not run if the slide is above this position

    public final double zeroSpeed = -0.2;

    public final double LEDheight = 24;

    public static final int slideMotorID = Motors.slideMotor;

    public SlideConfig() {
        super(name);
        this.kP = 0.43;
        this.kI = 0;
        this.kD = 0;
        this.kF = 0.064;
        this.motionCruiseVelocity = 24500; // 24500
        this.motionAcceleration = 40000; // 40000

        this.currentLimit = 30;
        this.tirggerThresholdLimit = 30;
        this.PeakCurrentDuration = 0.0;
        this.EnableCurrentLimit = true;
        this.kNeutralMode = NeutralMode.Brake;
        updateTalonFXConfig();
    }
}
