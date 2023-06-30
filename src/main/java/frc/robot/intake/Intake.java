package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotConfig;

public class Intake extends SubsystemBase {
    public static IntakeConfig config = new IntakeConfig();
    public IntakeTelemetry telemetry;
    public IntakeMotor lowerMotor;
    public IntakeMotor intakeMotor;
    public IntakeMotor launcherMotor;
    // private DigitalInput cubeSensor;

    public Intake() {
        super();
        intakeMotor =
                new IntakeMotor(
                        config, RobotConfig.Motors.intakeMotor, TalonFXInvertType.CounterClockwise);

        // cubeSensor = new DigitalInput(0);
        telemetry = new IntakeTelemetry(this);
    }

    // public boolean getCubeSensor() {
    //     return !cubeSensor.get();
    // }

    public void setCurrentLimits(double limit, double threshold) {
        intakeMotor.setCurrentLimit(limit, threshold);
    }

    public void setVelocities(double velocity) {
        intakeMotor.setVelocity(velocity);
    }

    public void setPercentOutputs(double percent) {
        intakeMotor.setPercent(percent);
    }

    public void launch() {
        intakeMotor.setVelocity(intakeMotor.setpoint);
    }

    public void stopAll() {
        intakeMotor.stop();
    }

    public double getFrontCurrent() {
        return intakeMotor.getCurrent();
    }

    public double getFrontRPM() {
        return intakeMotor.getVelocity();
    }
}
