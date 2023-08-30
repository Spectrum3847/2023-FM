// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.slide.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.slide.Slide;

public class SlideHoldPosition extends CommandBase {
    private double holdPosition = 0;
    /** Creates a new slideHoldPosition. */
    public SlideHoldPosition() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.slide);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        holdPosition = Robot.slide.getPosition();

        // Robot.slide.setEncoder(slide.config.maxExtension);
        // Robot.slide.setMMPosition(0);
        // moves slide down to lowest position
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double currentPosition = Robot.slide.getPosition();
        if (Math.abs(holdPosition - currentPosition) <= 5000) {
            Robot.slide.setMMPosition(holdPosition);
        } else {
            DriverStation.reportError(
                    "SlideHoldPosition tried to go too far away from current position. Current Position: "
                            + Slide.falconToInches(currentPosition)
                            + " || Hold Position: "
                            + Slide.falconToInches(holdPosition),
                    false);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.slide.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
