package frc.robot.test.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;

// Uses the Pilot start/menu button to move on to the next method
public class ConfirmCommand extends Command {
    private String text;

    /** Creates a new ConfirmCommand. */
    public ConfirmCommand(String text) {
        this.text = text;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        RobotTelemetry.print(text);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return Robot.pilotGamepad.gamepad.startButton.getAsBoolean();
    }
}
