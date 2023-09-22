// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.swerve.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.vision.VisionConfig;
import java.util.function.DoubleSupplier;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AlignToConeNode extends PIDCommand {

    private static double kp = 0.025;
    private static double tolerance = 0.01;
    private static double maxOutput = Robot.swerve.config.tuning.maxVelocity * 0.5;
    SwerveDrive driveCommand;
    DoubleSupplier fwdPositiveSupplier;
    private static double out = 0;
    private int pipelineIndex = VisionConfig.reflectivePipeline;
    private double heading = Math.PI;
    private final String m_limelight = VisionConfig.DEFAULT_LL;

    /**
     * Creates a new AlignToVisionTarget command that aligns to a vision target (apriltag,
     * retroreflective tape, detector target) on the Field Oriented X-axis.
     *
     * @param limelight {@link VisionConfig}
     * @param fwdPositiveSupplier
     * @param offset
     * @param pipeline
     */
    public AlignToConeNode(DoubleSupplier fwdPositiveSupplier, double offset) {
        super(
                // The controller that the command will use
                new PIDController(kp, 0, 0),
                // This should return the measurement
                () -> Robot.vision.getHorizontalOffset(VisionConfig.DEFAULT_LL),
                // This should return the setpoint (can also be a constant)
                () -> offset,
                // This uses the output
                output -> setOutput(output),
                Robot.swerve);

        this.getController().setTolerance(tolerance);
        driveCommand =
                new SwerveDrive(
                        fwdPositiveSupplier, // Allows pilot to drive fwd and rev
                        () -> getOutput(), // Moves us center to the tag
                        () -> getSteering(), // Aligns to grid
                        () -> 1.0, // full velocity
                        () -> getFieldRelative()); // Field relative is true
        // Use addRequirements() here to declare subsystem dependencies.
        // Configure additional PID options by calling `getController` here.
        this.setName("AlignToVisionTarget");
    }

    public double getSteering() {
        // // if customizable heading is set, rotate to that heading
        // if (heading != Integer.MIN_VALUE) {
        //     return Robot.swerve.calculateRotationController(() -> heading);
        // }

        // // dont set rotation on detector pipelines
        // if (pipelineIndex > 2) {
        //     return 0;
        // }
        return Robot.swerve.calculateRotationController(() -> Math.PI);
    }

    public boolean getFieldRelative() {
        // drive robot oriented if on detector pipelines
        // if (Robot.vision.isDetectorPipeline(m_limelight)) {
        //     return false;
        // }
        return true;
    }

    public static double getOutput() {
        return out;
    }

    public static void setOutput(double output) {
        out = -1 * output; // Flip the output because camera is looking back at driver station.
        if (Math.abs(out) > 1) {
            out = 1 * Math.signum(out);
        }

        out = out * maxOutput;
    }

    @Override
    public void initialize() {
        super.initialize();
        out = 0;
        // getLedCommand(tagID).initialize();
        Robot.swerve.resetRotationController();
        driveCommand.initialize();
        Robot.vision.setLimelightPipeline(m_limelight, pipelineIndex);
    }

    @Override
    public void execute() {
        super.execute();
        if (this.getController().atSetpoint() || !Robot.vision.isAimTarget()) {
            out = 0;
        }
        driveCommand.execute();
    }

    @Override
    public void end(boolean interrupted) {
        // Robot.vision.setLimelightPipeline(VisionConfig.aprilTagPipeline);
        // getLedCommand(tagID).end(interrupted);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
