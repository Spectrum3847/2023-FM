package frc.robot.operator.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.elbow.commands.ElbowCommands;
import frc.robot.intake.commands.IntakeCommands;
import frc.robot.operator.OperatorConfig;
import frc.robot.shoulder.commands.ShoulderCommands;
import frc.robot.slide.commands.SlideCommands;

public class OperatorCommands {
    public static void setupDefaultCommand() {
        Robot.operatorGamepad.setDefaultCommand(
                rumble(0, 9999).repeatedly().withName("DisableOperatorRumble"));
    }

    /* Intaking Commands */

    public static Command coneIntake() {
        return IntakeCommands.coneIntake()
                .alongWith(
                        SlideCommands.home(),
                        ShoulderCommands.coneIntake(),
                        ElbowCommands.coneIntake())
                // .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorConeIntake");
    }

    public static Command coneStandingIntake() {
        return IntakeCommands.coneIntake()
                .alongWith(
                        // SlideCommands.home(),
                        ShoulderCommands.coneStandingIntake(), ElbowCommands.coneStandingIntake())
                .withName("OperatorStandingCone");
        // .finallyDo((b) -> homeSystems().withTimeout(1).schedule());
    }

    public static Command airConeIntake() {
        return IntakeCommands.coneIntake()
                .alongWith(
                        SlideCommands.home(),
                        ShoulderCommands.airConeIntake(),
                        ElbowCommands.airConeIntake())
                // .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorAirConeIntake");
    }

    // Called by finally do, to let the intake hop up, and keep intaking for a bit after button
    // release

    public static Command coneShelfIntake() {
        return IntakeCommands.coneIntake()
                .alongWith(
                        SlideCommands.home(),
                        ShoulderCommands.coneShelf(),
                        ElbowCommands.coneShelf())
                // .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorConeShelfIntake");
    }

    public static Command cubeIntake() {
        return IntakeCommands.cubeIntake()
                .alongWith(
                        // SlideCommands.home(),
                        ShoulderCommands.cubeIntake(), ElbowCommands.cubeIntake())
                // .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorCubeIntake");
    }

    public static Command cubeAirIntake() {
        return IntakeCommands.cubeIntake()
                .alongWith(
                        SlideCommands.home(),
                        ShoulderCommands.airCubeIntake(),
                        ElbowCommands.airCubeIntake())
                // .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorCubeAirIntake");
    }

    /* Position Commands */

    /* Move to coneFloor position and eject cone */
    public static Command coneFloor() {
        return SlideCommands.home()
                .alongWith(ShoulderCommands.coneFloor(), ElbowCommands.coneFloor())
                // new WaitCommand(0.2).andThen(IntakeCommands.eject()))
                // .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
                .withName("OperatorConeFloorGoal");
        // return ShoulderCommands.coneFloor()
        //         .alongWith(ElbowCommands.coneFloor())
        //         // new WaitCommand(0.2).andThen(IntakeCommands.eject()))
        //         // .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
        //         .withName("OperatorConeFloorGoal");
    }

    public static Command coneMid() {
        return IntakeCommands.slowIntake()
                .alongWith(
                        SlideCommands.home(), ShoulderCommands.coneMid(), ElbowCommands.coneMid())
                .withName("OperatorConeMid");
    }

    public static Command coneTop() {
        return IntakeCommands.slowIntake()
                .alongWith(
                        SlideCommands.fullExtend(),
                        ShoulderCommands.coneMid(),
                        ElbowCommands.coneMid())
                .withName("OperatorConeTop");
    }

    public static Command cubeFloor() {
        // return SlideCommands()
        //         .alongWith(
        //                 ShoulderCommands.cubeFloor(), ElbowCommands.cubeFloor()
        //                 // new WaitCommand(0.2).andThen(IntakeCommands.eject()))
        //                 )
        //         .finallyDo((b) -> homeSystems().withTimeout(1).schedule())
        //         .withName("OperatorFloorScore");
        return ShoulderCommands.cubeFloor()
                .alongWith(
                        ElbowCommands.cubeFloor()
                        // new WaitCommand(0.2).andThen(IntakeCommands.eject()))
                        )
                .withName("OperatorFloorScore");
    }

    public static Command cubeMid() {
        return SlideCommands.home()
                .alongWith(ShoulderCommands.cubeMid(), ElbowCommands.cubeMid())
                .withName("OperatorCubeMid");
        // return ShoulderCommands.cubeMid()
        //         .alongWith(ElbowCommands.cubeMid())
        //         .withName("OperatorCubeMid");
    }

    public static Command cubeTop() {
        return SlideCommands.fullExtend()
                .alongWith(ShoulderCommands.cubeTop(), ElbowCommands.cubeTop())
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

    /** Goes to 0 */
    public static Command homeSystems() {
        return SlideCommands.home()
                .alongWith(ShoulderCommands.home(), ElbowCommands.home())
                .withName("OperatorHomeSystems");
        // return ShoulderCommands.home()
        //         .alongWith(ElbowCommands.home())
        //         .withName("OperatorHomeSystems");
    }

    public static Command manualSlide() {
        return new RunCommand(
                        () -> Robot.slide.setManualOutput(Robot.operatorGamepad.slideManual()),
                        Robot.slide)
                .withName("OperatorManualSlide");
    }

    public static Command slowManualSlide() {
        return new RunCommand(
                        () ->
                                Robot.slide.setManualOutput(
                                        Robot.operatorGamepad.slideManual()
                                                * OperatorConfig.slowModeScalar),
                        Robot.slide)
                .withName("OperatorManualSlowSlide");
    }

    public static Command manualShoulder() {
        return new RunCommand(
                        () -> Robot.shoulder.setManualOutput(Robot.operatorGamepad.shoulderManual()),
                        Robot.shoulder)
                .withName("OperatorManualShoulder");
    }

    public static Command slowManualShoulder() {
        return new RunCommand(
                        () ->
                                Robot.shoulder.setManualOutput(
                                        Robot.operatorGamepad.shoulderManual()
                                                * OperatorConfig.slowModeScalar),
                        Robot.shoulder)
                .withName("OperatorManualSlowShoulder");
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
