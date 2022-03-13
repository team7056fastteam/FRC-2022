// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Robot;
import static frc.robot.Constants.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Shooter {

    private final Robot robot;
    Constants constants = new Constants();
    double currentTime;

    private final CANSparkMax leftShooterMotor;
    private final CANSparkMax rightShooterMotor;

    public Shooter()
    {
        robot = new Robot();
        leftShooterMotor = new CANSparkMax(SHOOTER_LEFT_MOTOR, MotorType.kBrushless);
        rightShooterMotor = new CANSparkMax(SHOOTER_RIGHT_MOTOR, MotorType.kBrushless);
    }

    /** Configuration */
    private double shooterSpeed = 0.251;

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {
        // Check input from right trigger
        if (constants.operatorRT() > 0.2) {
            runShooter();
        }
        else {
            stop();
        }

        // Check input from RB button
        if (constants.operatorRB()) {
            shooterSpeed = 0.8;
        }
        else {
            shooterSpeed = 0.251;
        }
    }

    public void runShooter() {
        rightShooterMotor.set(shooterSpeed);
        leftShooterMotor.set(robot.invert(shooterSpeed));
    }


    public void stop() {
        leftShooterMotor.set(0);
        rightShooterMotor.set(0);
    }
}
