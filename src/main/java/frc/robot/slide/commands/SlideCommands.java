package frc.robot.slide.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Robot;
import frc.robot.slide.Slide;
import java.util.function.DoubleSupplier;

// above all copied from PilotCommands.java

public class SlideCommands {
    public static Trigger slideUp =
            new Trigger(() -> Slide.falconToInches(Robot.slide.getPosition()) > 12);

    public static void setupDefaultCommand() {
        Robot.slide.setDefaultCommand(
                stop().withTimeout(0.25)
                        .andThen(new SlideHoldPosition())
                        .withName("SlideDefaultCommand"));
    }

    public static void setupSlideTriggers() {
        slideUp = new Trigger(() -> Slide.falconToInches(Robot.slide.getPosition()) > 12);
    }

    public static Command stop() {
        return new RunCommand(() -> Robot.slide.stop(), Robot.slide).withName("SlideStop");
    }

    public static Command coastMode() {
        return new StartEndCommand(
                        () -> Robot.slide.setBrakeMode(false),
                        () -> Robot.slide.setBrakeMode(true),
                        Robot.slide)
                .ignoringDisable(true)
                .withName("SlideCoast");
    }

    public static Command setOutput(double value) {
        return new RunCommand(() -> Robot.slide.setManualOutput(value), Robot.slide)
                .withName("SlideOutput");
    }

    public static Command setOutput(DoubleSupplier value) {
        return new RunCommand(() -> Robot.slide.setManualOutput(value.getAsDouble()), Robot.slide)
                .withName("SlideOutput");
    }

    public static Command setPositionFromInches(double inches) {
        return new RunCommand(
                        () -> Robot.slide.setMMPosition(Slide.inchesToFalcon(inches)), Robot.slide)
                .withName("SlideSetPosition");
    }

    public static Command setPositionFromFalcon(double falcon) {
        return new RunCommand(() -> Robot.slide.setMMPosition(falcon), Robot.slide);
    }

    // full out
    public static Command fullExtend() {
        return setPositionFromFalcon(Slide.config.maxCarriageHeight)
                .withName("SlideFullExtend"); // TODO: change back to inches
    }

    // full in
    public static Command home() {
        return setPositionFromFalcon(300).withName("SlideHome");
    }

    public static Command zeroSlideRoutine() {
        return new ZeroSlideRoutine().withName("Zero Slide");
    }
}
