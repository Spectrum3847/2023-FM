package frc.robot.auton.commands;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.auton.Auton;

public class AutoPaths {
    public static Command CleanSide() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("CleanSide1", new PathConstraints(4, 3)));
    }

    public static Command BumpSide() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("BumpSide1", new PathConstraints(4, 3)));
    }

    public static Command OverCharge() {
        return Auton.getAutoBuilder()
                .fullAuto(PathPlanner.loadPathGroup("OverCharge1", new PathConstraints(4, 3)));
    }
}
