package frc.robot.auton.commands;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.auton.Auton;
import frc.robot.swerve.commands.LockSwerve;

public class AutoPaths {
    public static Command CleanSide() {
        return AutonCommands.coneTopFull()
                .andThen(
                        Auton.getAutoBuilder()
                                .fullAuto(
                                        PathPlanner.loadPathGroup(
                                                "CleanSide1", new PathConstraints(4, 1))))
                .andThen(AutonCommands.cubeTopFull());
    }

    public static Command BumpSide() {
        return AutonCommands.coneTopFull()
                .andThen(
                        Auton.getAutoBuilder()
                                .fullAuto(
                                        PathPlanner.loadPathGroup(
                                                "Bump1", new PathConstraints(1, 1))))
                .andThen(AutonCommands.cubeTopFull());
    }

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
                                                "Balance2", new PathConstraints(4, 2.5))))
                .andThen(new AutoBalance())
                .andThen(new LockSwerve().withTimeout(0.1));
    }
}
