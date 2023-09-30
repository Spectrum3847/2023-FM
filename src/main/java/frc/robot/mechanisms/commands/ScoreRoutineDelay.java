// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.mechanisms.commands;

import java.util.Set;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;
import frc.robot.shoulder.Shoulder;
import frc.robot.shoulder.ShoulderConfig;

public class ScoreRoutineDelay extends CommandBase {
  /** Creates a new ScoreRoutine. */
  private final double maxTimeout = 1; //seconds

  private double targetShoulderPosition;
  private double startTime;
  private boolean shouldEnd = false;

  /**
   * Scoring routine delay for cones and cubes. If score routine button is pressed before shoulder is in scoring position, command will wait for the proper time or timeout with a set maximum: {@link #maxTimeout}
   * This command must be used along with the scoring command. 
   * Proper usage: new ScoreRoutineDelay(ScoreType.CONE).andThen(coneScoreRoutine());
   * @param type see {@link ScoreType}
   */
  public ScoreRoutineDelay(ScoreType type) {

    if(type == ScoreType.CUBE) {
      targetShoulderPosition = Shoulder.config.cubeUp;
    } else {
      targetShoulderPosition = Shoulder.config.coneTop;
    }

    // Use addRequirements() here to declare subsystem dependencies.
    // this.addRequirements(null);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    shouldEnd = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(Robot.shoulder.getPercentAngle() >= targetShoulderPosition) {
      shouldEnd = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  public enum ScoreType {
    CONE,
    CUBE
  }
}
