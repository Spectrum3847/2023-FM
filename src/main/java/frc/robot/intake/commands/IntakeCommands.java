package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Robot;
import frc.robot.intake.Intake;

public class IntakeCommands {

    public static void setupDefaultCommand() {

        Robot.intake.setDefaultCommand(slowIntake().withName("IntakeDefaultCommand"));
    }

    public static Command slowIntake() {
        return setVelocities(Intake.config.slowIntake).withName("SlowIntake");
    }

    public static Command intake() {
        return setVelocities(Intake.config.intake).withName("Intake");
    }

    public static Command eject() {
        return setVelocities(Intake.config.eject).withName("Eject");
    }

    public static Command drop() {
        return setVelocities(Intake.config.drop).withName("Drop");
    }

    public static Command setVelocities(double velocity) {
        return new RunCommand(() -> Robot.intake.setVelocities(velocity), Robot.intake)
                .withName("IntakeSetVelocity");
    }

    public static Command setIntakeRollers(double percent) {
        return new RunCommand(() -> Robot.intake.setPercentOutputs(percent), Robot.intake)
                .withName("IntakeSetPercentage");
    }

    public static Command stopAllMotors() {
        return new RunCommand(() -> Robot.intake.stopAll(), Robot.intake).withName("IntakeStop");
    }
}
