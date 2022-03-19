// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public final class Constants {
    /** Chassis Constants */

    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.4953;
    public static final double DRIVETRAIN_WHEELBASE_METERS = 0.4953;

    /*
        00-01 CAN : PDP, other
        02-05 CAN : Encoders
        10-19 CAN : Steer Motors
        20-29 CAN : Drive Motors
        30-39 CAN : Intake Motors
        40-49 CAN : Lifter Motors
    */

    /** Drive Constants */
    
    // Front Left Module
    public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 20;
    public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 10;
    public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 3;
    public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(270.3515625);

    // Front Right Module
    public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 21;
    public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 11;
    public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 5;
    public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(324.31640625);

    // Back Left Module
    public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 22;
    public static final int BACK_LEFT_MODULE_STEER_MOTOR = 12;
    public static final int BACK_LEFT_MODULE_STEER_ENCODER = 2;
    public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(134.912109375);
    
    // Back Right Module
    public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 23;
    public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 13;
    public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 4;
    public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(37.353515625);

    /** Intake/Shooter Constants */

    // Intake Module
    public static final int INTAKE_ROLLER_MOTOR = 30;
    public static final int INTAKE_CONVEYOR_MOTOR = 31;

    // Shooter Module
    public static final int SHOOTER_LEFT_MOTOR = 32;
    public static final int SHOOTER_RIGHT_MOTOR = 33;

    /** Lift Constants */

    public static final int LIFT_LEFT_MOTOR = 40;
    public static final int LIFT_RIGHT_MOTOR = 41;

    /** Digital Input Constants */
    
    // Limit Switches
    public static final int INTAKE_LIMIT_SWITCH = 0;
    public static final int CONVEYOR_LIMIT_SWITCH = 1;

    // LED Control
    public static final int LED_CHANNEL = 0;

    private static final Joystick driver = new Joystick(0);
    private static final Joystick operator = new Joystick(1);

    /** Functions to recieve joystick input from Constants class */
    public double driverLX() { return driver.getRawAxis(0); }
    public double driverLY() { return driver.getRawAxis(1); }
    public double driverLT() { return driver.getRawAxis(2); }
    public double driverRY() { return driver.getRawAxis(5); }
    public double driverRX() { return driver.getRawAxis(4); }
    public double driverRT() { return driver.getRawAxis(3); }
    public boolean driverA() { return driver.getRawButton(1); }
    public boolean driverB() { return driver.getRawButton(2); }
    public boolean driverX() { return driver.getRawButton(3); }
    public boolean driverY() { return driver.getRawButton(4); }
    public boolean driverLB() { return driver.getRawButton(5); }
    public boolean driverRB() { return driver.getRawButton(6); }
    public boolean driverBack() { return driver.getRawButton(7); }
    public boolean driverHome() { return driver.getRawButton(8); }
    public boolean driverLS() { return driver.getRawButton(9); }
    public boolean driverRS() { return driver.getRawButton(10); }

    public double operatorLX() { return operator.getRawAxis(0); }
    public double operatorLY() { return operator.getRawAxis(1); }
    public double operatorLT() { return operator.getRawAxis(2); }
    public double operatorRY() { return operator.getRawAxis(5); }
    public double operatorRX() { return operator.getRawAxis(4); }
    public double operatorRT() { return operator.getRawAxis(3); }
    public boolean operatorA() { return operator.getRawButton(1); }
    public boolean operatorB() { return operator.getRawButton(2); }
    public boolean operatorX() { return operator.getRawButton(3); }
    public boolean operatorY() { return operator.getRawButton(4); }
    public boolean operatorLB() { return operator.getRawButton(5); }
    public boolean operatorRB() { return operator.getRawButton(6); }
    public boolean operatorBack() { return operator.getRawButton(7); }
    public boolean operatorHome() { return operator.getRawButton(8); }
    public boolean operatorLS() { return operator.getRawButton(9); }
    public boolean operatorRS() { return operator.getRawButton(10); }
}