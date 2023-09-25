package frc.robot.auton.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.auton.AutonConfig;
import frc.robot.elbow.commands.ElbowCommands;
import frc.robot.intake.commands.IntakeCommands;
import frc.robot.operator.commands.OperatorCommands;
import frc.robot.pose.commands.PoseCommands;
import frc.robot.shoulder.commands.ShoulderCommands;
import frc.robot.slide.commands.SlideCommands;
import frc.robot.swerve.commands.AlignToAprilTag;
import frc.robot.swerve.commands.AlignToConeNode;
import frc.robot.swerve.commands.DriveToConeFloor;
import frc.robot.swerve.commands.DriveToConeNode;
import frc.robot.swerve.commands.DriveToCubeNode;
import frc.robot.swerve.commands.SwerveCommands;
import frc.robot.swerve.commands.SwerveDrive;
import frc.robot.vision.VisionCommands;
import java.util.function.DoubleSupplier;

public class AutonCommands {

    /** Goes to 0 */
    public static Command homeSystems() {
        return ShoulderCommands.home()
                .alongWith(SlideCommands.home(), ElbowCommands.home())
                .withTimeout(1.5);
    }

    public static Command stopMotors() {
        return IntakeCommands.stopAllMotors().withTimeout(AutonConfig.stopTime);
    }

    public static Command retractIntake() {
        return OperatorCommands.homeAfterFloorIntake();
    }

    // intaking is the same for cone and cube
    public static Command floorIntake() {
        return ShoulderCommands.intake()
                .withTimeout(.25)
                .andThen(
                        IntakeCommands.intake()
                                .alongWith(
                                        SlideCommands.home(),
                                        ShoulderCommands.intake(),
                                        ElbowCommands.intake()));
    }

    public static Command holdIntake() {
        return IntakeCommands.holdPercentOutput();
    }

