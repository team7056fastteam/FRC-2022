// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

//import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;

import static frc.robot.Constants.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;

public class Lifter {

    private final Robot robot;

    private final CANSparkMax leftLiftMotor;
    private final CANSparkMax rightLiftMotor;

    public Lifter()
    {
        robot = new Robot();
        
        /** Motor speeds will be reported in dashboard later */
        leftLiftMotor = new CANSparkMax(LIFT_LEFT_MOTOR, MotorType.kBrushless);
        rightLiftMotor = new CANSparkMax(LIFT_RIGHT_MOTOR, MotorType.kBrushless);
    }
    
    private final Joystick operatorController = new Joystick(OPERATOR_JOYSTICK_ID);

    /** Motor speed variables */
    private double liftModifier = 0.5;

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {
        // Check input from Y axis
        if (operatorController.getY() > 0.1) {

            leftLiftMotor.set(liftModifier * operatorController.getY());
            rightLiftMotor.set(robot.invert(liftModifier) * operatorController.getY());
        }
    }
}
