package frc.robot.shoulder;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.SpectrumLib.subsystems.angleMech.AngleMechSubsystem;
import frc.robot.RobotConfig;

public class Shoulder extends AngleMechSubsystem {
    public static ShoulderConfig config = new ShoulderConfig();

    public Shoulder() {
        super(config);
        motorLeader = new WPI_TalonFX(RobotConfig.Motors.shoulderMotor);
        config.updateTalonFXConfig();
        setupFalconLeader();
        motorLeader.setInverted(TalonFXInvertType.Clockwise); // Should be done in config
        motorLeader.configReverseSoftLimitThreshold(200);
        motorLeader.configReverseSoftLimitEnable(true);
        motorLeader.configForwardSoftLimitThreshold(config.shoulderMaxFalcon);
        motorLeader.configForwardSoftLimitEnable(true);
    }

    public void zeroShoulder() {
        motorLeader.setSelectedSensorPosition(0);
    }

    public void setBrakeMode(Boolean brake) {
        if (brake) {
            motorLeader.setNeutralMode(NeutralMode.Brake);
        } else {
            motorLeader.setNeutralMode(NeutralMode.Coast);
        }
    }

    public void resetSensorPosition(double pos) {
        motorLeader.setSelectedSensorPosition(pos); // 10 for now, will change later
    }

    public double percentToFalcon(double percent) {
        return config.shoulderMaxFalcon * (percent / 100);
    }

    public double getPercentAngle() {
        return motorLeader.getSelectedSensorPosition() / config.shoulderMaxFalcon * 100;
    }

    public void setMMPercent(double percent) {
        setMMPosition(percentToFalcon(percent));
    }

    public void softLimitsTrue() {
        motorLeader.configReverseSoftLimitEnable(true);
    }

    public void softLimitsFalse() {
        motorLeader.configReverseSoftLimitEnable(false);
    }
}
