package frc.robot.operator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.elbow.commands.ElbowCommands;
import frc.robot.intake.commands.IntakeCommands;
import frc.robot.operator.OperatorConfig;
import frc.robot.shoulder.Shoulder;
import frc.robot.shoulder.commands.ShoulderCommands;
import frc.robot.shoulder.commands.ShoulderDelay;
import frc.robot.slide.Slide;
import frc.robot.slide.commands.SlideCommands;

public class OperatorCommands {
    public static void setupDefaultCommand() {
        Robot.operatorGamepad.setDefaultCommand(
                rumble(0, 9999).repeatedly().withName("DisableOperatorRumble"));
    }

    // TODO: change elevator commands
    /* Intaking Commands */

    //

    public static Command coneStandingIntake() {
        return IntakeCommands.coneIntake()
                .alongWith(SlideCommands.home(), ShoulderCommands.coneStandingIntake())
                .withName("OperatorStandingCone")
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule());
    }

    /* Position Commands */

    public static Command coneIntake() {
        return IntakeCommands.coneIntake()
                .alongWith(SlideCommands.home(), ShoulderCommands.coneIntake())
                .withName("OperatorFloorCone");
    }

    // Called by finally do, to let the intake hop up, and keep intaking for a bit after button
    // release

    /* Move to coneFloor position and eject cone */
    public static Command coneFloorGoal() {
        return SlideCommands.home()
                .alongWith(
                        ShoulderCommands.coneFloorGoal(),
                        new WaitCommand(0.2).andThen(IntakeCommands.eject()))
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorConeFloorGoal");
    }

    public static Command coneFloorIntake() {
        return IntakeCommands.coneIntake()
                .alongWith(
                        SlideCommands.home(),
                        ShoulderCommands.coneIntake(),
                        ElbowCommands.coneIntake())
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorConeFloorIntake");
    }

    public static Command cubeFloorIntake() {
        return IntakeCommands.cubeIntake()
                .alongWith(
                        SlideCommands.home(),
                        ShoulderCommands.cubeIntake(),
                        ElbowCommands.cubeIntake())
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorCubeFloorIntake");
    }

    public static Command coneAirIntake() {
        return IntakeCommands.coneIntake()
                .alongWith(
                        SlideCommands.home(),
                        ShoulderCommands.airConeIntake(),
                        ElbowCommands.airConeIntake())
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorConeAirIntake");
    }

    public static Command cubeAirIntake() {
        return IntakeCommands.cubeIntake()
                .alongWith(
                        SlideCommands.fullExtend(),
                        ShoulderCommands.airCubeIntake(),
                        ElbowCommands.airCubeIntake())
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorCubeAirIntake");
    }

    public static Command coneShelfIntake() {
        return IntakeCommands.coneIntake()
                .alongWith(
                        SlideCommands.fullExtend(),
                        ShoulderCommands.topConeIntake(),
                        ElbowCommands.topConeIntake())
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorConeDoubleSubIntake");
    }

    public static Command cubeShelfIntake() {
        return IntakeCommands.coneIntake()
                .alongWith(
                        SlideCommands.fullExtend(),
                        ShoulderCommands.topCubeIntake(),
                        ElbowCommands.topCubeIntake())
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorCubeDoubleSubIntake");
    }

    public static Command coneMid() {
        return IntakeCommands.slowIntake()
                .alongWith(
                        SlideCommands.home(),
                        ShoulderCommands.coneMid(),
                        ElbowCommands.coneMid(),
                        new WaitCommand(0.2)
                                .andThen(IntakeCommands.eject())
                                .finallyDo((b) -> homeSystems().withTimeout(1).schedule()))
                .withName("OperatorConeMid");
    }

    public static Command cubeMid() {
        return SlideCommands.home()
                .alongWith(
                        ShoulderCommands.cubeMid(),
                        ElbowCommands.cubeMid(),
                        new WaitCommand(0.2)
                                .andThen(IntakeCommands.eject())
                                .finallyDo((b) -> homeSystems().withTimeout(1).schedule()))
                .withName("OperatorCubeMid");
    }

    public static Command floorScore() {
        return SlideCommands.home()
                .alongWith(
                        ShoulderCommands.floorScore(),
                        ElbowCommands.floorGoal(),
                        new WaitCommand(0.2)
                                .andThen(IntakeCommands.eject())
                                .finallyDo((b) -> homeSystems().withTimeout(1).schedule()))
                .withName("OperatorFloorScore");
    }

    public static Command slideUp() {
        return SlideCommands.fullExtend();
    }

    public static Command coneTop() {
        return IntakeCommands.slowIntake()
                .alongWith(
                        SlideCommands.fullExtend(),
                        new ShoulderDelay(
                                Shoulder.config.safePositionForElevator,
                                Shoulder.config.coneTop,
                                Slide.config.safePositionForFourBar))
                .withName("OperatorConeTop");
    }

    public static Command cubeIntake() {
        return IntakeCommands.cubeIntake()
                .alongWith(SlideCommands.home(), ShoulderCommands.cubeIntake())
                .withName("OperatorCubeIntake");
    }

    public static Command cubeFloorGoal() {
        return SlideCommands.home()
                .alongWith(ShoulderCommands.cubeFloorGoal(), IntakeCommands.eject())
                .withName("OperatorCubeFloor")
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule());
    }

    /** Sets Elevator and Fourbar to coast mode */
    public static Command coastMode() {
        return SlideCommands.coastMode()
                .alongWith(ShoulderCommands.coastMode())
                .withName("OperatorCoastMode");
    }

    public static Command homeAndSlowIntake() {
        return IntakeCommands.slowIntake()
                .alongWith(homeSystems())
                .withName("OperatorSlowHomeIntake");
    }

    /** Goes to 0 */
    public static Command homeSystems() {
        return SlideCommands.home()
                .alongWith(ShoulderCommands.home(), ElbowCommands.home())
                .withName("OperatorHomeSystems");
    }

    public static Command manualElevator() {
        return new RunCommand(
                        () -> Robot.slide.setManualOutput(Robot.operatorGamepad.elevatorManual()),
                        Robot.slide)
                .withName("OperatorManualElevator");
    }

    public static Command slowManualElevator() {
        return new RunCommand(
                        () ->
                                Robot.slide.setManualOutput(
                                        Robot.operatorGamepad.elevatorManual()
                                                * OperatorConfig.slowModeScalar),
                        Robot.slide)
                .withName("OperatorManualSlowElevator");
    }

    public static Command manualFourBar() {
        return new RunCommand(
                        () -> Robot.shoulder.setManualOutput(Robot.operatorGamepad.fourBarManual()),
                        Robot.shoulder)
                .withName("OperatorManualFourBar");
    }

    public static Command slowManualFourBar() {
        return new RunCommand(
                        () ->
                                Robot.shoulder.setManualOutput(
                                        Robot.operatorGamepad.fourBarManual()
                                                * OperatorConfig.slowModeScalar),
                        Robot.shoulder)
                .withName("OperatorManualSlowFourBar");
    }

    /** Command that can be used to rumble the pilot controller */
    public static Command rumble(double intensity, double durationSeconds) {
        return new RunCommand(() -> Robot.operatorGamepad.rumble(intensity), Robot.operatorGamepad)
                .withTimeout(durationSeconds)
                .withName("OperatorRumble");
    }

    public static Command cancelCommands() {
        return new InstantCommand(() -> CommandScheduler.getInstance().cancelAll())
                .withName("OperatorCancelAll");
    }
}
