package frc.robot.auton.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.auton.AutonConfig;
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
        return ShoulderCommands.autonHome().withTimeout(1.5).alongWith(SlideCommands.home());
    }

    public static Command stopMotors() {
        return IntakeCommands.stopAllMotors().withTimeout(AutonConfig.stopTime);
    }

    public static Command retractIntake() { // TODO: joseph come back
        return homeSystems().alongWith(IntakeCommands.stopAllMotors());
    }

    // public static Command intakeCube() {
    //     return new CubeIntake()
    //             .alongWith(ElevatorCommands.autonCubeIntake(), FourBarCommands.cubeIntake());
    // }

    public static Command intakeCone() {
        return IntakeCommands.coneIntake();
    }

    public static Command intakeCube() {
        return IntakeCommands.cubeIntake();
    }

    public static Command eject() {
        return IntakeCommands.eject();
    }

    // public static Command intakeCone() {
    //     return new ConeIntake()
    //             .alongWith(
    //                     ElevatorCommands.autonConeStandingIntake(),
    //                     FourBarCommands.coneStandingIntake())
    //             .withName("AutonStandingCone");
    // }

    public static Command stopElevator() { // TODO: joseph come back
        return new RunCommand(() -> Robot.slide.stop(), Robot.slide).withTimeout(0.1);
    }

    // public static Command coneMid() {
    //     return OperatorCommands.coneMid()
    //             .withTimeout(1.1)
    //             .andThen(
    //                     IntakeCommands.eject()
    //                             .withTimeout(.1)); // TODO: done after slide + op refactor
    // }

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

    public static Command autonConeFloorGoalPostion() {
        return SlideCommands.home().alongWith(ShoulderCommands.coneFloor());
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
