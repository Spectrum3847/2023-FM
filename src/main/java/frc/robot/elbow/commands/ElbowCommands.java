package frc.robot.elbow.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.Robot;
import frc.robot.elbow.Elbow;
import java.util.function.DoubleSupplier;

public class ElbowCommands {
    // public static Trigger isInitialized = new Trigger(() -> false);

    public static void setupDefaultCommand() {
        Robot.elbow.setDefaultCommand(new ElbowHoldPosition().withName("ElbowDefaultCommand"));
    }

    public static void setupElbowTriggers() {
        // isInitialized = new Trigger(() -> isInPosition());
        // isInitialized.onTrue(
        //         new InstantCommand(
        //                         () -> {
        //                             Robot.elbow.setBrakeMode(true);
        //                             Elbow.isInitialized = true;
        //                         },
        //                         Robot.elbow)
        //                 .ignoringDisable(true));
    }

    public static Command coastMode() {
        return new StartEndCommand(
                        () -> Robot.elbow.setBrakeMode(false),
                        () -> Robot.elbow.setBrakeMode(true),
                        Robot.elbow)
                .ignoringDisable(true)
                .withName("ElbowCoastMode");
    }

    public static Command zeroElbowRoutine() {
        return new ZeroElbowRoutine().withName("ZeroElbow");
    }

    public static Command setManualOutput(double speed) {
        return setManualOutput(speed);
    }

    public static Command setManualOutput(DoubleSupplier speed) {
        return new RunCommand(() -> Robot.elbow.setManualOutput(speed.getAsDouble()), Robot.elbow);
    }

    public static Command setMMPercent(double percent) {
        return new RunCommand(() -> Robot.elbow.setMMPercent(percent), Robot.elbow);
    }

    public static Command raiseByPercent(double percent) {
        return new RunCommand(
                () -> {
                    double test = Robot.elbow.falconToPercent(Robot.elbow.getPosition()) + percent;
                    System.out.print(
                            "at " + Robot.elbow.falconToPercent(Robot.elbow.getPosition()));
                    System.out.println(" || go to " + test);
                    Robot.elbow.setMMPercent(test);
                },
                Robot.elbow);
    }

    /* Misc Positions */
    public static Command stow() {
        return setMMPercent(Elbow.config.stow);
    }

    /* Intaking Positions */

    public static Command intake() {
        return setMMPercent(Elbow.config.intake);
    }

    public static Command airIntake() {
        return setMMPercent(Elbow.config.airIntake);
    }

    public static Command shelfIntake() {
        return setMMPercent(Elbow.config.shelfIntake);
    }

    /* Scoring Positions */

    public static Command home() {
        return setMMPercent(Elbow.config.home);
    }

    public static Command floor() {
        return setMMPercent(Elbow.config.floor);
    }

    public static Command score() {
        return setMMPercent(Elbow.config.scorePos);
    }

    public static Command coneTop() {
        return setMMPercent(Elbow.config.coneTop);
    }

    public static Command coneMid() {
        return setMMPercent(Elbow.config.coneMid);
    }

    public static Command cubeUp() {
        return setMMPercent(Elbow.config.cubeUp);
    }

    public static Command raiseBy(double percent) {
        return raiseByPercent(percent); // +percent lowers elbow
    }

    public static Command setMMPosition(double position) {
        return new RunCommand(() -> Robot.elbow.setMMPosition(position), Robot.elbow);
    }

    public static Command stop() {
        return new RunCommand(() -> Robot.elbow.stop(), Robot.elbow);
    }

    public static boolean isInPosition() {
        return !Elbow.isInitialized
                && Robot.elbow.getPercentAngle() >= Elbow.config.initializedPosition;
    }
}
