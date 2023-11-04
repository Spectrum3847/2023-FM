// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.slide.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.slide.Slide;

public class ZeroSlideRoutine extends Command {
    /** Creates a new Zeroslide. */
    public ZeroSlideRoutine() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.slide);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // Turn off soft limits
        Robot.slide.softLimitsFalse();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Set slide to slowly lower
        Robot.slide.setManualOutput(Slide.config.zeroSpeed);
        Robot.slide.zeroSlide();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        // Set slide position to zero
        // enable soft limits
        Robot.slide.resetSensorPosition(0);
        Robot.slide.softLimitsTrue();
        Robot.slide.setMMPosition(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
