// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shoulder.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.slide.Slide;

public class ShoulderDelay extends CommandBase {
    private double safePercent;
    private double finalPercent;
    private double conditionalPos;
    /**
     * Creates a new ShoulderDelay. Shoulder will move to safePos, wait for Elevator to be at
     * conditionalPos, then move to finalPos
     *
     * @param safePos Shoulder position that won't hit anything
     * @param finalPos position that Shoulder will go to after Elevator is at conditionalPos
     * @param conditionalPos position that Elevator must be at before Shoulder will move to finalPos
     */
    public ShoulderDelay(double safePercent, double finalPercent, double conditionalPos) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.safePercent = safePercent;
        this.finalPercent = finalPercent;
        this.conditionalPos = conditionalPos;
        addRequirements(Robot.shoulder);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // may need to be in execute
        Robot.shoulder.setMMPercent(safePercent);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (Robot.shoulder.getPosition() < Robot.shoulder.percentToFalcon(safePercent)) {
            Robot.shoulder.setMMPercent(safePercent);
        } else if (Robot.slide.getPosition() >= Slide.inchesToFalcon(conditionalPos)) {
            Robot.shoulder.setMMPercent(finalPercent);
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
}
