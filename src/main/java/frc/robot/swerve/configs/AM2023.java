package frc.robot.swerve.configs;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.util.Units;
import frc.robot.RobotConfig.Motors;
import frc.robot.swerve.configTemplates.GyroConfig;
import frc.robot.swerve.configTemplates.ModuleConfig;
import frc.robot.swerve.configTemplates.PhysicalConfig;
import frc.robot.swerve.configTemplates.PhysicalConfig.AngleSensorType;
import frc.robot.swerve.configTemplates.SwerveConfig;
import frc.robot.swerve.configTemplates.TuningConfig;

public class AM2023 {
    /* Angle Offsets */
    public static final double mod0angleOffset = 193.7988; // 194.23828
    public static final double mod1angleOffset = 169.98; // 169.716797
    public static final double mod2angleOffset = 257.25586; // 256.640625
    public static final double mod3angleOffset = 308.0566; // 307.44141

    /* Physical Configs */
    static final double trackWidth = Units.inchesToMeters(18.5);
    static final double wheelBase = Units.inchesToMeters(21.5);
    static final double wheelDiameter = Units.inchesToMeters(3.965); // 3.85 worked for 1 meter
    static final double driveGearRatio = (6.746 / 1.0);
    static final double angleGearRatio = (50.0 / 14.0) * (60.0 / 10.0);
    static final boolean driveMotorInvert = true;
    static final boolean angleMotorInvert = true;
    static final boolean angleSensorInvert = false;

    // Tuning Config
    /* Angle Motor PID Values */
    static final double angleKP = 0.6;
    static final double angleKD = 12;

    /* Drive Motor PID Values */
    static final double driveKP = 0.1;
    static final double driveKD = 0.0;

    /* Drive Motor Characterization Values */
    static final double driveKS =
            (0.13368 / 12); // (0.605 / 12); // /12 to convert from volts to %output
    static final double driveKV = (2.2682 / 12); // (1.72 / 12);
    static final double driveKA = (0.24865 / 12); // (0.193 / 12);

    /* Swerve Profiling Values */
    public static final double maxVelocity =
            ((6380 / 60) / driveGearRatio) * wheelDiameter * Math.PI;
    public static final double maxAutoVelocity =
            ((6380 / 60) / driveGearRatio) * wheelDiameter * Math.PI;
    static final double maxAccel = maxVelocity * 1.5; // take 1/2 sec to get to max speed.
    static final double maxAngularVelocity =
            maxVelocity / Math.hypot(trackWidth / 2.0, wheelBase / 2.0) * 0.8;
    static final double maxAngularAcceleration = Math.pow(maxAngularVelocity, 2) * 0.8;

    /*Rotation Controller*/
    public static final double kPRotationController = 7;
    public static final double kIRotationController = 0.0;
    public static final double kDRotationController = 0.1;

    public static final GyroConfig gyro = INFRARED2022.gyro;

    public static final PhysicalConfig physical =
            new PhysicalConfig(
                    trackWidth,
                    wheelBase,
                    wheelDiameter,
                    driveGearRatio,
                    angleGearRatio,
                    driveMotorInvert,
                    angleMotorInvert,
                    angleSensorInvert,
                    AngleSensorType.CANCoder);

    public static final TuningConfig tuning =
            new TuningConfig(
                            angleKP,
                            angleKD,
                            driveKP,
                            driveKD,
                            driveKS,
                            driveKV,
                            driveKA,
                            maxVelocity,
                            maxAutoVelocity,
                            maxAccel,
                            maxAngularVelocity,
                            maxAngularAcceleration)
                    .configNeutralModes(NeutralMode.Brake, NeutralMode.Brake)
                    .configRotationController(
                            kPRotationController, kIRotationController, kDRotationController);

    /* Module Configs */
    static final ModuleConfig Mod0 =
            new ModuleConfig(
                    Motors.driveMotor0, Motors.angleMotor0, 3, mod0angleOffset, physical, tuning);

    static final ModuleConfig Mod1 =
            new ModuleConfig(
                    Motors.driveMotor1, Motors.angleMotor1, 13, mod1angleOffset, physical, tuning);

    static final ModuleConfig Mod2 =
            new ModuleConfig(
                    Motors.driveMotor2, Motors.angleMotor2, 23, mod2angleOffset, physical, tuning);

    static final ModuleConfig Mod3 =
            new ModuleConfig(
                    Motors.driveMotor3, Motors.angleMotor3, 33, mod3angleOffset, physical, tuning);

    public static final ModuleConfig[] modules = new ModuleConfig[] {Mod0, Mod1, Mod2, Mod3};

    public static final SwerveConfig config = new SwerveConfig(physical, tuning, gyro, modules);
}
