package frc.robot.mechanisms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.elbow.commands.ElbowCommands;
import frc.robot.intake.commands.IntakeCommands;
import frc.robot.shoulder.commands.ShoulderCommands;
import frc.robot.slide.commands.SlideCommands;
import frc.robot.swerve.commands.SwerveDrive;

public class MechanismsCommands {

    public static Command coneStandingIntake() {

        return SlideCommands.home()
                .alongWith(ShoulderCommands.intake(), ElbowCommands.intake())
                .withTimeout(0.8)
                .andThen(
                        IntakeCommands.intake()
                                .withTimeout(1.2)
                                .alongWith(
                                        new SwerveDrive(
                                                        () -> 1, // drive fwd at full speed
                                                        () -> 0,
                                                        () -> 0,
                                                        () -> 1.0, // full velocity
                                                        () -> false // Robot Relative
                                                        )
                                                .withTimeout(0.3)
                                                .andThen(homeSystems().withTimeout(1))))
                .withName("MechanismsStandingConeIntake");
    }

    public static Command stowIntake() {
        return SlideCommands.home()
                .alongWith(ShoulderCommands.stow().withTimeout(0.2).andThen(ElbowCommands.stow()))
                .withTimeout(1);
    }

    // public static Command cubeIntake() {
    //     return ElevatorCommands.cubeIntake()
    //             .alongWith(FourBarCommands.cubeIntake())
    //             .withTimeout(0.3)
    //             .andThen(
    //                     new CubeIntake()
    //                             .withTimeout(1)
    //                             .alongWith(
    //                                     new SwerveDrive(
    //                                                     () -> 1, // drive fwd at full speed
    //                                                     () -> 0,
    //                                                     () -> 0,
    //                                                     () -> 1.0, // full velocity
    //                                                     () -> false // drive robot relative
    //                                                     )
    //                                             .withTimeout(1)
    //                                             .andThen(homeSystems().withTimeout(1))))
    //             .withName("MechanismsStandingCone");
    // }

    public static Command homeAndSlowIntake() {
        return IntakeCommands.slowIntake()
                .alongWith(homeSystems())
                .withName("OperatorSlowHomeIntake");
    }

    public static Command scoreRoutine() {
        return ElbowCommands.score()
                .withTimeout(0.1)
                .andThen(IntakeCommands.drop())
                .withTimeout(0.3)
                .andThen(homeSystems().withTimeout(2.5));
    }

    /** Goes to home position */
    public static Command homeSystems() {
        return SlideCommands.home()
                .alongWith(ShoulderCommands.home(), ElbowCommands.home())
                .withName("OperatorHomeSystems");
    }
}
