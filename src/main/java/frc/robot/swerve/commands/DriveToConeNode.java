package frc.robot.swerve.commands;

import java.util.LinkedList;
import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.vision.VisionConfig;

public class DriveToConeNode extends PIDCommand {
    /* Config settings */
    private static double kP = 0.5; // 0.8;
    private static double verticalSetpoint =
            -3.88; // These are different for each of our cone nodes. Get negative as we get closer
    // private static double minOutput =
    //      Robot.swerve.config.tuning.maxVelocity * 0.2; // Minimum value to output to motor
    private static double maxOutput = Robot.swerve.config.tuning.maxVelocity * 0.4;
    private double horizontalOffset = 0; // positive is right (driver POV)
    private static double tolerance = 0.0;
    private static final int batchSize = 10;
    private static double minimumPercentOfBatch = 0.5; // 50%

/** List to store a batch of {@link #batchSize} verticalOffset values. Command will end if more than {@link #minimumPercentOfBatch} of batch are below setpoint */
    private List<Double> batchedOffsets = new LinkedList<>(); 

    private static double out = 0;
    private Command alignToConeNode;
    private static final String m_limelight = VisionConfig.DEFAULT_LL;

    /**
     * Creates a new DriveToVisionTarget. Aligns to a vision target in both X and Y axes
     * (field-oriented). If used for automation purposes, it is best to give it a timeout as a
     * maximum timeout
     *
     * @param horizontalOffset adjustable offset in the Y axis in case robot isn't completely
     *     aligned. Default value should be 0
     * @param pipeline the pipeline to use for vision {@link VisionConfig}
     */
    public DriveToConeNode(double horizontalOffset) {
        super(
                // The controller that the command will use
                new PIDController(kP, 0, 0),
                // This should return the measurement
                () -> getVerticalOffset(),
                // This should return the setpoint (can also be a constant)
                () -> getVerticalSetpoint(),
                // This uses the output
                output -> {
                    setOutput(output);
                },
                Robot.swerve);
        this.horizontalOffset = horizontalOffset;
        alignToConeNode = getVisionTargetCommand();
        this.getController().setTolerance(tolerance);
    }

    @Override
    public void initialize() {
        super.initialize();
        out = 0;
        alignToConeNode.initialize();
    }

    @Override
    public void execute() {
        super.execute();
        // If we are already closer than the target distance stop driving.
        if (getVerticalOffset() <= getVerticalSetpoint() || !Robot.vision.isAimTarget()) {
            out = 0;
        }
        alignToConeNode.execute();
    }

    @Override
    public void end(boolean interrupted) {
        alignToConeNode.end(interrupted);
        Robot.swerve.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        double vertOffset = getVerticalOffset();

        batchedOffsets.add(vertOffset);

        // If the batch size is more than 10, remove the oldest offset
        if (batchedOffsets.size() > batchSize) {
            batchedOffsets.remove(0);
        }

        // Only proceed if there are at least 10 items in the batch
        if (batchedOffsets.size() < batchSize) {
            return false;
        }

        // Count the offsets that are below the setpoint
        int countBelowSetpoint = 0;
        for (double offset : batchedOffsets) {
            if (offset <= verticalSetpoint) {
                countBelowSetpoint++;
            }
        }

        // Calculate the proportion of offsets that are below the setpoint
        double percentBelowSetpoint = (double) countBelowSetpoint / batchedOffsets.size();

        // If the proportion is greater than the configured percentage, finish the command
        if (percentBelowSetpoint > minimumPercentOfBatch && Robot.vision.isAimTarget()) {
            String values = batchedOffsets.toString(); // Converts the list to a string in the format [x, x, x, ..., x]
            RobotTelemetry.print(String.format("Instant vertical setpoint at end: %.2f. %.2f%% of batch were below the setpoint. Values: %s", vertOffset, (percentBelowSetpoint * 100), values));            
            return true;
        }

        return false;
    }

    // If out is > 1 then cap at one, make the robot drive slow and still have bigger kP
    private static void setOutput(double output) {
        out = output;
        if (Math.abs(out) > 1) {
            out = 1 * Math.signum(out);
        }

        // Multiply the output times how fast we want the max speed to drive is.
        out = out * maxOutput;

        // If we are going to slow increase the speed to the min
        // if (Math.abs(out) < minOutput) {
        //     out = minOutput * Math.signum(out);
        // }
    }

    // We are driving negative and our Ty values are getting smaller so that works.
    public static double getOutput() {
        return out;
    }

    // Align and drive while we are doing this, pass output to it so it can drive forward.
    public AlignToConeNode getVisionTargetCommand() {
        return new AlignToConeNode(() -> getOutput(), horizontalOffset);
    }

    // Return where we are trying to get the target to be in the Y axis
    public static double getVerticalSetpoint() {
        return verticalSetpoint;
    }

    // Returns the measurement from the Limelight
    private static double getVerticalOffset() {
        double offset = Robot.vision.getVerticalOffset(m_limelight);
        return offset; // Why was this here? (offset == 0) ? verticalSetpoint : offset;
    }
}
