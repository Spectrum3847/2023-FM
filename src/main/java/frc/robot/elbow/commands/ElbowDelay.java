package frc.robot.elbow.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ElbowDelay extends CommandBase {
    private double safePercent;
    private double finalPercent;
    private double conditionalPos;
    /**
     * Creates a new ElbowDelay. Elbow will move to safePos, wait for Shoulder to be at
     * conditionalPos, then move to finalPos
     *
     * @param safePos Elbow position that won't hit anything
     * @param finalPos position that Elbow will go to after Shoulder is at conditionalPos
     * @param conditionalPos position that Shoulder must be at before Elbow will move to finalPos
     */
    public ElbowDelay(double safePercent, double finalPercent, double conditionalPos) {
        // Use addRequirements() here to declare subsystem dependencies.
        this.safePercent = safePercent;
        this.finalPercent = finalPercent;
        this.conditionalPos = conditionalPos;
        addRequirements(Robot.elbow);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // may need to be in execute
        Robot.elbow.setMMPercent(safePercent);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (Robot.elbow.getPosition() > Robot.elbow.percentToFalcon(safePercent)) {
            Robot.elbow.setMMPercent(safePercent);
        } else if (Robot.shoulder.getPosition() <= Robot.shoulder.percentToFalcon(conditionalPos)) {
            Robot.elbow.setMMPercent(finalPercent);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.elbow.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
