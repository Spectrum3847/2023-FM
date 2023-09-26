// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.swerve.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class FullTurn extends CommandBase {
    /** Creates a new FullTurn. */
    private double timeout = 2;

    private double startTime;
    private Command driveCommand;
    private boolean hasDriveEnded = false;

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
