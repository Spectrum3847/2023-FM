// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.swerve.commands;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class TractionTest extends CommandBase {
    /** Creates a new TractionTest. */
    Command driveCommand;

    int swerveModule = 3;
    double mxCurr = 0;
    double startingCurrentLimit = 10;
    double startingtime = 0; // in seconds
    SupplyCurrentLimitConfiguration currentLimit;
    SupplyCurrentLimitConfiguration lowLimit = new SupplyCurrentLimitConfiguration(true, 0, 0, 0.0);
    SupplyCurrentLimitConfiguration[] currentLimits = {lowLimit, lowLimit, lowLimit, lowLimit};

    public TractionTest() {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        log();

        // set current limit to 10 amps
        currentLimit =
                new SupplyCurrentLimitConfiguration(
                        true, startingCurrentLimit, startingCurrentLimit, 0.1);

        Robot.swerve.mSwerveMods[swerveModule].mDriveMotor.configSupplyCurrentLimit(currentLimit);

        driveCommand =
                new SwerveDrive(() -> -1, () -> 0.0, () -> 0.0, () -> 1, () -> false, true)
                        .withTimeout(2);
        startingtime = Timer.getFPGATimestamp();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (Math.abs(Robot.swerve.mSwerveMods[swerveModule].mDriveMotor.getSelectedSensorVelocity())
                < 400) {
            mxCurr =
                    Math.max(
                            mxCurr,
                            Robot.swerve.mSwerveMods[swerveModule].mDriveMotor.getSupplyCurrent());
        }
        log();
        /*run every 2 seconds */
        if (Timer.getFPGATimestamp() - startingtime > 5) {
            Robot.swerve.stop();
            startingCurrentLimit += 1;
            startingtime = Timer.getFPGATimestamp();
            currentLimit =
                    new SupplyCurrentLimitConfiguration(
                            true, startingCurrentLimit, startingCurrentLimit, 0.1);
            Robot.swerve.mSwerveMods[swerveModule].mDriveMotor.configSupplyCurrentLimit(
                    currentLimit);
        }
        // SmartDashboard.putString("Supply Current",
        // Robot.swerve.mSwerveMods[swerveModule].mDriveMotor.configGetSupplyCurrentLimit(currentLimit) );

        // set current limit to 10 amps

        driveCommand.execute();
    }

    public void log() {
        SmartDashboard.putNumber("current limit", startingCurrentLimit);
        // add wheel speed and supply current for all 4 modules:
        SmartDashboard.putNumber(
                "WheelSpeed Mod 0",
                Robot.swerve.mSwerveMods[0].mDriveMotor.getSelectedSensorVelocity());
        SmartDashboard.putNumber(
                "supply Current 0", Robot.swerve.mSwerveMods[0].mDriveMotor.getSupplyCurrent());
        SmartDashboard.putNumber(
                "WheelSpeed Mod 1",
                Robot.swerve.mSwerveMods[1].mDriveMotor.getSelectedSensorVelocity());
        SmartDashboard.putNumber(
                "supply Current 1", Robot.swerve.mSwerveMods[1].mDriveMotor.getSupplyCurrent());
        SmartDashboard.putNumber(
                "WheelSpeed Mod 2",
                Robot.swerve.mSwerveMods[2].mDriveMotor.getSelectedSensorVelocity());
        SmartDashboard.putNumber(
                "supply Current 2", Robot.swerve.mSwerveMods[2].mDriveMotor.getSupplyCurrent());
        SmartDashboard.putNumber(
                "WheelSpeed Mod 3",
                Robot.swerve.mSwerveMods[3].mDriveMotor.getSelectedSensorVelocity());
        SmartDashboard.putNumber(
                "supply Current 3", Robot.swerve.mSwerveMods[3].mDriveMotor.getSupplyCurrent());

        SmartDashboard.putNumber("Max Current", mxCurr);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.swerve.stop();
        // set current limit to config value\
        startingCurrentLimit = 10;
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
        return false;
    }
}
