package frc.robot.auton.commands;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.auton.Auton;
import frc.robot.pilot.PilotConfig;
import frc.robot.swerve.commands.DriveToConeFloor;
import frc.robot.swerve.commands.DriveToConeNode;
import frc.robot.swerve.commands.LockSwerve;
import frc.robot.vision.VisionCommands;

public class AutoPaths {

    public static Command CleanSide() {
        return logCommand(AutonCommands.coneTopScore())
                .deadlineWith(AutonCommands.alignWheelsStraight())
                .andThen(
                        logCommand(CleanSide1()),
                        logCommand(AlignAndIntakeCone()),
                        logCommand(CleanSide2()),
                        logCommand(AlignPreScoreTopCone()),
                        logCommand(AutonCommands.scoreGP()),
                        logCommand(CleanSide3()),
                        logCommand(AlignAndIntakeCone()),
                        logCommand(CleanSide4()),
                        logCommand(AlignPreScoreMidCone()),
                        logCommand(AutonCommands.scoreGP()),
                        logCommand(AutonCommands.homeSystems()));
    }

    public static Command AlignAndIntakeCone() {
        return logCommand(new DriveToConeFloor(PilotConfig.coneFloorAlignOffset))
                .deadlineWith(AutonCommands.floorIntake(), VisionCommands.setConeDetectPipeline())
                .withTimeout(1.2)
                .andThen(AutonCommands.finishIntakeDrive())
                .withName("AlignAndIntakeCone");
    }

    public static Command AlignPreScoreTopCone() {
        return logCommand(new DriveToConeNode(PilotConfig.coneNodeAlignOffset))
                .alongWith(VisionCommands.setConeNodePipeline().withTimeout(0.1))
                .withTimeout(3)
                .andThen(AutonCommands.coneTopPreScore().withTimeout(1.2), new WaitCommand(0.3))
                .withName("AlignPreScoreTopCone");
    }

    public static Command AlignPreScoreMidCone() {
        return logCommand(new DriveToConeNode(PilotConfig.coneNodeAlignOffset))
                .alongWith(
                        AutonCommands.coneMidPreScore().withTimeout(1.2),
                        VisionCommands.setConeNodePipeline().withTimeout(0.1))
                .withTimeout(3)
                .withName("AlignPreScoreMidCone");
    }

    // 1st use full auto
    // Drive to pre-cone 1 spot
    // Commands - homesystems, setConeFloor pipeline, deploy intake midway
    public static Command CleanSide1() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("CleanSide1", new PathConstraints(4, 2.5)))
                .withName("CleanSide1");
    }

    // Drive to outside cone pre node spot to prepare for align
    // Commands - retract intake, set cone node pipline, just after start, hold
    // intake, at end set
    // ConeTopPrescore
    public static Command CleanSide2() {
        return Auton.getAutoBuilder()
                .followPathGroupWithEvents(
                        PathPlanner.loadPathGroup("CleanSide2", new PathConstraints(4, 2.0)))
                .withName("CleanSide2");
    }

    // Drive to pre-cone 2 spot
    // Commands - homesystems, setConeFloor pipeline, deploy Intake midway
    public static Command CleanSide3() {
        return Auton.getAutoBuilder()
                .followPathGroupWithEvents(
                        PathPlanner.loadPathGroup("CleanSide3", new PathConstraints(4, 2.5)))
                .withName("CleanSide3");
    }

    // Drive to pre-outside cone node spot
    // Commands - retract intake, set cone node pipeline, just after start hold
    // intake, at end set
    // conemidprescore
    public static Command CleanSide4() {
        return Auton.getAutoBuilder()
                .followPathGroupWithEvents(
                        PathPlanner.loadPathGroup("CleanSide4", new PathConstraints(4, 2.0)))
                .withName("CleanSide4");
    }

    public static Command CleanSide5() {
        return (Auton.getAutoBuilder()
                        .followPathGroupWithEvents(
                                PathPlanner.loadPathGroup("CleanSide5", new PathConstraints(4, 4))))
                .withTimeout(0.2)
                .andThen(new InstantCommand(() -> Robot.swerve.brakeMode(false), Robot.swerve));
    }

    public static Command CleanBumpSide1() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("CleanBumpSide1", new PathConstraints(4, 3.5)));
    }

    public static Command CleanBumpSide2() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("CleanBumpSide2", new PathConstraints(4, 3.5)));
    }

    public static Command CleanBumpSide3() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("CleanBumpSide3", new PathConstraints(4, 3.5)));
    }

    public static Command CleanBumpSide4() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("CleanBumpSide4", new PathConstraints(4, 3.5)));
    }

    public static Command CleanBumpSide5() {
        return (Auton.getAutoBuilder()
                        .fullAuto(
                                PathPlanner.loadPathGroup(
                                        "CleanBumpSide5", new PathConstraints(4, 4))))
                .withTimeout(0.8)
                .andThen(new InstantCommand(() -> Robot.swerve.brakeMode(false), Robot.swerve));
    }

    public static Command logCommand(Command command) {
        return new LoggingDecorator(command);
    }

    // public static Command CleanSidewMid() {
    // return AutonCommands.coneTopFull()
    // .andThen(
    // Auton.getAutoBuilder()
    // .fullAuto(
    // PathPlanner.loadPathGroup(
    // "CleanSide1", new PathConstraints(4, 1.5))))
    // .andThen(AutonCommands.coneMidFull())
    // .andThen(
    // Auton.getAutoBuilder()
    // .fullAuto(
    // PathPlanner.loadPathGroup(
    // "CleanSide3", new PathConstraints(4, 2))));
    // }

    // public static Command CleanSideAndAHalf() {
    // return AutonCommands.coneTopFull()
    // .andThen(
    // Auton.getAutoBuilder()
    // .fullAuto(
    // PathPlanner.loadPathGroup(
    // "CleanSide1", new PathConstraints(4, 1))))
    // .andThen(AutonCommands.coneMidFull())
    // .andThen(
    // Auton.getAutoBuilder()
    // .fullAuto(
    // PathPlanner.loadPathGroup(
    // "CleanSide2", new PathConstraints(4, 2))));
    // }

    // public static Command BumpSide() {
    // return AutonCommands.coneTopFull()
    // .andThen(
    // Auton.getAutoBuilder()
    // .fullAuto(
    // PathPlanner.loadPathGroup(
    // "Bump1", new PathConstraints(4, 1.1))))
    // .andThen(AutonCommands.coneMidFull());
    // }

    //     public static Command ConePoleTest() {
    //         return new DriveToConeNode(2);
    //     }

    public static Command OverCharge() {
        return AutonCommands.coneTopFull()
                .deadlineWith(AutonCommands.alignWheelsStraight())
                .andThen(
                        Auton.getAutoBuilder()
                                .fullAuto(
                                        PathPlanner.loadPathGroup(
                                                "Balance1", new PathConstraints(4, 2.5))))
                .andThen(
                        Auton.getAutoBuilder()
                                .followPathGroupWithEvents(
                                        PathPlanner.loadPathGroup(
                                                "Balance2", new PathConstraints(4, 3))))
                .andThen(new WaitCommand(0.5).withTimeout(0.5))
                .andThen(new AutoBalance())
                .andThen(new LockSwerve().withTimeout(0.1));
    }
}
