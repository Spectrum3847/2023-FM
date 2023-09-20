package frc.robot.auton.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.auton.AutonConfig;
import frc.robot.elbow.commands.ElbowCommands;
import frc.robot.intake.commands.IntakeCommands;
import frc.robot.operator.commands.OperatorCommands;
import frc.robot.pose.commands.PoseCommands;
import frc.robot.shoulder.commands.ShoulderCommands;
import frc.robot.slide.commands.SlideCommands;
import frc.robot.swerve.commands.AlignToVisionTarget;
import frc.robot.swerve.commands.DriveToVisionTarget;
import frc.robot.swerve.commands.SwerveCommands;
import frc.robot.swerve.commands.SwerveDrive;
import frc.robot.vision.VisionConfig;
import java.util.function.DoubleSupplier;

public class AutonCommands {

    public static void setupDefaultCommand() {}

    /** Goes to 0 */
    private static Command homeSystems() {
        return ShoulderCommands.home()
                .alongWith(SlideCommands.home(), ElbowCommands.home())
                .withTimeout(0.5);
    }

    public static Command stopMotors() {
        return IntakeCommands.stopAllMotors().withTimeout(AutonConfig.stopTime);
    }

    public static Command retractIntake() {
        return homeSystems().alongWith(IntakeCommands.stopAllMotors());
    }

    // intaking is the same for cone and cube
    public static Command intake() {
        return IntakeCommands.intake()
                .alongWith(SlideCommands.home(), ShoulderCommands.intake(), ElbowCommands.intake());
    }

    public static Command eject() {
        return IntakeCommands.drop();
    }

    public static Command stopElevator() {
        return new RunCommand(() -> Robot.slide.stop(), Robot.slide).withTimeout(0.1);
    }

    public static Command autonFloorScorePosition() {
        return SlideCommands.home().alongWith(ShoulderCommands.floor(), ElbowCommands.floor());
    }

    public static Command aimPilotDrive(double goalAngleRadians) {
        return aimPilotDrive(() -> goalAngleRadians);
    }

    /** Reset the Theata Controller and then run the SwerveDrive command and pass a goal Supplier */
    public static Command aimPilotDrive(DoubleSupplier goalAngleSupplierRadians) {
        return SwerveCommands.resetTurnController()
                .andThen(
                        new SwerveDrive(
                                () -> 0,
                                () -> 0,
                                () ->
                                        Robot.swerve.calculateRotationController(
                                                goalAngleSupplierRadians)))
                .withName("AutoAimPilotDrive");
    }

    public static Command faceForward() {
        return aimPilotDrive(Math.PI);
    }

    public static Command faceBackward() {
        return aimPilotDrive(0);
    }

    public static Command cubeMidPreScore() {
        return SlideCommands.home().alongWith(ShoulderCommands.cubeUp(), ElbowCommands.cubeUp());
    }

    public static Command cubeMidFull() {
        return cubeMidPreScore().withTimeout(2).andThen(eject().withTimeout(1));
    }

    public static Command cubeTopPreScore() {
        return SlideCommands.fullExtend()
                .alongWith(ShoulderCommands.cubeUp(), ElbowCommands.cubeUp());
    }

    public static Command cubeTopFull() {
        return cubeTopPreScore().withTimeout(2).andThen(eject().withTimeout(1));
    }

    public static Command coneMidPreScore() {
        return IntakeCommands.slowIntake()
                .alongWith(
                        SlideCommands.home(), ShoulderCommands.coneTop(), ElbowCommands.coneTop());
    }

    public static Command coneMidFull() {
        return coneMidPreScore().withTimeout(2).andThen(eject().withTimeout(0.1));
    }

    public static Command coneTopPreScore() {
        return IntakeCommands.slowIntake()
                .alongWith(
                        SlideCommands.fullExtend(),
                        ShoulderCommands.coneTop(),
                        ElbowCommands.coneTop());
    }

    public static Command coneTopFull() {
        return coneTopPreScore()
                .withTimeout(2)
                .andThen(
                        ElbowCommands.score()
                                .withTimeout(0.1)
                                .andThen(IntakeCommands.drop())
                                .withTimeout(0.3)
                                .andThen(OperatorCommands.homeSystems().withTimeout(2.5)));
    }

    public static Command coneTop() {
        return coneTopPreScore()
                .withTimeout(1.3)
                .andThen(
                        ElbowCommands.score()
                                .withTimeout(0.05)
                                .andThen(IntakeCommands.drop())
                                .withTimeout(0.2));
    }

    public static Command alignToGridMid() {
        return new DriveToVisionTarget(VisionConfig.DETECT_LL, 0, VisionConfig.aprilTagPipeline)
                // .alongWith(cubeMidPreScore())
                .withTimeout(0.75)
                .andThen(eject());
    }

    public static Command alignToConeNode() {
        return new AlignToVisionTarget(
                        VisionConfig.DEFAULT_LL, () -> 0, 0, VisionConfig.reflectivePipeline)
                .alongWith(IntakeCommands.stopAllMotors())
                .alongWith(ElbowCommands.stop())
                .alongWith(ShoulderCommands.stop())
                .alongWith(SlideCommands.stop());
    }

    public static Command alignToCubeNode() {
        return new AlignToVisionTarget(
                        VisionConfig.DEFAULT_LL, () -> 0, 0, VisionConfig.aprilTagPipeline)
                .alongWith(IntakeCommands.stopAllMotors())
                .alongWith(ElbowCommands.stop())
                .alongWith(ShoulderCommands.stop())
                .alongWith(SlideCommands.stop());
    }

    public static Command alignToCubeFloor() {
        return new AlignToVisionTarget(
                        VisionConfig.DETECT_LL, () -> 0, 0, VisionConfig.cubeDetectorPipeline)
                .alongWith(IntakeCommands.stopAllMotors())
                .alongWith(ElbowCommands.stop())
                .alongWith(ShoulderCommands.stop())
                .alongWith(SlideCommands.stop());
    }

    public static Command alignToConeFloor() {
        return new AlignToVisionTarget(
                        VisionConfig.DETECT_LL, () -> 0, 0, VisionConfig.coneDetectorPipeline)
                .alongWith(IntakeCommands.stopAllMotors())
                .alongWith(ElbowCommands.stop())
                .alongWith(ShoulderCommands.stop())
                .alongWith(SlideCommands.stop());
    }

    public static Command driveToConeNode() {
        return PoseCommands.resetHeading(180)
                .andThen(
                        new DriveToVisionTarget(
                                        VisionConfig.DEFAULT_LL,
                                        0,
                                        VisionConfig.reflectivePipeline,
                                        Math.PI)
                                .alongWith(IntakeCommands.stopAllMotors())
                                .alongWith(ElbowCommands.stop())
                                .alongWith(ShoulderCommands.stop())
                                .alongWith(SlideCommands.stop()));
    }

    public static Command driveToConeFloor() {
        return PoseCommands.resetHeading(180)
                .andThen(
                        new DriveToVisionTarget(
                                        VisionConfig.DETECT_LL,
                                        0,
                                        VisionConfig.coneDetectorPipeline,
                                        Math.PI)
                                .alongWith(IntakeCommands.stopAllMotors())
                                .alongWith(ElbowCommands.stop())
                                .alongWith(ShoulderCommands.stop())
                                .alongWith(SlideCommands.stop()));
    }
}
