package frc.robot.operator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.elbow.commands.ElbowCommands;
import frc.robot.exceptions.KillRobotException;
import frc.robot.intake.commands.IntakeCommands;
import frc.robot.shoulder.commands.ShoulderCommands;
import frc.robot.slide.commands.SlideCommands;

public class OperatorCommands {
    public static void setupDefaultCommand() {
        Robot.operatorGamepad.setDefaultCommand(
                rumble(0, 9999).repeatedly().withName("DisableOperatorRumble"));
    }

    /* Intaking Commands */

    public static Command intake() {
        return IntakeCommands.intake()
                .alongWith(SlideCommands.home(), ShoulderCommands.intake(), ElbowCommands.intake())
                .finallyDo((b) -> homeAndSlowIntake().withTimeout(1).schedule())
                .withName("OperatorIntake");
    }

    public static Command airIntake() {
        return IntakeCommands.intake()
                .alongWith(
                        SlideCommands.home(),
                        ShoulderCommands.airIntake(),
                        ElbowCommands.airIntake())
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorAirIntake");
    }

    public static Command shelfIntake() {
        return IntakeCommands.intake()
                .alongWith(
                        SlideCommands.home(),
                        ShoulderCommands.shelfIntake(),
                        ElbowCommands.shelfIntake())
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorShelfIntake");
    }

    /* Position Commands */

    // Move to coneFloor position and eject cone
    public static Command floorScore() {
        return SlideCommands.home()
                .alongWith(
                        ShoulderCommands.floor(),
                        ElbowCommands.floor(),
                        new WaitCommand(0.2).andThen(IntakeCommands.drop()))
                .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorFloorScore");
    }

    public static Command coneMid() {
        return IntakeCommands.slowIntake()
                .alongWith(SlideCommands.home(), ShoulderCommands.coneUp(), ElbowCommands.coneUp())
                .withName("OperatorConeMid");
    }

    public static Command coneTop() {
        return IntakeCommands.slowIntake()
                .alongWith(
                        SlideCommands.fullExtend(),
                        ShoulderCommands.coneUp(),
                        ElbowCommands.coneUp())
                .withName("OperatorConeTop");
    }

    public static Command cubeMid() {
        return SlideCommands.home()
                .alongWith(ShoulderCommands.cubeUp(), ElbowCommands.cubeUp())
                .withName("OperatorCubeMid");
    }

    public static Command cubeTop() {
        return SlideCommands.fullExtend()
                .alongWith(ShoulderCommands.cubeUp(), ElbowCommands.cubeUp())
                .withName("OperatorCubeTop");
    }

    /** Sets Slide, Shoulder, and Elbow to coast mode */
    public static Command coastMode() {
        return SlideCommands.coastMode()
                .alongWith(ShoulderCommands.coastMode(), ElbowCommands.coastMode())
                .withName("OperatorCoastMode");
    }

    public static Command homeAndSlowIntake() {
        return IntakeCommands.slowIntake()
                .alongWith(homeSystems())
                .withName("OperatorSlowHomeIntake");
    }

    /** Goes to home position */
    public static Command homeSystems() {
        return SlideCommands.home()
                .alongWith(ShoulderCommands.home(), ElbowCommands.home())
                .withName("OperatorHomeSystems");
    }

    public static Command manualSlide() {
        return new RunCommand(
                        () -> Robot.slide.setManualOutput(Robot.operatorGamepad.slideManual()),
                        Robot.slide)
                .withName("OperatorManualSlide");
    }

    public static Command manualShoulder() {
        return new RunCommand(
                        () ->
                                Robot.shoulder.setManualOutput(
                                        Robot.operatorGamepad.shoulderManual()),
                        Robot.shoulder)
                .withName("OperatorManualShoulder");
    }

    public static Command manualElbow() {
        return new RunCommand(
                        () -> Robot.elbow.setManualOutput(Robot.operatorGamepad.elbowManual()),
                        Robot.elbow)
                .withName("OperatorManualElbow");
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

    public static Command killTheRobot() {
        return new InstantCommand(() -> operatorError()).ignoringDisable(true);
    }

    public static void operatorError() {
        throw new KillRobotException("The robot was killed by operator");
    }
}
