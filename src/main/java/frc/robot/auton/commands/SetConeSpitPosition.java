// Created by Spectrum3847
package frc.robot.auton.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.auton.Auton;
import frc.robot.shoulder.commands.ShoulderCommands;
import frc.robot.slide.commands.SlideCommands;

public class SetConeSpitPosition extends CommandBase {
    Command setConeSpitPosition;

    public SetConeSpitPosition() {
        addRequirements(Robot.slide, Robot.shoulder);
    }

    @Override
    public void initialize() {
        if (Auton.score3rd.getSelected() == true) {
            setConeSpitPosition = SlideCommands.home().alongWith(ShoulderCommands.coneFloorGoal());
        } else {
            setConeSpitPosition = new WaitCommand(0);
        }
        setConeSpitPosition.initialize();
    }

    @Override
    public void execute() {
        setConeSpitPosition.execute();
    }

    @Override
    public void end(boolean interrupted) {
        setConeSpitPosition.end(interrupted);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
