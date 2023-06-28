package frc.robot.elbow.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.elbow.Elbow;

public class ZeroElbowRoutine extends CommandBase {
    /** Creates a new ZeroElbow. */
    public ZeroElbowRoutine() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.elbow);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // Turn off soft limits
        Robot.elbow.softLimitsFalse();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Set Elbow to slowly lower
        Robot.elbow.setManualOutput(Elbow.config.zeroSpeed);
        Robot.elbow.zeroElbow();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        // Set Elbow position to zero
        // enable soft limits
        Robot.elbow.resetSensorPosition(-1100);
        Robot.elbow.softLimitsTrue();
        Robot.elbow.setMMPosition(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
