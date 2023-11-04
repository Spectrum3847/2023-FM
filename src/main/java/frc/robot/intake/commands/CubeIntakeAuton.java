// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot.intake.commands;

// import edu.wpi.first.wpilibj.RobotState;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.Robot;
// import frc.robot.elevator.commands.ElevatorCommands;
// import frc.robot.fourbar.commands.FourBarCommands;

// public class CubeIntakeAuton extends Command {
//     boolean velocityLimitReached = false;
//     int count = 0;
//     int thresholdCount = 0;
//     boolean runMotors = true;

//     /** Creates a new CubeIntake. */
//     public CubeIntakeAuton() {
//         // Use addRequirements() here to declare subsystem dependencies.
//         addRequirements(Robot.intake);
//     }

//     // Called when the command is initially scheduled.
//     @Override
//     public void initialize() {
//         velocityLimitReached = false;
//         count = 0;
//         thresholdCount = 0;
//         runMotors = true;

//         // Robot.intake.setCurrentLimits(80, 60);
//     }

//     // Called every time the scheduler runs while the command is scheduled.
//     @Override
//     public void execute() {
//         /*if (Robot.intake.getFrontRPM() > 3000) {
//             if (!velocityLimitReached && thresholdCount >= 8) {
//                 count++;
//                 velocityLimitReached = true;
//             }
//             thresholdCount++;
//         } else if (Robot.intake.getFrontRPM() <= 3000) {
//             velocityLimitReached = false;
//         }

//         if (count >= 2) {
//             runMotors = false;
//         }*/

//         if (runMotors) {
//             // Robot.intake.setVelocities(
//             //        3000, Intake.config.frontIntakeSpeed, Intake.config.launcherIntakeSpeed);
//             Robot.intake.setPercentOutputs(1.0);
//         } else {
//             Robot.intake.stopAll();
//         }
//     }

//     // Called once the command ends or is interrupted.
//     @Override
//     public void end(boolean interrupted) {
//         // Robot.intake.setCurrentLimits(Intake.config.currentLimit, Intake.config.threshold);
//         Robot.operatorGamepad.rumble(0);
//         Robot.pilotGamepad.rumble(0);
//         if (!RobotState.isAutonomous()) {
//             ElevatorCommands.hopElevator().withTimeout(1).schedule();
//             FourBarCommands.home().withTimeout(1).schedule();
//         }
//     }

//     // Returns true when the command should end.
//     @Override
//     public boolean isFinished() {
//         return false;
//     }
// }
