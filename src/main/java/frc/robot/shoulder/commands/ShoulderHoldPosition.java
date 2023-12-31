// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shoulder.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ShoulderHoldPosition extends CommandBase {
    double position = 0;

    /** Creates a new ShoulderHoldPosition. */
    public ShoulderHoldPosition() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.shoulder);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Robot.shoulder.stop();
        position = Robot.shoulder.getPosition();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Robot.shoulder.setMMPosition(position);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.shoulder.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
