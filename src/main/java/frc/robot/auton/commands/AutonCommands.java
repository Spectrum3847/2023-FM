package frc.robot.auton.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.auton.AutonConfig;
import frc.robot.elbow.commands.ElbowCommands;
import frc.robot.intake.commands.IntakeCommands;
import frc.robot.shoulder.commands.ShoulderCommands;
import frc.robot.slide.commands.SlideCommands;
import frc.robot.swerve.commands.DriveToCubeNode;
import frc.robot.swerve.commands.SwerveCommands;
import frc.robot.swerve.commands.SwerveDrive;
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
                .alongWith(SlideCommands.home(), ShoulderCommands.coneUp(), ElbowCommands.coneUp());
    }

    public static Command coneMidFull() {
        return coneMidPreScore().withTimeout(2).andThen(eject().withTimeout(0.1));
    }

    public static Command coneTopPreScore() {
        return IntakeCommands.slowIntake()
                .alongWith(
                        SlideCommands.fullExtend(),
                        ShoulderCommands.coneUp(),
                        ElbowCommands.coneUp());
    }

    public static Command coneTopFull() {
        return coneTopPreScore().withTimeout(2).andThen(eject().withTimeout(0.1));
    }

    public static Command alignToGridMid() {
        return new DriveToCubeNode(0, true)
                .alongWith(cubeMidPreScore())
                .withTimeout(0.75)
                .andThen(eject());
    }
}
