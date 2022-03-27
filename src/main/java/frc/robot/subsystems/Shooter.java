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

    private Robot _robot;

    Constants constants = new Constants();
    double currentTime;

    private final CANSparkMax leftShooterMotor;
    private final CANSparkMax rightShooterMotor;

    public Shooter(Robot robot)
    {
        _robot = robot;
        leftShooterMotor = new CANSparkMax(SHOOTER_LEFT_MOTOR, MotorType.kBrushless);
        rightShooterMotor = new CANSparkMax(SHOOTER_RIGHT_MOTOR, MotorType.kBrushless);
    }

    /** Configuration */
    double speed = .25;
    int index = 0;

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {
        // Check input from right trigger
        if (constants.operatorRT() > 0.2) {
            runShooter();
        }
        else {
            stop();
        }

        if (constants.operatorRB()) {
            speed = .70;
        }
        else {
            speed = .25;
        }
    }

    public void runShooter() {
        leftShooterMotor.set(_robot.invert(speed));
        rightShooterMotor.set(speed);
    }

    public void forceRunShooter() {
        leftShooterMotor.set(-.70);
        rightShooterMotor.set(.70);
    }

    public void stop() {
        leftShooterMotor.set(0);
        rightShooterMotor.set(0);
    }
}