    public static Command scoreGP() {
        return ElbowCommands.score()
                .withTimeout(0.05)
                .andThen(IntakeCommands.drop().alongWith(ElbowCommands.score()))
                .withTimeout(0.2);
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

    public static Command alignWheelsStraight() {
        return new SwerveDrive(
                () -> 0.04,
                () -> 0,
                () -> Robot.swerve.calculateRotationController(() -> 0),
                () -> 1.0,
                () -> false,
                false);
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

    public static Command finishIntakeDrive() {
        return new SwerveDrive(() -> 1, () -> 0, () -> 0, () -> 1.0, () -> false, false)
                .alongWith(IntakeCommands.intake())
                .withTimeout(0.3);
    }

    public static Command floorPreSchool() {
        return OperatorCommands.floorScore();
    }

    public static Command cubeMidPreScore() {
        return SlideCommands.home()
                .alongWith(
                        ShoulderCommands.cubeUp(),
                        ElbowCommands.cubeUp(),
                        AutonCommands.holdIntake());
    }

    public static Command cubeMidFull() {
        return cubeMidPreScore().withTimeout(2).andThen(scoreGP().withTimeout(1));
    }

    public static Command cubeTopPreScore() {
        return SlideCommands.fullExtend()
                .alongWith(
                        ShoulderCommands.cubeUp(),
                        ElbowCommands.cubeUp(),
                        AutonCommands.holdIntake());
    }

    public static Command cubeTopFull() {
        return cubeTopPreScore().withTimeout(2).andThen(scoreGP().withTimeout(1));
    }

    public static Command coneMidPreScore() {
        return AutonCommands.holdIntake()
                .alongWith(
                        SlideCommands.home(), ShoulderCommands.coneTop(), ElbowCommands.coneTop());
    }

    public static Command coneMidFull() {
        return coneMidPreScore().withTimeout(2).andThen(scoreGP().withTimeout(0.1));
    }

    public static Command coneTopPreScore() {
        return AutonCommands.holdIntake()
                .alongWith(
                        SlideCommands.fullExtend(),
                        ShoulderCommands.coneTop(),
                        ElbowCommands.coneTop());
    }

    public static Command coneTopFull() {
        return coneTopPreScore()
                .withTimeout(2)
                .andThen(scoreGP().andThen(OperatorCommands.homeSystems().withTimeout(2.5)));
    }

    public static Command coneTopScore() {
        return coneTopPreScore().withTimeout(1.2).andThen(scoreGP());
    }

    /*
     * public static Command alignToGridMid() {
     * return new DriveToVisionTarget(VisionConfig.DETECT_LL, 0,
     * VisionConfig.aprilTagPipeline)
     * // .alongWith(cubeMidPreScore())
     * .withTimeout(0.75)
     * .andThen(eject());
     * }
     *
     * public static Command alignToConeNode() {
     * return new AlignToVisionTarget(
     * VisionConfig.DEFAULT_LL, () -> 0, 0, VisionConfig.reflectivePipeline)
     * .alongWith(IntakeCommands.stopAllMotors())
     * .alongWith(ElbowCommands.stop())
     * .alongWith(ShoulderCommands.stop())
     * .alongWith(SlideCommands.stop());
     * }
     *
     * public static Command alignToCubeNode() {
     * return new AlignToVisionTarget(
     * VisionConfig.DEFAULT_LL, () -> 0, 0, VisionConfig.aprilTagPipeline)
     * .alongWith(IntakeCommands.stopAllMotors())
     * .alongWith(ElbowCommands.stop())
     * .alongWith(ShoulderCommands.stop())
     * .alongWith(SlideCommands.stop());
     * }
     *
     * public static Command alignToCubeFloor() {
     * return new AlignToVisionTarget(
     * VisionConfig.DETECT_LL, () -> 0, 0, VisionConfig.cubeDetectorPipeline)
     * .alongWith(IntakeCommands.stopAllMotors())
     * .alongWith(ElbowCommands.stop())
     * .alongWith(ShoulderCommands.stop())
     * .alongWith(SlideCommands.stop());
     * }
     *
     * public static Command alignToConeFloor() {
     * return new AlignToVisionTarget(
     * VisionConfig.DETECT_LL, () -> 0, 0, VisionConfig.coneDetectorPipeline)
     * .alongWith(IntakeCommands.stopAllMotors())
     * .alongWith(ElbowCommands.stop())
     * .alongWith(ShoulderCommands.stop())
     * .alongWith(SlideCommands.stop());
     * }
     *
     * public static Command driveToConeNode() {
     * return PoseCommands.resetHeading(180)
     * .andThen(
     * new DriveToVisionTarget(
     * VisionConfig.DEFAULT_LL,
     * 0,
     * VisionConfig.reflectivePipeline,
     * Math.PI)
     * .alongWith(IntakeCommands.stopAllMotors())
     * .alongWith(ElbowCommands.stop())
     * .alongWith(ShoulderCommands.stop())
     * .alongWith(SlideCommands.stop()));
     * }
     *
     * public static Command driveToConeFloor() {
     * return PoseCommands.resetHeading(180)
     * .andThen(
     * new DriveToVisionTarget(
     * VisionConfig.DETECT_LL,
     * 0,
     * VisionConfig.coneDetectorPipeline,
     * Math.PI)
     * .alongWith(IntakeCommands.stopAllMotors())
     * .alongWith(ElbowCommands.stop())
     * .alongWith(ShoulderCommands.stop())
     * .alongWith(SlideCommands.stop()));
     * }
     */

     //Test Auton commands should not be used in match
    public static Command AlignToAprilTagTest() {
        return PoseCommands.resetHeading(180)
                .andThen(
                        VisionCommands.setCubeNodePipeline()
                                .withTimeout(1)
                                .alongWith(
                                        new WaitCommand(
                                                1)), // Switch to cube pipeline and wait 1 sec
                        new AlignToAprilTag(() -> 0, 0)
                                .alongWith(IntakeCommands.stopAllMotors())
                                .alongWith(ElbowCommands.stop())
                                .alongWith(ShoulderCommands.stop())
                                .alongWith(SlideCommands.stop()));
    }

    public static Command DriveToCubeNode() {
        return PoseCommands.resetHeading(180)
                .andThen(
                        VisionCommands.setCubeNodePipeline()
                                .alongWith(
                                        new WaitCommand(
                                                1)), // Switch to cube pipeline and wait 1 sec
                        new DriveToCubeNode(0)
                                .alongWith(
                                        IntakeCommands.stopAllMotors(),
                                        ElbowCommands.stop(),
                                        ShoulderCommands.stop(),
                                        SlideCommands.stop()));
    }

    public static Command AlignToConeNodeTest() {
        return PoseCommands.resetHeading(180)
                // .andThen(VisionCommands.setConeNodePipeline(), new WaitCommand(1))
                .andThen(
                        new AlignToConeNode(() -> 0, 0)
                                .alongWith(IntakeCommands.stopAllMotors())
                                .alongWith(ElbowCommands.stop())
                                .alongWith(ShoulderCommands.stop())
                                .alongWith(SlideCommands.stop()));
    }

    public static Command DriveToConeNodeTest() {
        return PoseCommands.resetHeading(180)
                .andThen(new DriveToConeNode(2).alongWith()) // OperatorCommands.coneMid()))
                .andThen(SwerveCommands.stop());
    }

    public static Command DriveToConeFloorTest() {
        return PoseCommands.resetHeading(180)
                .andThen(
                        new DriveToConeFloor(0)
                                .withTimeout(1)
                                .andThen(finishIntakeDrive())) // OperatorCommands.coneMid()))
                .andThen(SwerveCommands.stop());
    }
}
