package frc.robot.intake;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class IntakeTelemetry {

    protected ShuffleboardTab tab;

    @SuppressWarnings("unused")
    private Intake intake;

    boolean intailized = false;

    public IntakeTelemetry(Intake intake) {
        this.intake = intake;

        tab = Shuffleboard.getTab("Intake");
        tab.addNumber("Intake Motor Velocity", () -> intake.intakeMotor.getVelocity())
                .withPosition(0, 1);
    }

    public void testMode() {
        if (!intailized) {

            intailized = true;
        }
    }
}
