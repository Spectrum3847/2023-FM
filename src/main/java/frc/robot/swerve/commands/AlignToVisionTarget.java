// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.swerve.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.leds.commands.LEDCommands;
import frc.robot.leds.commands.OneColorLEDCommand;
import frc.robot.vision.VisionConfig;
import java.util.function.DoubleSupplier;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AlignToVisionTarget extends PIDCommand {

    private static double lowKP = 0.035;
    private static double highKP = 0.06;
    private static double tolerance = 2;
    SwerveDrive driveCommand;
    DoubleSupplier fwdPositiveSupplier;
    private static double out = 0;
    private int pipelineIndex = 0;
    private double heading = Integer.MIN_VALUE;
    private String m_limelight = VisionConfig.DEFAULT_LL;

    /**
     * Creates a new AlignToVisionTarget command that aligns to a vision target (apriltag,
     * retroreflective tape, detector target) on the Field Oriented X-axis.
     *
     * @param limelight {@link VisionConfig}
     * @param fwdPositiveSupplier
     * @param offset
     * @param pipeline
     */
    public AlignToVisionTarget(
            String limelight, DoubleSupplier fwdPositiveSupplier, double offset, int pipeline) {
        super(
                // The controller that the command will use
                new PIDController(lowKP, 0, 0),
                // This should return the measurement
                () -> Robot.vision.getHorizontalOffset(limelight),
                // This should return the setpoint (can also be a constant)
                () -> offset,
                // This uses the output
                output -> setOutput(output),
                Robot.swerve);

        this.getController().setTolerance(tolerance);
        this.pipelineIndex = pipeline;
        driveCommand =
                new SwerveDrive(
                        fwdPositiveSupplier, // Allows pilot to drive fwd and rev
                        () -> (getOutput() * 2), // Moves us center to the tag
                        () -> getSteering(), // Aligns to grid
                        () -> 1.0, // full velocity
                        () -> getFieldRelative()); // Field relative is true
        // Use addRequirements() here to declare subsystem dependencies.
        // Configure additional PID options by calling `getController` here.
        this.setName("AlignToVisionTarget");
        m_limelight = limelight;
    }

    /**
     * Creates a new AlignToVisionTarget command that aligns to a vision target (apriltag,
     * retroreflective tape, detector target) on the Field Oriented X-axis. Adds customizable
     * heading to the command (radians)
     *
     * @param fwdPositiveSupplier
     * @param offset
     * @param pipeline
     * @param heading rotation the robot will face
     */
    public AlignToVisionTarget(
            String limelight,
            DoubleSupplier fwdPositiveSupplier,
            double offset,
            int pipeline,
            double heading) {
        this(limelight, fwdPositiveSupplier, offset, pipeline);
        this.heading = heading;
    }

    public double getSteering() {
        // if customizable heading is set, rotate to that heading
        if (heading != Integer.MIN_VALUE) {
            return Robot.swerve.calculateRotationController(() -> heading);
        }

        // dont set rotation on detector pipelines
        if (pipelineIndex > 2) {
            return 0;
        }
        return Robot.swerve.calculateRotationController(() -> Math.PI);
    }

    public boolean getFieldRelative() {
        // drive robot oriented if on detector pipelines
        if (Robot.vision.isDetectorPipeline(m_limelight)) {
            return false;
        }
        return true;
    }

    public double getOutput() {
        // If there are no targets don't move
        if (Robot.vision.isTarget(m_limelight)) {
            // reverse direction for robot pov
            if (Robot.vision.isDetectorPipeline(m_limelight)) {
                return -out;
            }
            return out;
        }
        RobotTelemetry.print("No Targets: Output Off");
        return 0;
    }

    public static void setOutput(double output) {
        out = -1 * output;
        if (Math.abs(out) > 1) {
            out = 1 * Math.signum(out);
        }

        out = out * Robot.swerve.config.tuning.maxVelocity * 0.3;
    }

    private void setKp() {
        if (Robot.vision.getVerticalOffset(m_limelight) > 16) {
            this.getController().setP(highKP);
        } else {
            this.getController().setP(lowKP);
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        out = 0;
        // getLedCommand(tagID).initialize();
        Robot.swerve.resetRotationController();
        driveCommand.initialize();
        setKp();

        Robot.vision.setLimelightPipeline(m_limelight, pipelineIndex);
    }

    @Override
    public void execute() {
        super.execute();
        driveCommand.execute();
        setKp();
        // RobotTelemetry.print("Aim Target? = " + Robot.vision.isAimTarget());
        // RobotTelemetry.print("Detect Target? = " + Robot.vision.isDetetTarget());
        // getLedCommand(tagID).execute();
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

    private Command getLedCommand() {
        double tagID = Robot.vision.getClosestTagID();
        switch ((int) tagID) {
            case 1:
            case 6:
                return LEDCommands.leftGrid();
            case 2:
            case 7:
                return LEDCommands.midGrid();
            case 3:
            case 8:
                return LEDCommands.rightGrid();
        }
        return new OneColorLEDCommand((2 / 3), 1, new Color(130, 103, 185), "Full Wrong", 80);
    }
}
