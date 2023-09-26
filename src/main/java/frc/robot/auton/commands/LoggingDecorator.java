// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.auton.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.auton.Auton;
import java.util.Set;

public class LoggingDecorator extends CommandBase {

    private final Command command;
    private String commandType;

    /**
     * Creates a new LoggingDecorator. Add start/end logging to any command/sequence using {@link
     * Auton#AUTON_LOG}
     */
    public LoggingDecorator(Command command) {
        this.command = command;
        // Use addRequirements() here to declare subsystem dependencies.
        Set<Subsystem> requirements = command.getRequirements();
        this.addRequirements(requirements.toArray(new Subsystem[requirements.size()]));
        commandType = (requirements.size() == 0) ? "Command" : "Sequence";
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Auton.updateLog(commandType + " Started", command.getName());
        command.initialize();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        command.execute();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        command.end(interrupted);
        Auton.updateLog(commandType + " Ended", command.getName());
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return command.isFinished();
    }
}
