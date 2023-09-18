package frc.robot.shoulder.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Robot;
import frc.robot.shoulder.Shoulder;
import java.util.function.DoubleSupplier;

public class ShoulderCommands {

    public static Trigger isInitialized = new Trigger(() -> false);

    public static void setupElbowTriggers() {}

    public static void setupDefaultCommand() {
        Robot.shoulder.setDefaultCommand(
                new ShoulderHoldPosition().withName("ShoulderDefaultCommand"));
    }

    public static void setupShoulderTriggers() {
        isInitialized = new Trigger(() -> isInPosition());
        isInitialized.onTrue(
                new InstantCommand(
                                () -> {
                                    Robot.shoulder.setBrakeMode(true);
                                    Shoulder.isInitialized = true;
                                },
                                Robot.shoulder)
                        .ignoringDisable(true));
    }

    public static Command coastMode() {
        return new StartEndCommand(
                        () -> Robot.shoulder.setBrakeMode(false),
                        () -> Robot.shoulder.setBrakeMode(true),
                        Robot.shoulder)
                .ignoringDisable(true);
    }

    public static Command zeroShoulderRoutine() {
        return new ZeroShoulderRoutine().withName("ZeroShoulder");
    }

    public static Command setManualOutput(double speed) {
        return setManualOutput(speed);
    }

    public static Command setManualOutput(DoubleSupplier speed) {
        return new RunCommand(
                () -> Robot.shoulder.setManualOutput(speed.getAsDouble()), Robot.shoulder);
    }

    public static Command setMMPercent(double percent) {
        return new RunCommand(() -> Robot.shoulder.setMMPercent(percent), Robot.shoulder);
    }

    /* Misc Positions */

    public static Command stow() {
        return setMMPercent(Shoulder.config.stow);
    }

    /* Intake Positions */

    public static Command intake() {
        return setMMPercent(Shoulder.config.intake);
    }

    public static Command airIntake() {
        return setMMPercent(Shoulder.config.airIntake);
    }

    public static Command shelfIntake() {
        return setMMPercent(Shoulder.config.shelfIntake);
    }

    /* Scoring Positions */

    public static Command home() {
        return setMMPercent(Shoulder.config.home);
    }

    public static Command floor() {
        return setMMPercent(Shoulder.config.floor);
    }

    public static Command prescore() {
        return setMMPercent(Shoulder.config.prescore);
    }

    public static Command launch() {
        return setMMPercent(Shoulder.config.launch);
    }

    public static Command coneTop() {
        return setMMPercent(Shoulder.config.coneTop);
    }

    public static Command coneMid() {
        return setMMPercent(Shoulder.config.coneMid);
    }

    public static Command cubeUp() {
        return setMMPercent(Shoulder.config.cubeUp);
    }

    public static Command setMMPosition(double position) {
        return new RunCommand(() -> Robot.shoulder.setMMPosition(position), Robot.shoulder);
    }

    public static Command stop() {
        return new RunCommand(() -> Robot.shoulder.stop(), Robot.shoulder);
    }

    public static boolean isInPosition() {
        return !Shoulder.isInitialized
                && Robot.shoulder.getPercentAngle() >= Shoulder.config.initializedPosition;
    }
}
