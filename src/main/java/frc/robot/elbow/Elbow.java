package frc.robot.elbow;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.SpectrumLib.subsystems.angleMech.AngleMechSubsystem;
import frc.robot.RobotConfig;

public class Elbow extends AngleMechSubsystem {
    public static ElbowConfig config = new ElbowConfig();
    public static boolean isInitialized = false;

    public Elbow() {
        super(config);
        motorLeader = new WPI_TalonFX(RobotConfig.Motors.elbowMotor);
        config.updateTalonFXConfig();
        setupFalconLeader();
        motorLeader.setInverted(TalonFXInvertType.Clockwise); // Should be done in config
        motorLeader.setNeutralMode(NeutralMode.Brake);
        motorLeader.configReverseSoftLimitThreshold(-68316); // TODO: change?
        motorLeader.configReverseSoftLimitEnable(true);
        motorLeader.configForwardSoftLimitThreshold(config.elbowMaxFalcon);
        motorLeader.configForwardSoftLimitEnable(true);
    }

    public void zeroElbow() {
        motorLeader.setSelectedSensorPosition(0);
    }

    public void setBrakeMode(boolean brake) {
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
        return config.elbowMaxFalcon * (percent / 100);
    }

    public double falconToPercent(double falcon) {
        return (falcon / config.elbowMaxFalcon) * 100;
    }

    public double getPercentAngle() {
        return motorLeader.getSelectedSensorPosition() / config.elbowMaxFalcon * 100;
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
