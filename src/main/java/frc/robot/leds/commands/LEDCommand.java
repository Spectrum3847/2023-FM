package frc.robot.leds.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public abstract class LEDCommand extends Command {
    String name;
    int priority;
    double timeout;
    /** Rerun the command if it was cancelled and the timeout wasn't finished */
    boolean prioritizeTimeout;

    boolean interrupted;

    public LEDCommand(String name, int priority, double timeout) {
        super();
        setName(name);
        addRequirements(Robot.leds);
        this.name = name;
        this.priority = priority;
        this.timeout = timeout;
        this.prioritizeTimeout = false;
        this.interrupted = false;
    }

    public LEDCommand(String name, int priority) {
        super();
        setName(name);
        addRequirements(Robot.leds);
        this.name = name;
        this.priority = priority;
        this.timeout = -101;
        this.prioritizeTimeout = false;
        this.interrupted = false;
    }

    @Override
    public void initialize() {
        Robot.leds.scheduler.addAnimation(name, this, priority, timeout);
    }

    public abstract void ledInitialize();

    public abstract void ledExecute();

    public boolean runsWhenDisabled() {
        return true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        this.interrupted = interrupted;
        Robot.leds.scheduler.removeAnimation(name);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    public boolean isTimeoutPriority() {
        return prioritizeTimeout;
    }

    public boolean getInterrupted() {
        return this.interrupted;
    }
}
