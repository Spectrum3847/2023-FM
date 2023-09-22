package frc.robot.elbow.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ElbowHoldPosition extends CommandBase {
    double position = 0;

    /** Creates a new ElbowHoldPosition. */
    public ElbowHoldPosition() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.elbow);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.shoulder.stop();
        position = Robot.elbow.getPosition();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.elbow.setMMPosition(position);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.elbow.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
