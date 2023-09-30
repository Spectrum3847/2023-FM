package frc.robot.auton;

import com.pathplanner.lib.auto.PIDConstants;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.auton.commands.AutoPaths;
import frc.robot.auton.commands.AutonCommands;
import frc.robot.mechanisms.MechanismsCommands;
import frc.robot.operator.commands.OperatorCommands;
import frc.robot.swerve.commands.LockSwerve;
import frc.robot.trajectories.TrajectoriesConfig;
import frc.robot.vision.VisionCommands;
import java.util.HashMap;

public class Auton {
    public static String AUTON_LOG = "";
    public static final SendableChooser<Command> autonChooser = new SendableChooser<>();
    public static final SendableChooser<Boolean> score3rd = new SendableChooser<>();
    private static boolean autoMessagePrinted = true;
    private static double autonStart = 0;
    public static HashMap<String, Command> eventMap =
            new HashMap<>(); // Stores all the values of the event map

    public Auton() {
        setupEventMap(); // sets the eventmap to run during auto
        setupSelectors(); // runs the command to start the chooser for auto on shuffleboard
    }

    // Autobuilder only using odometry
    public static SwerveAutoBuilder getAutoBuilder() {
        return new SwerveAutoBuilder(
                Robot.swerve.odometry::getPoseMeters, // Pose2d supplier
                Robot.swerve.odometry
                        ::resetOdometry, // Pose2d consumer, used to reset odometry at the
                // beginning of auto
                Robot.swerve.config.swerveKinematics, // SwerveDriveKinematics
                new PIDConstants(
                        TrajectoriesConfig.kPTranslationController,
                        TrajectoriesConfig.kITranslationController,
                        TrajectoriesConfig.kDTranslationController), // PID constants to correct for
                // translation error (used to create
                // the X and Y PID controllers)
                new PIDConstants(
                        TrajectoriesConfig.kPRotationController,
                        TrajectoriesConfig.kIRotationController,
                        TrajectoriesConfig
                                .kDRotationController), // PID constants to correct for rotation
                // error (used to create the
                // rotation controller)
                Robot.swerve
                        ::setModuleStatesAuto, // Module states consumer used to output to the drive
                // subsystem
                eventMap, // Gets the event map values to use for running addional
                // commands during auto
                true, // Should the path be automatically mirrored depending on
                // alliance color
                // Alliance.
                Robot.swerve // The drive subsystem. Used to properly set the requirements of
                // path following commands
                );
    }

    // A chooser for autonomous commands
    public static void setupSelectors() {
        // // Advanced comp autos with odometry (Ordered by likelyhood of running)
        autonChooser.setDefaultOption("Clean3", AutoPaths.CleanSide());
        autonChooser.addOption("Balance w/ Mobility (1 Piece)", AutoPaths.OverCharge());
        // autonChooser.addOption("Align to AprilTag", AutonCommands.AlignToAprilTagTest());
        // autonChooser.addOption("Drive to AprilTag", AutonCommands.DriveToAprilTagTest());
        // autonChooser.addOption("Align to ConeNode", AutonCommands.AlignToConeNodeTest());
        autonChooser.addOption("Drive to ConeNode", AutonCommands.DriveToConeNodeTest());
        // autonChooser.addOption("Align to CubeNode", AutonCommands.AlignToAprilTagTest());
        // autonChooser.addOption("Drive to CubeNode", AutonCommands.DriveToCubeNode());
        autonChooser.addOption("Drive to Cone Floor", AutonCommands.DriveToConeFloorTest());

        autonChooser.addOption("CleanBump3", AutoPaths.CleanBumpSide());

        autonChooser.addOption("OperatorLaunch", OperatorCommands.launch());
        // autonChooser.addOption("ConeMidPlacement", OperatorCommands.coneMid());
        // // Simple comp autos
        // autonChooser.addOption(
        //         "Taxi Simple w/ High Cone",
        //         AutonCommands.coneTopFull()
        //                 .andThen(AutonCommands.retractIntake().withTimeout(3))
        //                 .andThen(new TaxiCommand())
        //                 .andThen(PoseCommands.resetHeading(180)));
        // autonChooser.addOption(
        //         "Taxi Simple", new TaxiCommand().andThen(PoseCommands.resetHeading(180)));
        // autonChooser.addOption(
        //         "Nothing",
        //         new PrintCommand("Doing Nothing in Auton")
        //                 .andThen(new WaitCommand(5))); // setups an auto that does nothing
        // Autos for tuning/testing (not used at comp)
        // autonChooser.addOption(
        //         "1 Meter",
        //         getAutoBuilder()
        //                 .fullAuto(
        //                         PathPlanner.loadPathGroup(
        //                                 "1 Meter",
        //                                 new PathConstraints(
        //                                         AutonConfig.kMaxSpeed, AutonConfig.kMaxAccel))));
        // autonChooser.addOption(
        //         "3 Meters",
        //         getAutoBuilder()
        //                 .fullAuto(
        //                         PathPlanner.loadPathGroup(
        //                                 "3 Meters",
        //                                 new PathConstraints(
        //                                         AutonConfig.kMaxSpeed, AutonConfig.kMaxAccel))));
        // autonChooser.addOption(
        //         "5 Meters",
        //         getAutoBuilder()
        //                 .fullAuto(
        //                         PathPlanner.loadPathGroup(
        //                                 "5 Meters",
        //                                 new PathConstraints(
        //                                         AutonConfig.kMaxSpeed, AutonConfig.kMaxAccel))));
    }

