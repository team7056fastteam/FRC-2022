// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.utils.Utilities;

import static frc.robot.utils.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Lifter {
    private Robot _robot;
    Utilities util = new Utilities();

    private final CANSparkMax leftLiftMotor;
    private final CANSparkMax rightLiftMotor;

    XboxController operator = new XboxController(1);

    public Lifter(Robot robot) {
        _robot = robot;
        ShuffleboardTab tab = Shuffleboard.getTab("Subsystems");

        leftLiftMotor = new CANSparkMax(LIFT_LEFT_MOTOR, MotorType.kBrushless);
        util.configure("Lift L", leftLiftMotor, 40, 0, false, tab);
        rightLiftMotor = new CANSparkMax(LIFT_RIGHT_MOTOR, MotorType.kBrushless);
        util.configure("Lift R", rightLiftMotor, 40, 0, false, tab);
    }

    // Configuration
    double liftSpeed = 0.5;

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {

        if (operator.getRawButton(3)) {
            leftLiftMotor.set(_robot.invert(liftSpeed));
            rightLiftMotor.set(_robot.invert(liftSpeed));
        } else if (operator.getRawButton(4)) {
            leftLiftMotor.set(liftSpeed);
            rightLiftMotor.set(liftSpeed);
        } else {
            leftLiftMotor.set(0.0);
            rightLiftMotor.set(0.0);
        }

        // Conveyor override controls
        if (operator.getRightY() > 0.1 || operator.getRightY() < -0.1) {
            leftLiftMotor.set(operator.getRightY() * .75);
            rightLiftMotor.set(operator.getRightY() * .75);
        }
    }
}
