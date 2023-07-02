package frc.robot.auton.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.auton.AutonConfig;
import frc.robot.elbow.commands.ElbowCommands;
import frc.robot.intake.commands.IntakeCommands;
import frc.robot.operator.commands.OperatorCommands;
import frc.robot.shoulder.commands.ShoulderCommands;
import frc.robot.slide.commands.SlideCommands;
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
        return IntakeCommands.intake();
    }

    public static Command eject() {
        return IntakeCommands.eject();
    }

    public static Command stopElevator() {
        return new RunCommand(() -> Robot.slide.stop(), Robot.slide).withTimeout(0.1);
    }

    public static Command coneMid() {
        return OperatorCommands.coneMid()
                .withTimeout(1.1)
                .andThen(IntakeCommands.eject().withTimeout(.1));
    }

    public static Command coneMidFull() {
        return OperatorCommands.coneMid()
                .withTimeout(1.1)
                .andThen(IntakeCommands.eject().withTimeout(.1))
                .andThen(retractIntake().withTimeout(1.1));
    }

    public static Command coneTop() {
        return OperatorCommands.coneTop()
                .withTimeout(1.7)
                .andThen(IntakeCommands.eject().withTimeout(.1));
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
}