    // Adds event mapping to autonomous commands
    public static void setupEventMap() {
        // Cone placing Commands
        eventMap.put("ConeMid", AutonCommands.coneMidPreScore());
        eventMap.put("ConeMidFull", AutonCommands.coneMidFull());
        eventMap.put("ConeTop", AutonCommands.coneTopPreScore());
        eventMap.put("ConeTopFull", AutonCommands.coneTopFull());
        eventMap.put("FloorPrescore", AutonCommands.floorPreSchool());
        eventMap.put("ConeHybrid", MechanismsCommands.lowScore().withTimeout(1));
        eventMap.put("HomeSystems", AutonCommands.homeSystems());
        eventMap.put("Throw", OperatorCommands.launch());

        // Intake Commands
        eventMap.put("Intake", AutonCommands.floorIntake());
        eventMap.put("RetractIntake", AutonCommands.retractIntake());
        eventMap.put("HoldIntake", AutonCommands.holdIntake());
        // Drivetrain Commands
        eventMap.put("LockSwerve", new LockSwerve());
        eventMap.put("FaceForward", AutonCommands.faceForward());
        eventMap.put("FaceBackward", AutonCommands.faceBackward());
        // LimeLight Commands
        eventMap.put("ConeFloorPipeline", VisionCommands.setConeDetectPipeline());
        eventMap.put("ConeNodePipeline", VisionCommands.setConeNodePipeline());
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public static Command getAutonomousCommand() {
        // return new CharacterizeLauncher(Robot.launcher);
        Command auton = autonChooser.getSelected(); // sees what auto is chosen on shuffleboard
        if (auton != null) {
            return auton; // checks to make sure there is an auto and if there is it runs an auto
        } else {
            return new PrintCommand(
                    "*** AUTON COMMAND IS NULL ***"); // runs if there is no auto chosen, which
            // shouldn't happen because of the default
            // auto set to nothing which still runs
            // something
        }
    }

    /** This method is called in AutonInit */
    public static void startAutonTimer() {
        autonStart = Timer.getFPGATimestamp();
        autoMessagePrinted = false;
    }

    // Get the time since Auton started
    public static double getAutonElapasedTime() {
        return Timer.getFPGATimestamp() - autonStart;
    }

    /** Called in RobotPeriodic and displays the duration of the auton command Based on 6328 code */
    public static void printAutoDuration() {
        Command autoCommand = Auton.getAutonomousCommand();
        if (autoCommand != null) {
            if (!autoCommand.isScheduled() && !autoMessagePrinted) {
                if (DriverStation.isAutonomousEnabled()) {
                    printLog();
                    RobotTelemetry.print(
                            String.format(
                                    "*** Auton finished in %.2f secs ***", getAutonElapasedTime()));
                } else {
                    printLog();
                    RobotTelemetry.print(
                            String.format(
                                    "*** Auton CANCELLED in %.2f secs ***",
                                    getAutonElapasedTime()));
                }
                autoMessagePrinted = true;
            }
        }
    }

    public static void updateLog(String log) {
        AUTON_LOG += String.format("** AUTO@%.2fs ** ", getAutonElapasedTime()) + log + "\n";
    }

    public static void updateLog(String log, String name) {
        updateLog(name.toUpperCase() + ": " + log);
    }

    public static void updateLog(String log, Command command) {
        updateLog(log, command.getName());
    }

    public static void resetLog() {
        AUTON_LOG = "";
    }

    public static void printLog() {
        if (AUTON_LOG.isBlank()) {
            AUTON_LOG = "*** AUTON LOG IS EMPTY ***";
        } else {
            updateLog("--------------------------END OF AUTO LOG--------------------------");
        }
        RobotTelemetry.print(AUTON_LOG);
        resetLog();
    }
}
