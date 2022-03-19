// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.Robot;
import static frc.robot.Constants.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

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
    
    private static final Joystick operator = new Joystick(1);

    /** Configuration */
    double[] speeds = {.70, .35, .45, .55, .25};
    int index = 0;
    boolean hasInc = false;
    boolean hasDec = false;

    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {
        // Check input from right trigger
        if (constants.operatorRT() > 0.2) {
            runShooter();
        }
        else {
            stop();
        }

        // Check input from RB
        if (constants.operatorRB() && hasInc == false) {
            hasInc = true;

            if (index < (speeds.length - 1)) {
                index++;
                operator.setRumble(RumbleType.kLeftRumble, 0);
                operator.setRumble(RumbleType.kRightRumble, 0);
            }
            else {
                operator.setRumble(RumbleType.kLeftRumble, 0.5);
                operator.setRumble(RumbleType.kRightRumble, 0.5);
            }
        }

        // Check input from LB
        else if (constants.operatorLB() && hasDec == false) {
            hasDec = true;

            if (index > 0) {
                operator.setRumble(RumbleType.kLeftRumble, 0);
                operator.setRumble(RumbleType.kRightRumble, 0);
                index--;
            }
            else {
                operator.setRumble(RumbleType.kLeftRumble, 0.5);
                operator.setRumble(RumbleType.kRightRumble, 0.5);
            }
        }

        if (!(constants.operatorLB())) {
            hasDec = false;
            operator.setRumble(RumbleType.kLeftRumble, 0);
            operator.setRumble(RumbleType.kRightRumble, 0);
        }

        if (!(constants.operatorRB())) {
            hasInc = false;
            operator.setRumble(RumbleType.kLeftRumble, 0);
            operator.setRumble(RumbleType.kRightRumble, 0);
        }
    }

    public void runShooter() {
        rightShooterMotor.set(speeds[index]);
        leftShooterMotor.set(robot.invert(speeds[index]));
    }


    public void stop() {
        leftShooterMotor.set(0);
        rightShooterMotor.set(0);
    }
}
