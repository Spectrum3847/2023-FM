package frc.robot.auton.commands;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.auton.Auton;
import frc.robot.swerve.commands.LockSwerve;

public class AutoPaths {
    public static Command CleanSide() {
        return AutonCommands.coneTopFull()
                .andThen(
                        Auton.getAutoBuilder()
                                .fullAuto(
                                        PathPlanner.loadPathGroup(
                                                "CleanSide1", new PathConstraints(4, 1.1))))
                .andThen(AutonCommands.coneMidFull());
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
