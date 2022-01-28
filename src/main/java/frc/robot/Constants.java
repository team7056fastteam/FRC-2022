// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public final class Constants {
    /** Chassis Constants */

    // FL to FR - 0.4953m
    // FL to BR - 0.7087m

    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 0.4953;
    public static final double DRIVETRAIN_WHEELBASE_METERS = 0.4953;

    /** Input/Controller Constants */

    public static final int DRIVER_JOYSTICK_ID = 0;
    public static final int OPERATOR_JOYSTICK_ID = 1;

    /** Drive Constants */

    public static final int FRONT_LEFT_MODULE_DRIVE_MOTOR = 20;
    public static final int FRONT_LEFT_MODULE_STEER_MOTOR = 10;
    public static final int FRONT_LEFT_MODULE_STEER_ENCODER = 0;
    public static final double FRONT_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); // FIXME Measure and set front left steer offset

    public static final int FRONT_RIGHT_MODULE_DRIVE_MOTOR = 21;
    public static final int FRONT_RIGHT_MODULE_STEER_MOTOR = 11;
    public static final int FRONT_RIGHT_MODULE_STEER_ENCODER = 3;
    public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); // FIXME Measure and set front right steer offset

    public static final int BACK_LEFT_MODULE_DRIVE_MOTOR = 23;
    public static final int BACK_LEFT_MODULE_STEER_MOTOR = 13;
    public static final int BACK_LEFT_MODULE_STEER_ENCODER = 1;
    public static final double BACK_LEFT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); // FIXME Measure and set back left steer offset

    public static final int BACK_RIGHT_MODULE_DRIVE_MOTOR = 22;
    public static final int BACK_RIGHT_MODULE_STEER_MOTOR = 12;
    public static final int BACK_RIGHT_MODULE_STEER_ENCODER = 2;
    public static final double BACK_RIGHT_MODULE_STEER_OFFSET = -Math.toRadians(0.0); // FIXME Measure and set back right steer offset

    /** Intake Constants */

    public static final int INTAKE_ROLLER_MOTOR = 8;
    public static final int INTAKE_CONVEYOR_MOTOR = 11;
    public static final int INTAKE_TOP_MOTOR = 9;
    public static final int INTAKE_BOTTOM_MOTOR = 10;

    /** Shooter Constants */

    public static final int SHOOTER_LEFT_MOTOR = 12;
    public static final int SHOOTER_RIGHT_MOTOR = 13;

    /** Lift Constants */

    public static final int LIFT_LEFT_MOTOR = 14;
    public static final int LIFT_RIGHT_MOTOR = 15;
    public static final int ANGLE_LEFT_MOTOR = 16;
    public static final int ANGLE_RIGHT_MOTOR = 17;

    /** Digital Input Constants */
    
    public static final int INTAKE_LIMIT_SWITCH = 0;
    public static final int CONVEYOR_LIMIT_SWITCH = 1;

    public static final int LED_CHANNEL = 2;
}