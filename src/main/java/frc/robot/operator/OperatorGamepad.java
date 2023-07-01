package frc.robot.operator;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.SpectrumLib.gamepads.AxisButton;
import frc.SpectrumLib.gamepads.Gamepad;
import frc.SpectrumLib.gamepads.XboxGamepad.XboxAxis;
import frc.robot.intake.commands.IntakeCommands;
import frc.robot.leds.commands.CountdownLEDCommand;
import frc.robot.leds.commands.LEDCommands;
import frc.robot.operator.commands.OperatorCommands;
import frc.robot.shoulder.commands.ShoulderCommands;
import frc.robot.slide.commands.SlideCommands;

/** Used to add buttons to the operator gamepad and configure the joysticks */
public class OperatorGamepad extends Gamepad {
    OperatorConfig config;

    public OperatorGamepad() {
        super("operator", OperatorConfig.port);
        config = new OperatorConfig();
        gamepad.leftStick.setXinvert(OperatorConfig.xInvert);
        gamepad.leftStick.setYinvert(OperatorConfig.yInvert);

        gamepad.rightStick.setXinvert(OperatorConfig.xInvert);
        gamepad.rightStick.setYinvert(OperatorConfig.yInvert);
    }

    public void setupTeleopButtons() {

        gamepad.leftBumper.whileTrue(OperatorCommands.airConeIntake()); //TODO: change to home
        gamepad.leftTriggerButton.whileTrue(OperatorCommands.airConeIntake());
        gamepad.rightTriggerButton.whileTrue(OperatorCommands.coneShelfIntake());
        gamepad.yButton.whileTrue(OperatorCommands.coneTop());
        gamepad.xButton.whileTrue(OperatorCommands.coneMid());
        gamepad.bButton.whileTrue(OperatorCommands.cubeTop());
        gamepad.aButton.whileTrue(OperatorCommands.cubeMid());

        gamepad.Dpad.Up.whileTrue(IntakeCommands.coneIntake());
        


        AxisButton.create(gamepad, XboxAxis.RIGHT_Y, 0.1)
                .and(noRightBumper())
                .whileTrue(OperatorCommands.manualShoulder());
        AxisButton.create(gamepad, XboxAxis.LEFT_Y, 0.1)
                .and(noRightBumper())
                .whileTrue(OperatorCommands.manualSlide());

        AxisButton.create(gamepad, XboxAxis.RIGHT_Y, 0.1)
                .and(rightBumper())
                .whileTrue(OperatorCommands.slowManualShoulder());
        AxisButton.create(gamepad, XboxAxis.LEFT_Y, 0.1)
                .and(rightBumper())
                .whileTrue(OperatorCommands.slowManualSlide());
    }

    public void setupDisabledButtons() {
        gamepad.aButton.whileTrue(LEDCommands.coneFloorLED());
        gamepad.yButton.whileTrue(LEDCommands.cubeLED());
        gamepad.bButton
                .and(bothTriggers())
                .and(bothBumpers())
                .whileTrue(new CountdownLEDCommand("Manual Countdown", 120, 10, true));
        gamepad.bButton.toggleOnTrue(OperatorCommands.coastMode());
    }

    public void setupTestButtons() {}

    private Trigger noRightBumper() {
        return gamepad.rightBumper.negate();
    }

    private Trigger rightBumper() {
        return gamepad.rightBumper;
    }

    private Trigger bothBumpers() {
        return gamepad.rightBumper.and(gamepad.leftBumper);
    }

    private Trigger bothTriggers() {
        return gamepad.rightTriggerButton.and(gamepad.leftTriggerButton);
    }

    public void rumble(double intensity) {
        this.gamepad.setRumble(intensity, intensity);
    }

    public double slideManual() {
        return gamepad.leftStick.getY() * OperatorConfig.slideModifer;
    }

    public double shoulderManual() {
        return gamepad.rightStick.getY() * OperatorConfig.shoulderModifer;
    }
}
