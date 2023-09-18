package frc.robot.operator;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.SpectrumLib.gamepads.AxisButton;
import frc.SpectrumLib.gamepads.Gamepad;
import frc.SpectrumLib.gamepads.XboxGamepad.XboxAxis;
import frc.robot.elbow.commands.ElbowCommands;
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

        /* Intaking */
        gamepad.leftTriggerButton
                .and(rightBumper())
                .and(noRightTrigger())
                .whileTrue(OperatorCommands.intake()); // Daniel only
        gamepad.leftTriggerButton.and(noRightBumper()).whileTrue(OperatorCommands.airIntake());
        gamepad.rightTriggerButton.and(noBumpers()).whileTrue(OperatorCommands.shelfIntake());

        /* Scoring/Positions */
        gamepad.leftBumper.and(noRightBumper()).whileTrue(OperatorCommands.homeAndSlowIntake());
        gamepad.leftBumper.and(rightBumper()).whileTrue(OperatorCommands.preScorePos());
        gamepad.xButton.and(noBumpers()).whileTrue(OperatorCommands.coneMid());
        gamepad.yButton.whileTrue(OperatorCommands.coneTop());
        gamepad.aButton.and(noRightBumper()).whileTrue(OperatorCommands.cubeMid());
        gamepad.bButton.whileTrue(OperatorCommands.cubeTop());
        // ground score is below, but different buttons for Daniel and training

        /* Misc */
        gamepad.Dpad.Up.whileTrue(IntakeCommands.intake()); // manual intake
        gamepad.Dpad.Down.whileTrue(IntakeCommands.eject());
        gamepad.Dpad.Left.whileTrue(LEDCommands.coneLED());
        gamepad.Dpad.Right.whileTrue(LEDCommands.cubeLED());
        gamepad.startButton.whileTrue(ShoulderCommands.zeroShoulderRoutine());
        gamepad.selectButton.and(noRightBumper()).whileTrue(SlideCommands.zeroSlideRoutine());
        gamepad.startButton.and(gamepad.selectButton).whileTrue(OperatorCommands.cancelCommands());
        gamepad.xButton
                .and(bothTriggers())
                .and(bothBumpers())
                .whileTrue(OperatorCommands.killTheRobot());

        /* Daniel Only */
        gamepad.aButton.and(rightBumper()).whileTrue(OperatorCommands.floorScore());
        gamepad.xButton.and(rightBumper()).whileTrue(OperatorCommands.floorScore());
        gamepad.selectButton.and(rightBumper()).whileTrue(ElbowCommands.zeroElbowRoutine());

        AxisButton.create(gamepad, XboxAxis.RIGHT_Y, 0.1)
                .and(rightBumper())
                .whileTrue(OperatorCommands.manualElbow()); // Daniel only

        /* Operation Training Wheels */
        // gamepad.rightBumper.whileTrue(OperatorCommands.floorScore());

        /* Manual Control */
        AxisButton.create(gamepad, XboxAxis.RIGHT_Y, 0.1)
                .and(noRightBumper())
                .whileTrue(OperatorCommands.manualShoulder());
        AxisButton.create(gamepad, XboxAxis.LEFT_Y, 0.1)
                .and(noRightBumper())
                .whileTrue(OperatorCommands.manualSlide());
    }

    public void setupDisabledButtons() {
        gamepad.aButton.whileTrue(LEDCommands.coneLED());
        gamepad.yButton.whileTrue(LEDCommands.cubeLED());
        gamepad.bButton
                .and(bothTriggers())
                .and(bothBumpers())
                .whileTrue(new CountdownLEDCommand("Manual Countdown", 120, 10, true));
        gamepad.xButton
                .and(bothTriggers())
                .and(bothBumpers())
                .whileTrue(OperatorCommands.killTheRobot());
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

    private Trigger noRightTrigger() {
        return gamepad.rightTriggerButton.negate();
    }

    private Trigger noBumpers() {
        return gamepad.rightBumper.negate().and(gamepad.leftBumper.negate());
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

    public double elbowManual() {
        return gamepad.rightStick.getY() * OperatorConfig.elbowModifier;
    }
}
