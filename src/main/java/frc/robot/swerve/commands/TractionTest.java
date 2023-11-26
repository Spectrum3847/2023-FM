// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.swerve.commands;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class TractionTest extends CommandBase {
    /** Creates a new TractionTest. */
    Command driveCommand;

    double startingCurrentLimit = 10;
    double startingtime = 0; // in seconds
    SupplyCurrentLimitConfiguration currentLimit;
    SupplyCurrentLimitConfiguration lowLimit = new SupplyCurrentLimitConfiguration(true, 5, 5, 0.1);
    SupplyCurrentLimitConfiguration[] currentLimits = {lowLimit, lowLimit, lowLimit, lowLimit};

    public TractionTest() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

        // set current limit to 10 amps
        currentLimit =
                new SupplyCurrentLimitConfiguration(
                        true, startingCurrentLimit, startingCurrentLimit, 0.1);
        currentLimits[0] = currentLimit;
        Robot.swerve.setDriveCurrentLimit(currentLimits);

        driveCommand =
                new SwerveDrive(() -> Robot.swerve.config.tuning.maxVelocity, () -> 0.0, () -> 0.0)
                        .withTimeout(2);
        startingtime = Timer.getFPGATimestamp();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        /*run every 2 seconds */
        if (Timer.getFPGATimestamp() - startingtime > 2) {
            Robot.swerve.stop();
            startingCurrentLimit += 5;
            startingtime = Timer.getFPGATimestamp();
        }
        // set current limit to 10 amps
        SupplyCurrentLimitConfiguration currentLimit =
                new SupplyCurrentLimitConfiguration(
                        true, startingCurrentLimit, startingCurrentLimit, 0.1);
        currentLimits[0] = currentLimit;
        Robot.swerve.setDriveCurrentLimit(currentLimits);

        driveCommand.execute();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.swerve.stop();
        // set current limit to config value
        SupplyCurrentLimitConfiguration currentLimit =
                new SupplyCurrentLimitConfiguration(true, 40, 40, 0.1);
        SupplyCurrentLimitConfiguration[] currentLimits = {
            currentLimit, currentLimit, currentLimit, currentLimit
        };
        Robot.swerve.setDriveCurrentLimit(currentLimits);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {

        /*return true if any of the 4 module wheel velocities are greater than 5 m/s */
        double[] wheelSpeeds = Robot.swerve.getWheelSpeeds();
        for (double speed : wheelSpeeds) {
            if (speed > 5) {
                return true;
            }
        }
        return false;
    }
}
