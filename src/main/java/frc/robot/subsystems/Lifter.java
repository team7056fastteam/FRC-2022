// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;

import static frc.robot.Constants.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;

public class Lifter {

    Constants constants = new Constants();

    private final CANSparkMax leftLiftMotor;
    private final CANSparkMax rightLiftMotor;

    public Lifter()
    {
        leftLiftMotor = new CANSparkMax(LIFT_LEFT_MOTOR, MotorType.kBrushless);
        rightLiftMotor = new CANSparkMax(LIFT_RIGHT_MOTOR, MotorType.kBrushless);
    }
    
    private final Joystick operator = new Joystick(OPERATOR_JOYSTICK_ID);

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {

        if (operator.getRawButton(3)) {
            leftLiftMotor.set(-0.5);
            rightLiftMotor.set(-0.5);
        }
        else if (operator.getRawButton(4)) {
            leftLiftMotor.set(0.5);
            rightLiftMotor.set(0.5);
        }
        else {
            leftLiftMotor.set(0.0);
            rightLiftMotor.set(0.0);
        }
    }
}
