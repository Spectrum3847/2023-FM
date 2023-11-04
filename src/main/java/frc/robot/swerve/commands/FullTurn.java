// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.swerve.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class FullTurn extends Command {
    private double timeout = 2; // seconds

    private double startTime;
    private Command driveCommand;
    private boolean hasDriveEnded = false;

    /**
     * Turn the robot as fast as possible to attempt to remove any stuck gamepieces. This command
     * will automatically time out at {@link #timeout} seconds if not stopped sooner.
     */
    public FullTurn() {
        // Use addRequirements() here to declare subsystem dependencies.
        driveCommand = new SwerveDrive(() -> 0, () -> 0, () -> 100).withName("SwerveFullTurn");
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
        hasDriveEnded = false;
        driveCommand.initialize();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (Timer.getFPGATimestamp() - startTime < timeout && !hasDriveEnded) {
            driveCommand.execute();
        } else if (!hasDriveEnded) {
            driveCommand.end(false);
            hasDriveEnded = true;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        if (!hasDriveEnded) {
            driveCommand.end(interrupted);
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
