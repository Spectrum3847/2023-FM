package frc.robot.elbow.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.Robot;
import frc.robot.elbow.Elbow;
import java.util.function.DoubleSupplier;

public class ElbowCommands {
    public static void setupDefaultCommand() {
        Robot.elbow.setDefaultCommand(new ElbowHoldPosition().withName("ElbowDefaultCommand"));
    }

    public static Command coastMode() {
        return new StartEndCommand(
                        () -> Robot.elbow.setBrakeMode(false),
                        () -> Robot.elbow.setBrakeMode(true),
                        Robot.elbow)
                .ignoringDisable(true);
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

    public static Command coneIntake() {
        return setMMPercent(Elbow.config.coneIntake);
    }

    public static Command airConeIntake() {
        return setMMPercent(Elbow.config.airConeIntake);
    }

    public static Command airCubeIntake() {
        return setMMPercent(Elbow.config.airCubeIntake);
    }

    public static Command topConeIntake() {
        return setMMPercent(Elbow.config.topConeIntake);
    }

    public static Command topCubeIntake() {
        return setMMPercent(Elbow.config.topCubeIntake);
    }

    public static Command coneStandingIntake() {
        return setMMPercent(Elbow.config.coneStandingIntake);
    }

    public static Command coneFloorGoal() {
        return setMMPercent(Elbow.config.coneHybrid);
    }

    public static Command coneMid() {
        return setMMPercent(Elbow.config.coneMid);
    }

    public static Command coneTop() {
        return setMMPercent(Elbow.config.coneTop);
    }

    public static Command coneShelf() {
        return setMMPercent(Elbow.config.coneShelf);
    }

    public static Command cubeIntake() {
        return setMMPercent(Elbow.config.cubeIntake);
    }

    public static Command cubeFloorGoal() {
        return setMMPercent(Elbow.config.cubeHybrid);
    }

    public static Command cubeMid() {
        return setMMPercent(Elbow.config.cubeMid);
    }

    public static Command cubeTop() {
        return setMMPercent(Elbow.config.cubeTop);
    }

    public static Command home() {
        return setMMPercent(0);
    }

    public static Command autonHome() {
        return setMMPercent(1);
    }

    public static Command setMMPosition(double position) {
        return new RunCommand(() -> Robot.elbow.setMMPosition(position), Robot.elbow);
    }

    public static Command stop() {
        return new RunCommand(() -> Robot.elbow.stop(), Robot.elbow);
    }
}
