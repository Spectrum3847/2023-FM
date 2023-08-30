// Created by Spectrum3847
package frc.robot.auton.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.auton.Auton;
import frc.robot.intake.commands.IntakeCommands;

public class Eject extends CommandBase {
    Command ejectCommand;

    public Eject() {
        addRequirements(Robot.intake);
    }

    @Override
    public void initialize() {
        if (Auton.score3rd.getSelected() == true) {
            ejectCommand = IntakeCommands.eject();
        } else {
            ejectCommand = new WaitCommand(0);
        }

        ejectCommand.initialize();
    }

    @Override
    public void execute() {
        ejectCommand.execute();
    }

    @Override
    public void end(boolean interrupted) {
        ejectCommand.end(interrupted);
        AutonCommands.retractIntake().execute();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
