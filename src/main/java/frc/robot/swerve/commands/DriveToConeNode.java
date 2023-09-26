package frc.robot.swerve.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.auton.Auton;
import frc.robot.vision.VisionConfig;

public class DriveToConeNode extends PIDCommand {
    /* Config settings */
    private static double kP = 0.5; // 0.8;
    private static double verticalSetpoint =
            -3.88; // These are different for each of our cone nodes. Get negative as we get closer
    // private static double minOutput =
    //      Robot.swerve.config.tuning.maxVelocity * 0.2; // Minimum value to output to motor
    private static double maxOutput = Robot.swerve.config.tuning.maxVelocity * 0.3;
    private double horizontalOffset = 0; // positive is right (driver POV)

    private static double tolerance = 0.0;

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
        this.setName("DriveToConeNode");
    }

    @Override
    public void initialize() {
        super.initialize();
        out = 0;
        alignToConeNode.initialize();
        Auton.updateLog("Command Started", this.getName());
    }

    @Override
    public void execute() {
        super.execute();
        // If we are already closer than the target distance stop driving.
        if (getVerticalOffset() <= getVerticalSetpoint() || !Robot.vision.isAimTarget()) {
            out = 0;
        }
        alignToConeNode.execute();
        Auton.updateLog("Node Vert offset at execution: " + getVerticalOffset(), this.getName());
    }

    @Override
    public void end(boolean interrupted) {
        alignToConeNode.end(interrupted);
        Auton.updateLog(
                "Node Vert offset at end: "
                        + getVerticalOffset()
                        + " with goal of: "
                        + verticalSetpoint
                        + " || interrrupted: "
                        + interrupted,
                this.getName());
        Robot.swerve.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        // return Math.abs(out) <= 0.05;
        double vertoffset = getVerticalOffset();
        if (vertoffset <= verticalSetpoint && Robot.vision.isAimTarget()) {
            return true; // true;
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
