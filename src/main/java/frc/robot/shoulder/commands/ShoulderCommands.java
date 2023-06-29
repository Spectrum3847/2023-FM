package frc.robot.shoulder.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.Robot;
import frc.robot.shoulder.Shoulder;
import java.util.function.DoubleSupplier;

public class ShoulderCommands {
    public static void setupDefaultCommand() {
        Robot.shoulder.setDefaultCommand(
                new ShoulderHoldPosition().withName("ShoulderDefaultCommand"));
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

    public static Command coneIntake() {
        return setMMPercent(Shoulder.config.coneIntake);
    }

    public static Command coneStandingIntake() {
        return setMMPercent(Shoulder.config.coneStandingIntake);
    }

    public static Command airConeIntake() {
        return setMMPercent(Shoulder.config.airConeIntake);
    }

    public static Command airCubeIntake() {
        return setMMPercent(Shoulder.config.airCubeIntake);
    }

    public static Command topConeIntake() {
        return setMMPercent(Shoulder.config.topConeIntake);
    }

    public static Command topCubeIntake() {
        return setMMPercent(Shoulder.config.topCubeIntake);
    }

    public static Command coneFloorGoal() {
        return setMMPercent(Shoulder.config.coneHybrid);
    }

    public static Command floorScore() {
        return setMMPercent(Shoulder.config.floorScore);
    }

    public static Command coneMid() {
        return setMMPercent(Shoulder.config.coneMid);
    }

    public static Command coneTop() {
        return setMMPercent(Shoulder.config.coneTop);
    }

    public static Command coneShelf() {
        return setMMPercent(Shoulder.config.coneShelf);
    }

    public static Command cubeIntake() {
        return setMMPercent(Shoulder.config.cubeIntake);
    }

    public static Command cubeFloorGoal() {
        return setMMPercent(Shoulder.config.cubeHybrid);
    }

    public static Command cubeMid() {
        return setMMPercent(Shoulder.config.cubeMid);
    }

    public static Command cubeTop() {
        return setMMPercent(Shoulder.config.cubeTop);
    }

    public static Command home() {
        return setMMPercent(0);
    }

    public static Command autonHome() {
        return setMMPercent(1);
    }

    public static Command setMMPosition(double position) {
        return new RunCommand(() -> Robot.shoulder.setMMPosition(position), Robot.shoulder);
    }

    public static Command stop() {
        return new RunCommand(() -> Robot.shoulder.stop(), Robot.shoulder);
    }
}
