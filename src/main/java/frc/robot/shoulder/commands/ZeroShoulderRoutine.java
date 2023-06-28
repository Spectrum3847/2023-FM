// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shoulder.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.shoulder.Shoulder;

public class ZeroShoulderRoutine extends CommandBase {
    /** Creates a new ZeroShoulder. */
    public ZeroShoulderRoutine() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.shoulder);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // Turn off soft limits
        Robot.shoulder.softLimitsFalse();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Set Shoulder to slowly lower
        Robot.shoulder.setManualOutput(Shoulder.config.zeroSpeed);
        Robot.shoulder.zeroShoulder();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        // Set Shoulder position to zero
        // enable soft limits
        Robot.shoulder.resetSensorPosition(-1100);
        Robot.shoulder.softLimitsTrue();
        Robot.shoulder.setMMPosition(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
