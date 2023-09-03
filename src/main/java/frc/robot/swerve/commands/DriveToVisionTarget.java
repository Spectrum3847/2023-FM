// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.swerve.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.vision.VisionConfig;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class DriveToVisionTarget extends PIDCommand {
    /* Config settings */
    private static double kP = 0.06;
    private static double verticalSetpoint = -15; // neg
    private static double detectorVerticalSetpoint = -17; // neg
    private double tolerance = 1;
    private double horizontalOffset = 0; // positive is right (driver POV)

    private static double out = 0;
    private Command alignToTag;
    private Command conditionalCommand = null;
    private double conditionalVerticalSetpoint; 
    private boolean commandStarted = false;
    /**
     * Creates a new DriveToVisionTarget.
     * Aligns to a vision target in both X and Y axes (field-oriented). If used for automation
     * purposes, it is best to give it a timeout as a maximum timeout
     *
     * @param horizontalOffset adjustable offset in the Y axis in case robot isn't completely
     *     aligned. Default value should be 0
     * @param pipeline the pipeline to use for vision {@link VisionConfig}
     */
    public DriveToVisionTarget(double horizontalOffset, int pipeline) {
        super(
                // The controller that the command will use
                new PIDController(kP, 0, 0),
                // This should return the measurement
                () -> getVerticalOffset(),
                // This should return the setpoint (can also be a constant)
                () -> getVerticalSetpoint(pipeline),
                // This uses the output
                output -> {
                    setOutput(output);
                });
        this.horizontalOffset = horizontalOffset;
        alignToTag = getVisionTargetCommand(pipeline);
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(Robot.swerve);
        // Configure additional PID options by calling `getController` here.
        this.getController().setTolerance(tolerance);
    }

    /**
     * Creates a new DriveToVisionTarget.
     * Optionally add a command to run once the robot is a certain distance away from a target using vertical setpoints (ex: lower intake when ~1 meter away from target). 
     * The conditional command will end with the drive command if not stopped earlier
     * 
     *
     * @param horizontalOffset adjustable offset in the Y axis in case robot isn't completely
     *     aligned. Default value should be 0
     * @param pipeline the pipeline to use for vision {@link VisionConfig}
     * @param conditionalCommand command or sequence to run once the conditionalVerticalSetpoint has been reached
     * @param conditionalVerticalSetpoint vertical setpoint to run the conditional command at. (Limelight `ty` value)
     */
    public DriveToVisionTarget(double horizontalOffset, int pipeline, Command conditionalCommand, double conditionalVerticalSetpoint) {
        this(horizontalOffset, pipeline);
        this.conditionalCommand = conditionalCommand;
        this.conditionalVerticalSetpoint = conditionalVerticalSetpoint;
    }

    @Override
    public void initialize() {
        super.initialize();
        alignToTag.initialize();
        out = 0;
    }

    @Override
    public void execute() {
        super.execute();
        alignToTag.execute();
        if(conditionalCommand != null) { //TODO: review, maybe <=
            if(getVerticalOffset() >= conditionalVerticalSetpoint) {
                if(!commandStarted) {
                    conditionalCommand.initialize();
                    commandStarted = true;
                } else {
                    conditionalCommand.execute();
                }
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        alignToTag.end(interrupted);
        if(conditionalCommand != null && commandStarted == true) {
            conditionalCommand.end(interrupted);
        }
        Robot.swerve.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (Robot.vision.isDetectorPipeline()) {
            if (Math.floor(getVerticalOffset()) == detectorVerticalSetpoint + 1) {
                return true;
            }
        }
        // return Math.abs(out) <= 0.05;
        return false;
    }

    private static void setOutput(double output) {
        out = output;
        if (Math.abs(out) > 1) {
            out = 1 * Math.signum(out);
        }
        out = out * Robot.swerve.config.tuning.maxVelocity * 0.3;
    }

    public static double getOutput() {
        return out;
    }

    public AlignToVisionTarget getVisionTargetCommand(int pipeline) {
        // if detector, reverse output
        if (Robot.vision.isDetectorPipeline()) {
            return new AlignToVisionTarget(() -> -(getOutput() * 2), horizontalOffset, pipeline);
        }
        return new AlignToVisionTarget(() -> getOutput() * 2, horizontalOffset, pipeline);
    }

    public static double getVerticalSetpoint(int pipeline) {
        if (Robot.vision.isDetectorPipeline()) {
            return detectorVerticalSetpoint;
        }
        return verticalSetpoint;
    }

    private static double getVerticalOffset() {
        double offset = Robot.vision.getVerticalOffset();
        return (offset == 0) ? verticalSetpoint : offset;
    }
}
