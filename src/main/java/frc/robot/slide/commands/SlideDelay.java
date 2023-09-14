// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.slide.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.slide.Slide;
import frc.robot.slide.SlideConfig;

public class SlideDelay extends CommandBase {
    private double safePos;
    private double finalPos;
    private double conditionalPercent;
    private boolean simpleDelay;

    /**
     * Homes the slide and stops after {@link SlideConfig#homeTimeout} once it reaches a {@link
     * SlideConfig#homeThreshold} from 0. Will also set the slide to be at 0 if it has taken too
     * long for the command to go home (0 position has changed and the slide is stalling)
     */
    private boolean isGoingHome;

    private boolean reachedThreshold;
    private double timestamp;
    /**
     * Creates a new slideDelay. slide will move to safePos, wait for FourBar to be at conditional
     * percentage, then move to finalPos
     *
     * @param safePos Falcon Units: slide position that won't hit anything
     * @param finalPos Falcon Units: position that slide will go to after FourBar is at
     *     conditionalPercent
     * @param conditionalPercent percentage that FourBar must be at before slide will move to
     *     finalPos ex: 40% = 40 not .40
     */
    public SlideDelay(double safePos, double finalPos, int conditionalPercent) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.safePos = safePos;
        this.finalPos = finalPos;
        this.conditionalPercent = conditionalPercent;
        this.simpleDelay = false;
        homeCheck(finalPos);
        this.setName("SlideDelay");
        addRequirements(Robot.slide);
    }

    /**
     * Creates a new slideDelay. Wait for FourBar to be at conditional percentage, then move to
     * finalPos
     *
     * @param finalPos Falcon Units: position that slide will go to after FourBar is at
     *     conditionalPercent
     * @param conditionalPercent percentage that FourBar must be at before slide will move to
     *     finalPos ex: 40% = 40 not .40
     */
    public SlideDelay(double finalPos, int conditionalPercent) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.finalPos = finalPos;
        this.conditionalPercent = conditionalPercent;
        this.simpleDelay = true;
        homeCheck(finalPos);
        this.setName("SlideDelay");
        addRequirements(Robot.slide);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        homeCheck(finalPos);
    }

    /* Called every time the scheduler runs while the command is scheduled.
         *
         * if slide is going down and fourbar is out too far and slide is greater than safepos then
    go to safe position
         * otherwise just go straight down
         */
    @Override
    public void execute() {
        if (simpleDelay) {
            if (Robot.shoulder.getPosition() < Robot.shoulder.percentToFalcon(conditionalPercent)) {
                Robot.slide.setMMPosition(finalPos);
            }
        } else {
            if (Robot.slide.getPosition() > finalPos
                    && Robot.shoulder.getPosition()
                            > Robot.shoulder.percentToFalcon(conditionalPercent)
                    && Robot.slide.getPosition() > safePos) {
                Robot.slide.setMMPosition(safePos);

            } else {
                Robot.slide.setMMPosition(finalPos);
            }
        }

        if (isGoingHome) {
            if (Robot.slide.getPosition() <= Slide.config.homeThreshold && !reachedThreshold) {
                reachedThreshold = true;
                timestamp = Timer.getFPGATimestamp();
            }
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
        // if this is an home command
        if (isGoingHome) {
            double timeElapsed = Timer.getFPGATimestamp() - timestamp;

            /* if the slide is past the threshold and has run for half a second more */
            if (reachedThreshold && timeElapsed >= Slide.config.homeTimeout) {
                return true;
            } else {
                return false;
            }

            /* if the slide has been running for more time than it possibly needs to go home
            (4 seconds) */
            // if (reachedThreshold && timeElapsed >= slide.config.homeTimeout) {
            //     RobotTelemetry.print("homing should end");
            //     return true;
            // } else if (timeElapsed >= slide.config.maxHomeTimeout) {
            //     RobotTelemetry.print("slide reset");
            //     Robot.slide.resetSensorPosition(0);
            //     return true;
            // } else {
            //     return false;
            // }

        } else {
            return false;
        }
    }

    private void homeCheck(double finalPos) {
        reachedThreshold = false;
        timestamp = 0;
        if (finalPos == 0) {
            isGoingHome = true;

        } else {
            isGoingHome = false;
        }
    }
}
