// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public final class Constants {
    /** Chassis Constants */

    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.4953;
    public static final double DRIVETRAIN_WHEELBASE_METERS = 0.4953;

    /** Input/Controller Constants */

    public static final int DRIVER_JOYSTICK_ID = 0;
    public static final int OPERATOR_JOYSTICK_ID = 1;

    /*
        00-01 CAN : PDP, other
        02-05 CAN : Encoders
        10-19 CAN : Steer Motors
        20-29 CAN : Drive Motors
        30-39 CAN : Intake Motors
        40-49 CAN : Lifter Motors
    */

    /*
        How to configure steer offsets:
        Increase - Counter Clockwise
        Decrease - Clockwise
    */

    /** Drive Constants */
    
    // Front Left Module
    public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 20;
    public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 10;
    public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 2;
    public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(0.0);

    // Front Right Module
    public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 21;
    public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 11;
    public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 3;
    public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(0.0);

    // Back Left Module
    public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 22;
    public static final int BACK_LEFT_MODULE_STEER_MOTOR = 12;
    public static final int BACK_LEFT_MODULE_STEER_ENCODER = 4;
    public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(0.0);
    
    // Back Right Module
    public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 23;
    public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 13;
    public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 5;
    public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(0.0);

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
    public static final int LED_CHANNEL = 2;

    /*
        Xbox Controller Inputs, in case needed:

        joystick.getRawAxis(0) - Left X Axis        joystick.getRawButton(1) - A
        joystick.getRawAxis(1) - Left Y Axis        joystick.getRawButton(2) - B
        joystick.getRawAxis(2) - Left Trigger       joystick.getRawButton(3) - X
        joystick.getRawAxis(3) - Right Trigger      joystick.getRawButton(4) - Y
        joystick.getRawAxis(4) - Right X Axis       joystick.getRawButton(5) - Left Bumper
        joystick.getRawAxis(5) - Right Y Axis       joystick.getRawButton(6) - Right Bumper
                                                    joystick.getRawButton(7) - Back
                                                    joystick.getRawButton(8) - Home
                                                    joystick.getRawButton(9) - Left Stick
                                                    joystick.getRawButton(10) - Right Stick
    */
}