package frc.robot.swerve.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.auton.Auton;
import frc.robot.vision.VisionConfig;

public class DriveToConeFloor extends PIDCommand {
    /* Config settings */
    private static double kP = 0.6; // 0.8;
    private static double verticalSetpoint = -2.5; // numbers get small as the cone gets closer

    private static double maxConeInView = 6;
    private static double maxOutput = Robot.swerve.config.tuning.maxVelocity * 1;
    private double horizontalOffset = 0; // positive is right (driver POV)

    private static double tolerance = 0.05;

    private static double out = 0;
    private Command alignToConeFloor;
    private static final String m_limelight = VisionConfig.DETECT_LL;

    /**
     * Creates a new DriveToVisionTarget. Aligns to a vision target in both X and Y axes
     * (field-oriented). If used for automation purposes, it is best to give it a timeout as a
     * maximum timeout
     *
     * @param horizontalOffset adjustable offset in the Y axis in case robot isn't completely
     *     aligned. Default value should be 0
     * @param pipeline the pipeline to use for vision {@link VisionConfig}
     */
    public DriveToConeFloor(double horizontalOffset) {
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
        alignToConeFloor = getVisionTargetCommand();
        this.getController().setTolerance(tolerance);
        this.setName("DriveToConeFloor");
    }

    @Override
    public void initialize() {
        super.initialize();
        out = 0;
        alignToConeFloor.initialize();
        Robot.vision.setLimelightPipeline(m_limelight, VisionConfig.coneDetectorPipeline);
    }

    @Override
    public void execute() {
        super.execute();
        // If we are already closer than the target distance stop driving.
        if (getVerticalOffset() < getVerticalSetpoint() || !Robot.vision.isDetetTarget()) {
            out = 0;
        }

        if (getVerticalOffset() > maxConeInView) {
            out = 0;
        }
        // RobotTelemetry.print("Drive to Cone Out: " + out);
        alignToConeFloor.execute();
        Auton.updateLog("Floor Vert offset at execution: " + getVerticalOffset(), this.getName());
    }

    @Override
    public void end(boolean interrupted) {
        alignToConeFloor.end(interrupted);
        Auton.updateLog(
                "Floor Vert offset at end: "
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
        // wait for robot to horizontally and vertically aligned before ending
        if (Math.floor(getVerticalOffset()) < verticalSetpoint && alignToConeFloor.isFinished()) {
            return true;
        }
        if (getVerticalOffset() > maxConeInView) {
            return true;
        }
        return false;
    }

    // If out is > 1 then cap at one, make the robot drive slow and still have bigger kP
    private static void setOutput(double output) {
        out = -1 * output;
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
    public Command getVisionTargetCommand() {
        return new AlignToConeFloor(() -> getOutput(), horizontalOffset);
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
