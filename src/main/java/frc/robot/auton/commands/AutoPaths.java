package frc.robot.auton.commands;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.auton.Auton;
import frc.robot.mechanisms.MechanismsCommands;
import frc.robot.swerve.commands.DriveToConeNode;
import frc.robot.swerve.commands.LockSwerve;

public class AutoPaths {

    public static Command ConePoleTest() {
        return new DriveToConeNode(2);
    }

    public static Command CleanSide() {
        return AutonCommands.coneTop()
                .andThen(
                        Auton.getAutoBuilder()
                                .fullAuto(
                                        PathPlanner.loadPathGroup(
                                                "CleanSide1", new PathConstraints(4, 3.5))))
                .andThen(
                        new DriveToConeNode(2))
                .andThen(MechanismsCommands.coneStandingIntake());
    }

    public static Command CleanSide1() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("CleanSide1", new PathConstraints(4, 3.5)));
    }

    public static Command CleanSide2() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("CleanSide2", new PathConstraints(4, 3.5)));
    }

    public static Command CleanSide3() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("CleanSide3", new PathConstraints(4, 3.5)));
    }

    public static Command CleanSide4() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("CleanSide4", new PathConstraints(4, 3.5)));
    }

    public static Command CleanSide5() {
        return (Auton.getAutoBuilder()
                        .fullAuto(
                                PathPlanner.loadPathGroup("CleanSide5", new PathConstraints(4, 4))))
                .withTimeout(0.8)
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

    //     public static Command CleanSidewMid() {
    //         return AutonCommands.coneTopFull()
    //                 .andThen(
    //                         Auton.getAutoBuilder()
    //                                 .fullAuto(
    //                                         PathPlanner.loadPathGroup(
    //                                                 "CleanSide1", new PathConstraints(4, 1.5))))
    //                 .andThen(AutonCommands.coneMidFull())
    //                 .andThen(
    //                         Auton.getAutoBuilder()
    //                                 .fullAuto(
    //                                         PathPlanner.loadPathGroup(
    //                                                 "CleanSide3", new PathConstraints(4, 2))));
    //     }

    //     public static Command CleanSideAndAHalf() {
    //         return AutonCommands.coneTopFull()
    //                 .andThen(
    //                         Auton.getAutoBuilder()
    //                                 .fullAuto(
    //                                         PathPlanner.loadPathGroup(
    //                                                 "CleanSide1", new PathConstraints(4, 1))))
    //                 .andThen(AutonCommands.coneMidFull())
    //                 .andThen(
    //                         Auton.getAutoBuilder()
    //                                 .fullAuto(
    //                                         PathPlanner.loadPathGroup(
    //                                                 "CleanSide2", new PathConstraints(4, 2))));
    //     }

    //     public static Command BumpSide() {
    //         return AutonCommands.coneTopFull()
    //                 .andThen(
    //                         Auton.getAutoBuilder()
    //                                 .fullAuto(
    //                                         PathPlanner.loadPathGroup(
    //                                                 "Bump1", new PathConstraints(4, 1.1))))
    //                 .andThen(AutonCommands.coneMidFull());
    //     }

    public static Command OverCharge() {
        return AutonCommands.coneTopFull()
                .andThen(
                        Auton.getAutoBuilder()
                                .fullAuto(
                                        PathPlanner.loadPathGroup(
                                                "Balance1", new PathConstraints(4, 2.5))))
                .andThen(
                        Auton.getAutoBuilder()
                                .fullAuto(
                                        PathPlanner.loadPathGroup(
                                                "Balance2", new PathConstraints(4, 3))))
                .andThen(new WaitCommand(0.5).withTimeout(0.5))
                .andThen(new AutoBalance())
                .andThen(new LockSwerve().withTimeout(0.1));
    }
}
