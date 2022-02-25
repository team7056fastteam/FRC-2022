// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

// import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;
import frc.robot.Robot;

import static frc.robot.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Shooter {

    private final Robot robot;
    Constants constants = new Constants();
    double currentTime;

    /**
     * Motors are currently initialized as CAN-Spark Max, but this might
     * change in the future. If using a Talon SRX, change the static
     * initialized controllers from CANSparkMax to TalonSRX
     */
    private final CANSparkMax leftShooterMotor;
    private final CANSparkMax rightShooterMotor;

    public Shooter()
    {
        robot = new Robot();
        leftShooterMotor = new CANSparkMax(SHOOTER_LEFT_MOTOR, MotorType.kBrushless);
        rightShooterMotor = new CANSparkMax(SHOOTER_RIGHT_MOTOR, MotorType.kBrushless);
    }
    
    // private final Joystick operator = new Joystick(OPERATOR_JOYSTICK_ID);

    /** Configuration */
    private double shooterSpeed = 0.85;

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {
        
        // Check input from right trigger
        if (constants.operatorRT() > 0.2) {
            runShooter();
        }
    }

    public void runShooter() {
        leftShooterMotor.set(shooterSpeed);
        rightShooterMotor.set(robot.invert(shooterSpeed));
    }

    public void stop() {
        leftShooterMotor.set(0);
        rightShooterMotor.set(0);
    }
}
